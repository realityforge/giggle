package org.realityforge.giggle.generator.java.server;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLDirective;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLEnumValueDefinition;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLFieldsContainer;
import graphql.schema.GraphQLInputObjectField;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLNonNull;
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
import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import org.realityforge.giggle.generator.Generator;
import org.realityforge.giggle.generator.GeneratorContext;
import org.realityforge.giggle.generator.java.AbstractJavaGenerator;
import org.realityforge.giggle.generator.java.JavaGenUtil;
import org.realityforge.giggle.generator.java.NamingUtil;

@SuppressWarnings( "Duplicates" )
@Generator.MetaData( name = "java-server" )
public class JavaServerGenerator
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
      if ( type instanceof GraphQLEnumType || type instanceof GraphQLInputObjectType )
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
      else if ( type instanceof GraphQLInputObjectType )
      {
        emitInput( context, fullTypeMap, (GraphQLInputObjectType) type );
      }
      else if ( type instanceof GraphQLFieldsContainer )
      {
        final GraphQLFieldsContainer container = (GraphQLFieldsContainer) type;
        for ( final GraphQLFieldDefinition definition : container.getFieldDefinitions() )
        {
          final List<GraphQLArgument> arguments = definition.getArguments();
          if ( !arguments.isEmpty() )
          {
            final String name = container.getName();
            final String argsName =
              "Query".equals( name ) || "Mutation".equals( name ) ?
              definition.getName() :
              container.getName() + definition.getName();
            emitArgs( context, fullTypeMap, NamingUtil.uppercaseFirstCharacter( argsName ) + "Args", arguments );
          }
        }
      }
    }
    writeTypeMappingFile( context, generatedTypeMap );
  }

  private void emitArgs( @Nonnull final GeneratorContext context,
                         @Nonnull final Map<GraphQLType, String> typeMap,
                         @Nonnull final String name,
                         @Nonnull final List<GraphQLArgument> arguments )
    throws IOException
  {
    final TypeSpec.Builder builder = TypeSpec.classBuilder( name );
    builder.addModifiers( Modifier.PUBLIC, Modifier.FINAL );

    final Map<GraphQLArgument, TypeName> argTypes = new HashMap<>();
    for ( final GraphQLArgument argument : arguments )
    {
      argTypes.put( argument, JavaGenUtil.getJavaType( typeMap, argument.getType() ) );
    }

    builder.addMethod( emitArgsFrom( name, arguments, argTypes ) );
    builder.addMethod( emitArgsConstructor( arguments, argTypes ) );
    for ( final GraphQLArgument argument : arguments )
    {
      builder.addField( emitArgsField( argument, argTypes ) );
      builder.addMethod( emitArgsFieldGetter( argument, argTypes ) );
    }

    JavaGenUtil.writeTopLevelType( context, builder );
  }

  @Nonnull
  private MethodSpec emitArgsFrom( @Nonnull final String className,
                                   @Nonnull final List<GraphQLArgument> arguments,
                                   @Nonnull final Map<GraphQLArgument, TypeName> argTypes )
  {
    final MethodSpec.Builder ctor = MethodSpec.methodBuilder( "from" );
    ctor.addModifiers( Modifier.PUBLIC, Modifier.STATIC );
    ctor.addAnnotation( JavaGenUtil.NONNULL_CLASSNAME );
    final ClassName self = ClassName.bestGuess( className );
    ctor.returns( self );

    ctor.addParameter( ParameterSpec.builder( ParameterizedTypeName.get( ClassName.get( Map.class ),
                                                                         ClassName.get( String.class ),
                                                                         ClassName.OBJECT ),
                                              "args",
                                              Modifier.FINAL )
                         .addAnnotation( JavaGenUtil.NONNULL_CLASSNAME )
                         .build() );

    final List<String> params = new ArrayList<>();
    final List<Object> args = new ArrayList<>();
    args.add( self );
    for ( final GraphQLArgument argument : arguments )
    {
      final String name = argument.getName();
      final TypeName typeName = argTypes.get( argument );
      final boolean isInputType = GraphQLTypeUtil.unwrapAll( argument.getType() ) instanceof GraphQLInputObjectType;

      final TypeName javaType =
        isInputType ?
        ParameterizedTypeName.get( Map.class, String.class, Object.class ) :
        typeName;
      ctor.addStatement( "final $T $N = ($T) args.get( $S )",
                         javaType,
                         name,
                         javaType.isPrimitive() ? javaType.box() : javaType,
                         name );
      if ( isInputType )
      {
        params.add( "$T.from( $N )" );
        args.add( typeName );
        args.add( name );
      }
      else
      {
        params.add( "$N" );
        args.add( name );
      }
    }
    ctor.addStatement( "return new $T(" + String.join( ", ", params ) + ")", args.toArray() );

    return ctor.build();
  }

  @Nonnull
  private MethodSpec emitArgsConstructor( @Nonnull final List<GraphQLArgument> arguments,
                                          @Nonnull final Map<GraphQLArgument, TypeName> argTypes )
  {
    final MethodSpec.Builder ctor = MethodSpec.constructorBuilder();
    ctor.addModifiers( Modifier.PUBLIC );
    for ( final GraphQLArgument argument : arguments )
    {
      final GraphQLInputType fieldType = argument.getType();
      ctor.addParameter( ParameterSpec
                           .builder( argTypes.get( argument ), argument.getName(), Modifier.FINAL )
                           .addAnnotation( getNullabilityAnnotation( fieldType ) )
                           .build() );
      if ( GraphQLTypeUtil.isNonNull( fieldType ) )
      {
        ctor.addStatement( "this.$N = $T.requireNonNull( $N )", argument.getName(), Objects.class, argument.getName() );
      }
      else
      {
        ctor.addStatement( "this.$N = $N", argument.getName(), argument.getName() );
      }
    }
    return ctor.build();
  }

  @Nonnull
  private MethodSpec emitArgsFieldGetter( @Nonnull final GraphQLArgument argument,
                                          @Nonnull final Map<GraphQLArgument, TypeName> argTypes )
  {
    final String name = argument.getName();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( "get" + NamingUtil.uppercaseFirstCharacter( name ) );
    builder.addModifiers( Modifier.PUBLIC );
    builder.returns( argTypes.get( argument ) );
    builder.addAnnotation( getNullabilityAnnotation( argument.getType() ) );

    final String description = argument.getDescription();
    if ( null != description )
    {
      builder.addJavadoc( asJavadoc( description ) );
    }
    builder.addStatement( "return $N", name );
    return builder.build();
  }

  @Nonnull
  private FieldSpec emitArgsField( @Nonnull final GraphQLArgument argument,
                                   @Nonnull final Map<GraphQLArgument, TypeName> argTypes )
  {
    final FieldSpec.Builder builder =
      FieldSpec.builder( argTypes.get( argument ), argument.getName(), Modifier.PRIVATE, Modifier.FINAL );
    builder.addAnnotation( getNullabilityAnnotation( argument.getType() ) );

    final String description = argument.getDescription();
    if ( null != description )
    {
      builder.addJavadoc( asJavadoc( description ) );
    }
    return builder.build();
  }

  private void emitInput( @Nonnull final GeneratorContext context,
                          @Nonnull final Map<GraphQLType, String> typeMap,
                          @Nonnull final GraphQLInputObjectType type )
    throws IOException
  {
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
      fieldTypes.put( field, JavaGenUtil.getJavaType( typeMap, field.getType() ) );
    }

    builder.addMethod( emitInputFrom( type, typeMap, fieldTypes ) );
    builder.addMethod( emitInputConstructor( type, fieldTypes ) );
    for ( final GraphQLInputObjectField field : type.getFields() )
    {
      builder.addField( emitField( field, fieldTypes ) );
      builder.addMethod( emitFieldGetter( field, fieldTypes ) );
    }
    JavaGenUtil.writeTopLevelType( context, builder );
  }

  @Nonnull
  private MethodSpec emitInputFrom( @Nonnull final GraphQLInputObjectType type,
                                    @Nonnull final Map<GraphQLType, String> typeMap,
                                    @Nonnull final Map<GraphQLInputObjectField, TypeName> fieldTypes )
  {
    final MethodSpec.Builder ctor = MethodSpec.methodBuilder( "from" );
    ctor.addModifiers( Modifier.PUBLIC, Modifier.STATIC );
    ctor.addAnnotation( JavaGenUtil.NONNULL_CLASSNAME );
    final ClassName self = ClassName.bestGuess( typeMap.get( type ) );
    ctor.returns( self );

    ctor.addParameter( ParameterSpec.builder( ParameterizedTypeName.get( ClassName.get( Map.class ),
                                                                         ClassName.get( String.class ),
                                                                         ClassName.OBJECT ),
                                              "args",
                                              Modifier.FINAL )
                         .addAnnotation( JavaGenUtil.NONNULL_CLASSNAME )
                         .build() );

    boolean suppressedUnchecked = false;

    final List<String> params = new ArrayList<>();
    final List<Object> args = new ArrayList<>();
    args.add( self );
    for ( final GraphQLInputObjectField field : type.getFields() )
    {
      final String name = field.getName();
      final TypeName typeName = fieldTypes.get( field );
      final boolean isInputType = GraphQLTypeUtil.unwrapAll( field.getType() ) instanceof GraphQLInputObjectType;
      final boolean isListType = JavaGenUtil.isList( field.getType() );

      if ( ( isInputType || isListType ) && !suppressedUnchecked )
      {
        suppressedUnchecked = true;
        ctor.addAnnotation( AnnotationSpec.builder( SuppressWarnings.class )
                              .addMember( "value", "$S", "unchecked" )
                              .build() );
      }

      final TypeName javaType =
        isInputType ?
        ParameterizedTypeName.get( Map.class, String.class, Object.class ) :
        typeName;
      ctor.addStatement( "final $T $N = ($T) args.get( $S )",
                         javaType,
                         name,
                         javaType.isPrimitive() ? javaType.box() : javaType,
                         name );
      if ( isInputType )
      {
        params.add( "$T.from( $N )" );
        args.add( typeName );
        args.add( name );
      }
      else
      {
        params.add( "$N" );
        args.add( name );
      }
    }
    ctor.addStatement( "return new $T(" + String.join( ", ", params ) + ")", args.toArray() );

    return ctor.build();
  }

  @Nonnull
  private MethodSpec emitInputConstructor( @Nonnull final GraphQLInputObjectType type,
                                           @Nonnull final Map<GraphQLInputObjectField, TypeName> fieldTypes )
  {
    final MethodSpec.Builder ctor = MethodSpec.constructorBuilder();
    ctor.addModifiers( Modifier.PUBLIC );
    for ( final GraphQLInputObjectField field : type.getFields() )
    {
      final GraphQLInputType fieldType = field.getType();
      ctor.addParameter( ParameterSpec
                           .builder( fieldTypes.get( field ), field.getName(), Modifier.FINAL )
                           .addAnnotation( getNullabilityAnnotation( fieldType ) )
                           .build() );
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

  private ClassName getNullabilityAnnotation( @Nonnull final GraphQLType type )
  {
    return type instanceof GraphQLNonNull ? JavaGenUtil.NONNULL_CLASSNAME : JavaGenUtil.NULLABLE_CLASSNAME;
  }

  @Nonnull
  private MethodSpec emitFieldGetter( @Nonnull final GraphQLInputObjectField field,
                                      @Nonnull final Map<GraphQLInputObjectField, TypeName> fieldTypes )
  {
    final String name = field.getName();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( "get" + NamingUtil.uppercaseFirstCharacter( name ) );
    builder.addModifiers( Modifier.PUBLIC );
    builder.returns( fieldTypes.get( field ) );
    builder.addAnnotation( getNullabilityAnnotation( field.getType() ) );

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
  private FieldSpec emitField( @Nonnull final GraphQLInputObjectField field,
                               @Nonnull final Map<GraphQLInputObjectField, TypeName> fieldTypes )
  {
    final FieldSpec.Builder builder =
      FieldSpec.builder( fieldTypes.get( field ), field.getName(), Modifier.PRIVATE, Modifier.FINAL );
    builder.addAnnotation( getNullabilityAnnotation( field.getType() ) );

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

  private void emitEnum( @Nonnull final GeneratorContext context, @Nonnull final GraphQLEnumType type )
    throws IOException
  {
    final TypeSpec.Builder builder = TypeSpec.enumBuilder( type.getName() );
    builder.addModifiers( Modifier.PUBLIC );
    final String description = type.getDescription();
    if ( null != description )
    {
      builder.addJavadoc( asJavadoc( description ) );
    }
    type.getValues().forEach( value -> emitEnumValue( builder, value ) );
    JavaGenUtil.writeTopLevelType( context, builder );
  }

  private void emitEnumValue( @Nonnull final TypeSpec.Builder enumBuilder,
                              @Nonnull final GraphQLEnumValueDefinition value )
  {
    final TypeSpec.Builder builder = TypeSpec.anonymousClassBuilder( "" );
    final String description = value.getDescription();
    if ( null != description )
    {
      builder.addJavadoc( asJavadoc( description ) );
    }
    final String deprecationReason = value.getDeprecationReason();
    if ( null != deprecationReason )
    {
      builder.addJavadoc( "@deprecated " + deprecationReason + "\n" );
      builder.addAnnotation( AnnotationSpec.builder( Deprecated.class ).build() );
    }
    enumBuilder.addEnumConstant( value.getName(), builder.build() );
  }
}
