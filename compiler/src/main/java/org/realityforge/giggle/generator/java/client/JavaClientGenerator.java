package org.realityforge.giggle.generator.java.client;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import graphql.execution.MergedField;
import graphql.execution.MergedSelectionSet;
import graphql.language.AstPrinter;
import graphql.language.Comment;
import graphql.language.Definition;
import graphql.language.Document;
import graphql.language.Field;
import graphql.language.OperationDefinition;
import graphql.language.SelectionSetContainer;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLFieldsContainer;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLType;
import graphql.schema.GraphQLTypeUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import org.realityforge.giggle.generator.Generator;
import org.realityforge.giggle.generator.GeneratorContext;
import org.realityforge.giggle.generator.java.AbstractJavaGenerator;
import org.realityforge.giggle.generator.java.JavaGenUtil;
import org.realityforge.giggle.generator.java.NamingUtil;

@SuppressWarnings( "Duplicates" )
@Generator.MetaData( name = "java-client" )
public class JavaClientGenerator
  extends AbstractJavaGenerator
{
  @Override
  public void generate( @Nonnull final GeneratorContext context )
    throws Exception
  {
    final Map<GraphQLType, String> inputTypeMap = buildTypeMapping( context );
    final Map<GraphQLType, String> generatedTypeMap = new HashMap<>();
    final GraphQLSchema schema = context.getSchema();
    final List<GraphQLType> types =
      schema.getAllTypesAsList()
        .stream()
        .filter( this::isNotIntrospectionType )
        .filter( t -> !inputTypeMap.containsKey( t ) )
        .collect( Collectors.toList() );
    for ( final GraphQLType type : types )
    {
      if ( type instanceof GraphQLEnumType )
      {
        generatedTypeMap.put( type, context.getPackageName() + "." + type.getName() );
      }
    }
    final Map<GraphQLType, String> fullTypeMap = new HashMap<>();
    fullTypeMap.putAll( inputTypeMap );
    fullTypeMap.putAll( generatedTypeMap );
    for ( final GraphQLType type : types )
    {
      if ( type instanceof GraphQLEnumType )
      {
        emitEnum( context, (GraphQLEnumType) type );
      }
    }
    final FieldCollector collector = new FieldCollector( context.getDocument() );
    final FragmentCollector fragmentCollector = new FragmentCollector( context.getDocument() );

    for ( final Definition definition : context.getDocument().getDefinitions() )
    {
      if ( definition instanceof OperationDefinition )
      {
        final OperationDefinition operation = (OperationDefinition) definition;
        emitOperation( context, collector, fullTypeMap, operation );
        emitOperationDocument( context, fragmentCollector, operation );
      }
    }
    writeTypeMappingFile( context, generatedTypeMap );
  }

  private void emitOperationDocument( @Nonnull final GeneratorContext context,
                                      @Nonnull final FragmentCollector collector,
                                      @Nonnull final OperationDefinition operation )
    throws IOException
  {
    final ArrayList<Definition> definitions = new ArrayList<>();
    definitions.add( operation );
    definitions.addAll( collector.collectFragments( operation.getSelectionSet() ) );
    final Document document = context.getDocument().transform( b -> b.definitions( definitions ) );
    writeFile( getPackageOutputDirectory( context ).resolve( operation.getName() + ".graphql" ),
               AstPrinter.printAstCompact( document ) );
  }

  private void emitOperation( @Nonnull final GeneratorContext context,
                              @Nonnull final FieldCollector collector,
                              @Nonnull final Map<GraphQLType, String> typeMap,
                              @Nonnull final OperationDefinition operation )
    throws IOException
  {
    final String name = operation.getName();
    if ( null == name )
    {
      throw new IllegalStateException( "Unable to generate infrastructure for anonymous operation" );
    }
    final OperationDefinition.Operation operationType = operation.getOperation();
    if ( OperationDefinition.Operation.SUBSCRIPTION == operationType )
    {
      throw new IllegalStateException( "Unable to generate infrastructure for SUBSCRIPTION operation" );
    }
    final String typeName = NamingUtil.uppercaseFirstCharacter( name ) + "Response";

    final GraphQLSchema schema = context.getSchema();
    final GraphQLFieldsContainer fieldsContainer =
      OperationDefinition.Operation.QUERY == operationType ? schema.getQueryType() :
      OperationDefinition.Operation.MUTATION == operationType ? schema.getMutationType() :
      schema.getSubscriptionType();
    JavaGenUtil.writeTopLevelType( context, buildType( collector, operation, typeMap, typeName, fieldsContainer ) );
  }

  @Nonnull
  private TypeSpec.Builder buildType( @Nonnull final FieldCollector collector,
                                      @Nonnull final SelectionSetContainer container,
                                      @Nonnull final Map<GraphQLType, String> typeMap,
                                      @Nonnull final String typeName,
                                      @Nonnull final GraphQLFieldsContainer fieldsContainer )
  {
    final TypeSpec.Builder builder = TypeSpec.classBuilder( typeName );
    builder.addModifiers( Modifier.PUBLIC, Modifier.FINAL );
    buildSelectedValues( collector, typeMap, fieldsContainer, container, builder );
    return builder;
  }

  private void buildSelectedValues( @Nonnull final FieldCollector collector,
                                    @Nonnull final Map<GraphQLType, String> typeMap,
                                    @Nonnull final GraphQLFieldsContainer fieldsContainer,
                                    @Nonnull final SelectionSetContainer selectionSetContainer,
                                    @Nonnull final TypeSpec.Builder builder )
  {
    final MergedSelectionSet selectionSet = collector.collectFields( selectionSetContainer.getSelectionSet() );
    for ( final MergedField field : selectionSet.getSubFields().values() )
    {
      buildFieldSelection( collector, typeMap, fieldsContainer, builder, field );
    }
  }

  private void buildFieldSelection( @Nonnull final FieldCollector collector,
                                    @Nonnull final Map<GraphQLType, String> typeMap,
                                    @Nonnull final GraphQLFieldsContainer fieldsContainer,
                                    @Nonnull final TypeSpec.Builder builder,
                                    @Nonnull final MergedField mergedField )
  {
    final Field selection = mergedField.getSingleField();
    final String alias = selection.getAlias();
    final String name = null == alias ? selection.getName() : alias;

    final GraphQLFieldDefinition fieldDefinition = fieldsContainer.getFieldDefinition( selection.getName() );
    assert null != fieldDefinition;

    final TypeName fieldType;
    if ( selection.getChildren().isEmpty() )
    {
      fieldType = JavaGenUtil.getJavaType( typeMap, fieldDefinition );
    }
    else
    {
      final GraphQLObjectType type = (GraphQLObjectType) GraphQLTypeUtil.unwrapAll( fieldDefinition.getType() );
      final String typeName = NamingUtil.uppercaseFirstCharacter( name );
      final TypeSpec.Builder subType =
        buildType( collector, selection, typeMap, typeName, type );
      subType.addModifiers( Modifier.STATIC );
      builder.addType( subType.build() );
      final boolean isList = JavaGenUtil.isList( fieldDefinition.getType() );
      fieldType = isList ? JavaGenUtil.listOf( ClassName.bestGuess( typeName ) ) : ClassName.bestGuess( typeName );
    }

    final FieldSpec.Builder field = FieldSpec.builder( fieldType, name, Modifier.PRIVATE/*, Modifier.FINAL*/ );
    if ( !fieldType.isPrimitive() )
    {
      field.addAnnotation( GraphQLTypeUtil.isNonNull( fieldDefinition.getType() ) ?
                           JavaGenUtil.NONNULL_CLASSNAME :
                           JavaGenUtil.NULLABLE_CLASSNAME );
    }
    final List<Comment> comments = selection.getComments();
    if ( !comments.isEmpty() )
    {
      for ( final Comment comment : comments )
      {
        field.addJavadoc( asJavadoc( comment.getContent() ) );
      }
    }
    builder.addField( field.build() );

    final MethodSpec.Builder getter =
      MethodSpec.methodBuilder( ( fieldType == TypeName.BOOLEAN ? "is" : "get" ) +
                                NamingUtil.uppercaseFirstCharacter( name ) )
        .addModifiers( Modifier.PUBLIC )
        .returns( fieldType )
        .addStatement( "return $N", name );
    if ( !fieldType.isPrimitive() )
    {
      getter.addAnnotation( GraphQLTypeUtil.isNonNull( fieldDefinition.getType() ) ?
                            JavaGenUtil.NONNULL_CLASSNAME :
                            JavaGenUtil.NULLABLE_CLASSNAME );
    }
    builder.addMethod( getter.build() );

    final ParameterSpec.Builder parameter = ParameterSpec.builder( fieldType, name, Modifier.FINAL );
    if ( !fieldType.isPrimitive() )
    {
      parameter.addAnnotation( GraphQLTypeUtil.isNonNull( fieldDefinition.getType() ) ?
                               JavaGenUtil.NONNULL_CLASSNAME :
                               JavaGenUtil.NULLABLE_CLASSNAME );
    }
    final MethodSpec.Builder setter = MethodSpec.methodBuilder( "set" + NamingUtil.uppercaseFirstCharacter( name ) )
      .addModifiers( Modifier.PUBLIC )
      .addParameter( parameter.build() )
      .addStatement( "this.$N = $N", name, name );
    builder.addMethod( setter.build() );
  }
}
