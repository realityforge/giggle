package org.realityforge.giggle.generator.java.client;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import graphql.execution.MergedField;
import graphql.execution.MergedSelectionSet;
import graphql.language.AstPrinter;
import graphql.language.Comment;
import graphql.language.Definition;
import graphql.language.Document;
import graphql.language.Field;
import graphql.language.NonNullType;
import graphql.language.OperationDefinition;
import graphql.language.SelectionSetContainer;
import graphql.language.VariableDefinition;
import graphql.schema.GraphQLDirective;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLFieldsContainer;
import graphql.schema.GraphQLInputObjectField;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLType;
import graphql.schema.GraphQLTypeUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
  private static final String GRAPH_QL_ERROR_TYPE_NAME = "GraphQLError";

  @Override
  public void generate( @Nonnull final GeneratorContext context )
    throws Exception
  {
    final Map<GraphQLType, String> inputTypeMap = buildTypeMapping( context );
    final List<GraphQLType> types = extractTypesToGenerate( context.getSchema(), inputTypeMap );
    final Map<GraphQLType, String> generatedTypeMap = new HashMap<>();
    for ( final GraphQLType type : types )
    {
      if ( type instanceof GraphQLEnumType | type instanceof GraphQLInputObjectType )
      {
        generatedTypeMap.put( type, context.getPackageName() + "." + type.getName() );
      }
    }

    final Map<GraphQLType, String> fullTypeMap = new HashMap<>( inputTypeMap );
    fullTypeMap.putAll( generatedTypeMap );

    emitGraphQLError( context );
    emitEnums( context, types );
    emitInputs( context, fullTypeMap, types );
    emitOperations( context, fullTypeMap );

    writeTypeMappingFile( context, generatedTypeMap );
  }

  private void emitEnums( @Nonnull final GeneratorContext context, @Nonnull final List<GraphQLType> types )
    throws IOException
  {
    for ( final GraphQLType type : types )
    {
      if ( type instanceof GraphQLEnumType )
      {
        emitEnum( context, (GraphQLEnumType) type );
      }
    }
  }

  private void emitInputs( @Nonnull final GeneratorContext context,
                           @Nonnull final Map<GraphQLType, String> fullTypeMap,
                           @Nonnull final List<GraphQLType> types )
    throws IOException
  {
    for ( final GraphQLType type : types )
    {
      if ( type instanceof GraphQLInputObjectType )
      {
        emitInput( context, fullTypeMap, (GraphQLInputObjectType) type );
      }
    }
  }

  private void emitOperations( @Nonnull final GeneratorContext context,
                               @Nonnull final Map<GraphQLType, String> typeMap )
    throws IOException
  {
    final FieldCollector collector = new FieldCollector( context.getDocument() );
    final FragmentCollector fragmentCollector = new FragmentCollector( context.getDocument() );

    for ( final Definition definition : context.getDocument().getDefinitions() )
    {
      if ( definition instanceof OperationDefinition )
      {
        final OperationDefinition operation = (OperationDefinition) definition;
        if ( OperationDefinition.Operation.SUBSCRIPTION != operation.getOperation() )
        {
          emitOperationResponse( context, collector, typeMap, operation );
          emitOperationType( context, fragmentCollector, typeMap, operation );
        }
      }
    }
  }

  private void emitInput( @Nonnull final GeneratorContext context,
                          @Nonnull final Map<GraphQLType, String> typeMap,
                          @Nonnull final GraphQLInputObjectType type )
    throws IOException
  {
    final ClassName self = ClassName.bestGuess( typeMap.get( type ) );
    final TypeSpec.Builder builder = TypeSpec.classBuilder( type.getName() );
    builder.addModifiers( Modifier.PUBLIC, Modifier.FINAL );
    final String description = type.getDescription();
    if ( null != description )
    {
      builder.addJavadoc( asJavadoc( description ) );
    }
    final Map<GraphQLInputObjectField, TypeName> fieldTypes = new HashMap<>();
    for ( final GraphQLInputObjectField field : type.getFields() )
    {
      fieldTypes.put( field, JavaGenUtil.getJavaType( typeMap, field ) );
    }

    builder.addMethod( buildInputConstructor( type, fieldTypes ) );
    for ( final GraphQLInputObjectField field : type.getFields() )
    {
      builder.addField( buildInputFieldField( field, fieldTypes ) );
      builder.addMethod( buildInputFieldGetter( field, fieldTypes ) );
    }
    builder.addMethod( buildInputEquals( self, type ) );
    builder.addMethod( buildInputHashCode( type ) );
    builder.addMethod( buildInputToString( type ) );
    JavaGenUtil.writeTopLevelType( context, builder );
  }

  @Nonnull
  private MethodSpec buildInputConstructor( @Nonnull final GraphQLInputObjectType type,
                                            @Nonnull final Map<GraphQLInputObjectField, TypeName> fieldTypes )
  {
    final MethodSpec.Builder ctor = MethodSpec.constructorBuilder();
    ctor.addModifiers( Modifier.PUBLIC );
    for ( final GraphQLInputObjectField field : type.getFields() )
    {
      final GraphQLInputType fieldType = field.getType();
      final TypeName javaType = fieldTypes.get( field );
      final ParameterSpec.Builder parameter = ParameterSpec.builder( javaType, field.getName(), Modifier.FINAL );
      if ( !javaType.isPrimitive() )
      {
        parameter.addAnnotation( GraphQLTypeUtil.isNonNull( fieldType ) ? Nonnull.class : Nullable.class );
      }
      ctor.addParameter( parameter.build() );
      if ( GraphQLTypeUtil.isNonNull( fieldType ) )
      {
        ctor.addStatement( "this.$N = $T.requireNonNull( $N )", field.getName(), Objects.class, field.getName() );
      }
      else
      {
        ctor.addStatement( "this.$N = $N", field.getName(), field.getName() );
      }
    }
    return ctor.build();
  }

  @Nonnull
  private MethodSpec buildInputFieldGetter( @Nonnull final GraphQLInputObjectField field,
                                            @Nonnull final Map<GraphQLInputObjectField, TypeName> fieldTypes )
  {
    final String name = field.getName();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( "get" + NamingUtil.uppercaseFirstCharacter( name ) );
    builder.addModifiers( Modifier.PUBLIC );
    final TypeName javaType = fieldTypes.get( field );
    builder.returns( javaType );
    if ( !javaType.isPrimitive() )
    {
      builder.addAnnotation( GraphQLTypeUtil.isNonNull( field.getType() ) ? Nonnull.class : Nullable.class );
    }

    final String description = field.getDescription();
    if ( null != description )
    {
      builder.addJavadoc( asJavadoc( description ) );
    }
    final GraphQLDirective deprecated = field.getDirective( "deprecated" );
    if ( null != deprecated )
    {
      builder.addJavadoc( "@deprecated " + deprecated.getArgument( "reason" ) + "\n" );
      builder.addAnnotation( AnnotationSpec.builder( Deprecated.class ).build() );
    }
    builder.addStatement( "return $N", name );
    return builder.build();
  }

  @Nonnull
  private FieldSpec buildInputFieldField( @Nonnull final GraphQLInputObjectField field,
                                          @Nonnull final Map<GraphQLInputObjectField, TypeName> fieldTypes )
  {
    final TypeName javaType = fieldTypes.get( field );
    final FieldSpec.Builder builder =
      FieldSpec.builder( javaType, field.getName(), Modifier.PRIVATE, Modifier.FINAL );
    if ( !javaType.isPrimitive() )
    {
      builder.addAnnotation( GraphQLTypeUtil.isNonNull( field.getType() ) ? Nonnull.class : Nullable.class );
    }

    final String description = field.getDescription();
    if ( null != description )
    {
      builder.addJavadoc( asJavadoc( description ) );
    }
    final GraphQLDirective deprecated = field.getDirective( "deprecated" );
    if ( null != deprecated )
    {
      builder.addJavadoc( "@deprecated " + deprecated.getArgument( "reason" ) + "\n" );
      builder.addAnnotation( AnnotationSpec.builder( Deprecated.class ).build() );
    }
    return builder.build();
  }

  @Nonnull
  private MethodSpec buildInputEquals( @Nonnull final ClassName self, @Nonnull final GraphQLInputObjectType type )
  {
    final MethodSpec.Builder method =
      MethodSpec.methodBuilder( "equals" ).
        addModifiers( Modifier.PUBLIC ).
        addAnnotation( Override.class ).
        addParameter( Object.class, "o", Modifier.FINAL ).
        returns( TypeName.BOOLEAN );

    final CodeBlock.Builder codeBlock = CodeBlock.builder();
    codeBlock.beginControlFlow( "if ( this == o )" );
    codeBlock.addStatement( "return true" );
    codeBlock.nextControlFlow( "else if ( !( o instanceof $T ) )", self );
    codeBlock.addStatement( "return false" );
    codeBlock.nextControlFlow( "else" );
    codeBlock.addStatement( "final $T that = ($T) o", self, self );

    final ArrayList<Object> args = new ArrayList<>();
    final String expr = type.getFields().stream().map( field -> {
      args.add( Objects.class );
      args.add( field.getName() );
      args.add( field.getName() );
      return "$T.equals( $N, that.$N )";
    } ).collect( Collectors.joining( " && " ) );
    codeBlock.addStatement( "return " + expr, args.toArray() );
    codeBlock.endControlFlow();
    method.addCode( codeBlock.build() );
    return method.build();
  }

  @Nonnull
  private MethodSpec buildInputHashCode( @Nonnull final GraphQLInputObjectType type )
  {
    final String fields = type.getFields().stream().map( f -> "$N" ).collect( Collectors.joining( ", " ) );
    return MethodSpec.methodBuilder( "hashCode" )
      .addModifiers( Modifier.PUBLIC )
      .addAnnotation( Override.class )
      .returns( TypeName.INT )
      .addStatement( "return $T.hash( " + fields + " )",
                     Stream.concat( Stream.of( Objects.class ),
                                    type.getFields().stream().map( GraphQLInputObjectField::getName ) ).toArray() )
      .build();
  }

  @Nonnull
  private MethodSpec buildInputToString( @Nonnull final GraphQLInputObjectType type )
  {
    final String fields =
      type.getFields().stream().map( f -> "$N=\" + $N" ).collect( Collectors.joining( " + \", " ) );
    return MethodSpec.methodBuilder( "toString" )
      .addModifiers( Modifier.PUBLIC )
      .addAnnotation( Override.class )
      .returns( String.class )
      .addStatement( "return \"$N[" + fields + " + \"]\"",
                     Stream.concat( Stream.of( type.getName() ),
                                    type.getFields()
                                      .stream()
                                      .flatMap( field -> Stream.of( field.getName(), field.getName() ) ) )
                       .toArray() )
      .build();
  }

  @Nonnull
  private String toCompactDocument( @Nonnull final GeneratorContext context,
                                    @Nonnull final FragmentCollector collector,
                                    @Nonnull final OperationDefinition operation )
  {
    final ArrayList<Definition> definitions =
      new ArrayList<>( collector.collectFragments( operation.getSelectionSet() ) );
    definitions.add( operation );
    final Document document = context.getDocument().transform( b -> b.definitions( definitions ) );
    return AstPrinter.printAstCompact( document );
  }

  private void emitOperationType( @Nonnull final GeneratorContext context,
                                  @Nonnull final FragmentCollector fragmentCollector,
                                  @Nonnull final Map<GraphQLType, String> typeMap,
                                  @Nonnull final OperationDefinition operation )
    throws IOException
  {
    final String name = operation.getName();
    assert null != name;
    final OperationDefinition.Operation operationType = operation.getOperation();
    final String typeName =
      NamingUtil.uppercaseFirstCharacter( name ) +
      (
        OperationDefinition.Operation.QUERY == operationType ? "Query" :
        OperationDefinition.Operation.MUTATION == operationType ? "Mutation" :
        "Subscription"
      );

    final TypeSpec.Builder builder = TypeSpec.classBuilder( typeName );
    builder.addModifiers( Modifier.PUBLIC, Modifier.FINAL );

    builder.addField( FieldSpec.builder( String.class, "QUERY", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL )
                        .addAnnotation( Nonnull.class )
                        .initializer( "$S", toCompactDocument( context, fragmentCollector, operation ) )
                        .build() );
    builder.addMethod( MethodSpec.constructorBuilder().addModifiers( Modifier.PRIVATE ).build() );
    if ( !operation.getVariableDefinitions().isEmpty() )
    {
      builder.addType( buildVariableType( typeMap, operation ).build() );
    }
    builder.addType( buildQuestionType( typeMap, operation ).build() );
    builder.addType( buildAnswerType( operation ).build() );
    JavaGenUtil.writeTopLevelType( context, builder );
  }

  private void emitOperationResponse( @Nonnull final GeneratorContext context,
                                      @Nonnull final FieldCollector collector,
                                      @Nonnull final Map<GraphQLType, String> typeMap,
                                      @Nonnull final OperationDefinition operation )
    throws IOException
  {
    final String name = operation.getName();
    assert null != name;
    final OperationDefinition.Operation operationType = operation.getOperation();
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
      field.addAnnotation( GraphQLTypeUtil.isNonNull( fieldDefinition.getType() ) ? Nonnull.class : Nullable.class );
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
      getter.addAnnotation( GraphQLTypeUtil.isNonNull( fieldDefinition.getType() ) ? Nonnull.class : Nullable.class );
    }
    builder.addMethod( getter.build() );

    final ParameterSpec.Builder parameter = ParameterSpec.builder( fieldType, name, Modifier.FINAL );
    if ( !fieldType.isPrimitive() )
    {
      parameter.addAnnotation( GraphQLTypeUtil.isNonNull( fieldDefinition.getType() ) ?
                               Nonnull.class :
                               Nullable.class );
    }
    final MethodSpec.Builder setter = MethodSpec.methodBuilder( "set" + NamingUtil.uppercaseFirstCharacter( name ) )
      .addModifiers( Modifier.PUBLIC )
      .addParameter( parameter.build() )
      .addStatement( "this.$N = $N", name, name );
    builder.addMethod( setter.build() );
  }

  private void emitGraphQLError( @Nonnull final GeneratorContext context )
    throws IOException
  {
    final TypeSpec.Builder builder = TypeSpec.classBuilder( GRAPH_QL_ERROR_TYPE_NAME );
    builder.addModifiers( Modifier.PUBLIC, Modifier.FINAL );

    builder.addType( buildErrorLocationType().build() );

    // Message property
    {
      builder.addField( FieldSpec.builder( String.class, "message", Modifier.PRIVATE ).build() );

      builder.addMethod( MethodSpec.methodBuilder( "getMessage" )
                           .addModifiers( Modifier.PUBLIC )
                           .returns( String.class )
                           .addAnnotation( Nonnull.class )
                           .addStatement( "return $T.requireNonNull( message )", Objects.class )
                           .build() );
      builder.addMethod( MethodSpec.methodBuilder( "setMessage" )
                           .addModifiers( Modifier.PUBLIC )
                           .addParameter( ParameterSpec.builder( String.class, "message", Modifier.FINAL )
                                            .addAnnotation( Nonnull.class )
                                            .build() )
                           .addStatement( "this.message = $T.requireNonNull( message )", Objects.class )
                           .build() );
    }

    // Path property
    {
      final Class<?> type = Object[].class;
      builder.addField( FieldSpec.builder( type, "path", Modifier.PRIVATE ).addAnnotation( Nullable.class ).build() );

      builder.addMethod( MethodSpec.methodBuilder( "getPath" )
                           .addModifiers( Modifier.PUBLIC )
                           .returns( type )
                           .addAnnotation( Nullable.class )
                           .addStatement( "return path" )
                           .build() );
      builder.addMethod( MethodSpec.methodBuilder( "setPath" )
                           .addModifiers( Modifier.PUBLIC )
                           .addParameter( ParameterSpec.builder( type, "path", Modifier.FINAL )
                                            .addAnnotation( Nullable.class )
                                            .build() )
                           .addStatement( "this.path = path" )
                           .build() );
    }

    // Locations property
    {
      final ArrayTypeName type = ArrayTypeName.of( ClassName.bestGuess( "Location" ) );
      builder.addField( FieldSpec.builder( type, "locations", Modifier.PRIVATE )
                          .addAnnotation( Nullable.class )
                          .build() );

      builder.addMethod( MethodSpec.methodBuilder( "getLocations" )
                           .addModifiers( Modifier.PUBLIC )
                           .returns( type )
                           .addAnnotation( Nullable.class )
                           .addStatement( "return locations" )
                           .build() );
      builder.addMethod( MethodSpec.methodBuilder( "setLocations" )
                           .addModifiers( Modifier.PUBLIC )
                           .addParameter( ParameterSpec.builder( type, "locations", Modifier.FINAL )
                                            .addAnnotation( Nullable.class )
                                            .build() )
                           .addStatement( "this.locations = locations" )
                           .build() );
    }

    // Extensions property
    {
      final ParameterizedTypeName type = ParameterizedTypeName.get( Map.class, String.class, Object.class );
      builder.addField( FieldSpec.builder( type, "extensions", Modifier.PRIVATE )
                          .addAnnotation( Nullable.class )
                          .build() );

      builder.addMethod( MethodSpec.methodBuilder( "getExtensions" )
                           .addModifiers( Modifier.PUBLIC )
                           .returns( type )
                           .addAnnotation( Nullable.class )
                           .addStatement( "return extensions" )
                           .build() );
      builder.addMethod( MethodSpec.methodBuilder( "setExtensions" )
                           .addModifiers( Modifier.PUBLIC )
                           .addParameter( ParameterSpec.builder( type, "extensions", Modifier.FINAL )
                                            .addAnnotation( Nullable.class )
                                            .build() )
                           .addStatement( "this.extensions = extensions" )
                           .build() );
    }

    JavaGenUtil.writeTopLevelType( context, builder );
  }

  @Nonnull
  private TypeSpec.Builder buildErrorLocationType()
  {
    final TypeSpec.Builder builder = TypeSpec.classBuilder( "Location" );
    builder.addModifiers( Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL );

    // line property
    {
      builder.addField( FieldSpec.builder( int.class, "line", Modifier.PRIVATE ).build() );

      builder.addMethod( MethodSpec.methodBuilder( "getLine" )
                           .addModifiers( Modifier.PUBLIC )
                           .returns( int.class )
                           .addStatement( "return line" )
                           .build() );
      builder.addMethod( MethodSpec.methodBuilder( "setLine" )
                           .addModifiers( Modifier.PUBLIC )
                           .addParameter( ParameterSpec.builder( int.class, "line", Modifier.FINAL ).build() )
                           .addStatement( "this.line = line" )
                           .build() );
    }

    // column property
    {
      builder.addField( FieldSpec.builder( int.class, "column", Modifier.PRIVATE ).build() );

      builder.addMethod( MethodSpec.methodBuilder( "getColumn" )
                           .addModifiers( Modifier.PUBLIC )
                           .returns( int.class )
                           .addStatement( "return column" )
                           .build() );
      builder.addMethod( MethodSpec.methodBuilder( "setColumn" )
                           .addModifiers( Modifier.PUBLIC )
                           .addParameter( ParameterSpec.builder( int.class, "column", Modifier.FINAL ).build() )
                           .addStatement( "this.column = column" )
                           .build() );
    }

    return builder;
  }

  @Nonnull
  private TypeSpec.Builder buildVariableType( @Nonnull final Map<GraphQLType, String> typeMap,
                                              @Nonnull final OperationDefinition operation )
  {
    final TypeSpec.Builder builder = TypeSpec.classBuilder( "Variables" );
    builder.addModifiers( Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL );

    final Map<VariableDefinition, TypeName> variableTypes = new HashMap<>();
    for ( final VariableDefinition variable : operation.getVariableDefinitions() )
    {
      variableTypes.put( variable, JavaGenUtil.getJavaType( typeMap, variable.getType() ) );
    }

    builder.addMethod( buildVariablesConstructor( operation, variableTypes ) );
    for ( final VariableDefinition variable : operation.getVariableDefinitions() )
    {
      builder.addField( buildVariableField( variable, variableTypes ) );
      builder.addMethod( buildVariableGetter( variable, variableTypes ) );
    }

    return builder;
  }

  @Nonnull
  private MethodSpec buildVariablesConstructor( @Nonnull final OperationDefinition operation,
                                                @Nonnull final Map<VariableDefinition, TypeName> variableTypes )
  {
    final MethodSpec.Builder ctor = MethodSpec.constructorBuilder();
    ctor.addModifiers( Modifier.PRIVATE );
    for ( final VariableDefinition variable : operation.getVariableDefinitions() )
    {
      final TypeName javaType = variableTypes.get( variable );
      final ParameterSpec.Builder parameter = ParameterSpec.builder( javaType, variable.getName(), Modifier.FINAL );
      final boolean isNonnull = variable.getType() instanceof NonNullType;
      if ( !javaType.isPrimitive() )
      {
        parameter.addAnnotation( isNonnull ? Nonnull.class : Nullable.class );
      }
      ctor.addParameter( parameter.build() );
      if ( isNonnull )
      {
        ctor.addStatement( "this.$N = $T.requireNonNull( $N )", variable.getName(), Objects.class, variable.getName() );
      }
      else
      {
        ctor.addStatement( "this.$N = $N", variable.getName(), variable.getName() );
      }
    }
    return ctor.build();
  }

  @Nonnull
  private FieldSpec buildVariableField( @Nonnull final VariableDefinition variable,
                                        @Nonnull final Map<VariableDefinition, TypeName> variableTypes )
  {
    final TypeName javaType = variableTypes.get( variable );
    final FieldSpec.Builder builder =
      FieldSpec.builder( javaType, variable.getName(), Modifier.PRIVATE, Modifier.FINAL );
    if ( !javaType.isPrimitive() )
    {
      builder.addAnnotation( variable.getType() instanceof NonNullType ? Nonnull.class : Nullable.class );
    }
    return builder.build();
  }

  @Nonnull
  private MethodSpec buildVariableGetter( @Nonnull final VariableDefinition variable,
                                          @Nonnull final Map<VariableDefinition, TypeName> variableTypes )
  {
    final String name = variable.getName();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( "get" + NamingUtil.uppercaseFirstCharacter( name ) );
    builder.addModifiers( Modifier.PUBLIC );
    final TypeName javaType = variableTypes.get( variable );
    builder.returns( javaType );
    if ( !javaType.isPrimitive() )
    {
      builder.addAnnotation( variable.getType() instanceof NonNullType ? Nonnull.class : Nullable.class );
    }
    builder.addStatement( "return $N", name );
    return builder.build();
  }

  @Nonnull
  private TypeSpec.Builder buildQuestionType( @Nonnull final Map<GraphQLType, String> typeMap,
                                              @Nonnull final OperationDefinition operation )
  {
    final TypeSpec.Builder builder = TypeSpec.classBuilder( "Question" );
    builder.addModifiers( Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL );

    final Map<VariableDefinition, TypeName> variableTypes = new HashMap<>();
    for ( final VariableDefinition variable : operation.getVariableDefinitions() )
    {
      variableTypes.put( variable, JavaGenUtil.getJavaType( typeMap, variable.getType() ) );
    }

    if ( !variableTypes.isEmpty() )
    {
      final ClassName variablesType = ClassName.bestGuess( "Variables" );
      builder.addField( FieldSpec.builder( variablesType,
                                           "variables",
                                           Modifier.PRIVATE,
                                           Modifier.FINAL ).addAnnotation( Nonnull.class ).build() );
      builder.addMethod( buildQuestionConstructor( operation, variableTypes ) );

      builder.addMethod( MethodSpec.methodBuilder( "getVariables" )
                           .addModifiers( Modifier.PUBLIC )
                           .addAnnotation( Nonnull.class )
                           .returns( variablesType )
                           .addStatement( "return variables" )
                           .build() );
    }

    builder.addMethod( MethodSpec.methodBuilder( "getQuery" )
                         .addModifiers( Modifier.PUBLIC )
                         .addAnnotation( Nonnull.class )
                         .returns( ClassName.get( String.class ) )
                         .addStatement( "return QUERY" )
                         .build() );

    return builder;
  }

  @Nonnull
  private MethodSpec buildQuestionConstructor( @Nonnull final OperationDefinition operation,
                                               @Nonnull final Map<VariableDefinition, TypeName> variableTypes )
  {
    final MethodSpec.Builder ctor = MethodSpec.constructorBuilder();
    ctor.addModifiers( Modifier.PUBLIC );
    final StringBuilder sb = new StringBuilder();
    final ArrayList<Object> params = new ArrayList<>();
    sb.append( "this.$N = new Variables( " );
    params.add( "variables" );
    boolean first = true;
    for ( final VariableDefinition variable : operation.getVariableDefinitions() )
    {
      final TypeName javaType = variableTypes.get( variable );
      final ParameterSpec.Builder parameter = ParameterSpec.builder( javaType, variable.getName(), Modifier.FINAL );
      final boolean isNonnull = variable.getType() instanceof NonNullType;
      if ( !javaType.isPrimitive() )
      {
        parameter.addAnnotation( isNonnull ? Nonnull.class : Nullable.class );
      }
      ctor.addParameter( parameter.build() );
      if ( !first )
      {
        sb.append( ", $N" );
      }
      else
      {
        sb.append( "$N" );
        first = false;
      }
      params.add( variable.getName() );
    }
    sb.append( " )" );
    ctor.addStatement( sb.toString(), params.toArray() );
    return ctor.build();
  }

  @Nonnull
  private TypeSpec.Builder buildAnswerType( @Nonnull final OperationDefinition operation )
  {
    final TypeSpec.Builder builder = TypeSpec.classBuilder( "Answer" );
    builder.addModifiers( Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL );

    // data property
    {
      final String name = operation.getName();
      assert null != name;
      final ClassName type = ClassName.bestGuess( NamingUtil.uppercaseFirstCharacter( name ) + "Response" );
      builder.addField( FieldSpec.builder( type, "data", Modifier.PRIVATE ).addAnnotation( Nullable.class ).build() );

      builder.addMethod( MethodSpec.methodBuilder( "hasData" )
                           .addModifiers( Modifier.PUBLIC )
                           .returns( TypeName.BOOLEAN )
                           .addStatement( "return null != data" )
                           .build() );
      builder.addMethod( MethodSpec.methodBuilder( "getData" )
                           .addModifiers( Modifier.PUBLIC )
                           .addAnnotation( Nonnull.class )
                           .returns( type )
                           .addStatement( "return $T.requireNonNull( data )", Objects.class )
                           .build() );
      builder.addMethod( MethodSpec.methodBuilder( "setData" )
                           .addModifiers( Modifier.PUBLIC )
                           .addParameter( ParameterSpec.builder( type, "data", Modifier.FINAL )
                                            .addAnnotation( Nullable.class )
                                            .build() )
                           .addStatement( "this.data = data" )
                           .build() );
    }

    // errors property
    {
      final ArrayTypeName type = ArrayTypeName.of( ClassName.bestGuess( "GraphQLError" ) );
      builder.addField( FieldSpec.builder( type, "errors", Modifier.PRIVATE ).addAnnotation( Nullable.class ).build() );

      builder.addMethod( MethodSpec.methodBuilder( "hasErrors" )
                           .addModifiers( Modifier.PUBLIC )
                           .returns( TypeName.BOOLEAN )
                           .addStatement( "return null != errors" )
                           .build() );
      builder.addMethod( MethodSpec.methodBuilder( "getErrors" )
                           .addModifiers( Modifier.PUBLIC )
                           .addAnnotation( Nonnull.class )
                           .returns( type )
                           .addStatement( "return $T.requireNonNull( errors )", Objects.class )
                           .build() );
      builder.addMethod( MethodSpec.methodBuilder( "setErrors" )
                           .addModifiers( Modifier.PUBLIC )
                           .addParameter( ParameterSpec.builder( type, "errors", Modifier.FINAL )
                                            .addAnnotation( Nullable.class )
                                            .build() )
                           .addStatement( "this.errors = errors" )
                           .build() );
    }

    return builder;
  }
}
