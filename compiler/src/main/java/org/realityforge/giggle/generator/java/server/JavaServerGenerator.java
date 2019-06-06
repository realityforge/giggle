package org.realityforge.giggle.generator.java.server;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
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
  private static final ParameterizedTypeName VALUE_MAP =
    ParameterizedTypeName.get( Map.class, String.class, Object.class );

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

    builder.addMethod( buildArgsFrom( name, arguments, argTypes ) );
    builder.addMethod( buildArgsConstructor( arguments, argTypes ) );
    for ( final GraphQLArgument argument : arguments )
    {
      builder.addField( buildArgsArgField( argument, argTypes ) );
      builder.addMethod( buildArgsArgGetter( argument, argTypes ) );
    }

    JavaGenUtil.writeTopLevelType( context, builder );
  }

  @Nonnull
  private MethodSpec buildArgsFrom( @Nonnull final String className,
                                    @Nonnull final List<GraphQLArgument> arguments,
                                    @Nonnull final Map<GraphQLArgument, TypeName> argTypes )
  {
    final MethodSpec.Builder method = MethodSpec.methodBuilder( "from" );
    method.addModifiers( Modifier.PUBLIC, Modifier.STATIC );
    method.addAnnotation( JavaGenUtil.NONNULL_CLASSNAME );
    final ClassName self = ClassName.bestGuess( className );
    method.returns( self );

    method.addParameter( ParameterSpec.builder( VALUE_MAP, "args", Modifier.FINAL )
                           .addAnnotation( JavaGenUtil.NONNULL_CLASSNAME )
                           .build() );

    boolean suppressedUnchecked = false;

    final List<String> params = new ArrayList<>();
    final List<Object> args = new ArrayList<>();
    args.add( self );
    for ( final GraphQLArgument argument : arguments )
    {
      final String name = VAR_PREFIX + argument.getName();
      final TypeName typeName = argTypes.get( argument );
      final GraphQLType graphQLType = argument.getType();

      final boolean isInputType = GraphQLTypeUtil.unwrapAll( graphQLType ) instanceof GraphQLInputObjectType;
      final boolean isListType = JavaGenUtil.isList( graphQLType );

      if ( ( isInputType || isListType ) && !suppressedUnchecked )
      {
        suppressedUnchecked = true;
        method.addAnnotation( AnnotationSpec.builder( SuppressWarnings.class )
                                .addMember( "value", "$S", "unchecked" )
                                .build() );
      }

      final TypeName javaType =
        isInputType && isListType ? JavaGenUtil.listOf( VALUE_MAP ) : isInputType ? VALUE_MAP : typeName;
      method.addStatement( "final $T $N = ($T) args.get( $S )",
                           javaType,
                           name,
                           javaType.isPrimitive() ? javaType.box() : javaType,
                           argument.getName() );
      if ( isInputType )
      {
        if ( isListType )
        {
          final String prefix;
          if ( GraphQLTypeUtil.isNonNull( graphQLType ) )
          {
            prefix = "";
          }
          else
          {
            prefix = "null == $N ? null : ";
            args.add( name );
          }
          final boolean listMayContainNulls =
            !GraphQLTypeUtil.isNonNull( GraphQLTypeUtil.unwrapOne( GraphQLTypeUtil.unwrapNonNull( graphQLType ) ) );

          params.add( prefix + "$N.stream().map( $T::$N ).collect( $T.toList() )" );
          args.add( name );
          args.add( ( (ParameterizedTypeName) typeName ).typeArguments.get( 0 ) );
          args.add( listMayContainNulls ? "maybeFrom" : "from" );
          args.add( Collectors.class );
        }
        else
        {
          params.add( "$T.$N( $N )" );
          args.add( typeName );
          args.add( GraphQLTypeUtil.isNonNull( graphQLType ) ? "from" : "maybeFrom" );
          args.add( name );
        }
      }
      else
      {
        params.add( "$N" );
        args.add( name );
      }
    }
    method.addStatement( "return new $T(" + String.join( ", ", params ) + ")", args.toArray() );

    return method.build();
  }

  @Nonnull
  private MethodSpec buildArgsConstructor( @Nonnull final List<GraphQLArgument> arguments,
                                           @Nonnull final Map<GraphQLArgument, TypeName> argTypes )
  {
    final MethodSpec.Builder ctor = MethodSpec.constructorBuilder();
    ctor.addModifiers( Modifier.PRIVATE );
    for ( final GraphQLArgument argument : arguments )
    {
      final GraphQLInputType fieldType = argument.getType();
      final TypeName javaType = argTypes.get( argument );
      final ParameterSpec.Builder parameter = ParameterSpec.builder( javaType, argument.getName(), Modifier.FINAL );
      if ( !javaType.isPrimitive() )
      {
        parameter.addAnnotation( getNullabilityAnnotation( fieldType ) );
      }
      ctor.addParameter( parameter.build() );
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
  private MethodSpec buildArgsArgGetter( @Nonnull final GraphQLArgument argument,
                                         @Nonnull final Map<GraphQLArgument, TypeName> argTypes )
  {
    final String name = argument.getName();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( "get" + NamingUtil.uppercaseFirstCharacter( name ) );
    builder.addModifiers( Modifier.PUBLIC );
    final TypeName javaType = argTypes.get( argument );
    builder.returns( javaType );
    if ( !javaType.isPrimitive() )
    {
      builder.addAnnotation( getNullabilityAnnotation( argument.getType() ) );
    }

    final String description = argument.getDescription();
    if ( null != description )
    {
      builder.addJavadoc( asJavadoc( description ) );
    }
    builder.addStatement( "return $N", name );
    return builder.build();
  }

  @Nonnull
  private FieldSpec buildArgsArgField( @Nonnull final GraphQLArgument argument,
                                       @Nonnull final Map<GraphQLArgument, TypeName> argTypes )
  {
    final TypeName javaType = argTypes.get( argument );
    final FieldSpec.Builder builder =
      FieldSpec.builder( javaType, argument.getName(), Modifier.PRIVATE, Modifier.FINAL );
    if ( !javaType.isPrimitive() )
    {
      builder.addAnnotation( getNullabilityAnnotation( argument.getType() ) );
    }

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
      fieldTypes.put( field, JavaGenUtil.getJavaType( typeMap, field.getType() ) );
    }

    builder.addMethod( buildInputFrom( type, typeMap, fieldTypes ) );
    builder.addMethod( buildInputMaybeFrom( type, typeMap ) );
    builder.addMethod( buildInputConstructor( type, fieldTypes ) );
    for ( final GraphQLInputObjectField field : type.getFields() )
    {
      builder.addField( buildInputFieldField( field, fieldTypes ) );
      builder.addMethod( buildInputFieldGetter( field, fieldTypes ) );
    }
    builder.addMethod( buildInputEquals( self, type ) );
    JavaGenUtil.writeTopLevelType( context, builder );
  }

  @Nonnull
  private MethodSpec buildInputEquals( @Nonnull final ClassName self, @Nonnull final GraphQLInputObjectType type )
  {
    final MethodSpec.Builder method =
      MethodSpec.methodBuilder( "equals" ).
        addModifiers( Modifier.PUBLIC, Modifier.FINAL ).
        addAnnotation( Override.class ).
        addParameter( Object.class, "o", Modifier.FINAL ).
        returns( TypeName.BOOLEAN );

    final CodeBlock.Builder codeBlock = CodeBlock.builder();
    codeBlock.beginControlFlow( "if ( !( o instanceof $T ) )", self );
    codeBlock.addStatement( "return false" );
    codeBlock.endControlFlow();
    method.addCode( codeBlock.build() );

    method.addStatement( "final $T that = ($T) o", self, self );

    for ( final GraphQLInputObjectField field : type.getFields() )
    {
      final CodeBlock.Builder block = CodeBlock.builder();
      block.beginControlFlow( "if ( !$T.equals( $N, that.$N ) )",
                              Objects.class,
                              field.getName(),
                              field.getName() );
      block.addStatement( "return false" );
      block.endControlFlow();
      method.addCode( block.build() );

    }
    method.addStatement( "return true" );
    return method.build();
  }

  @Nonnull
  private MethodSpec buildInputMaybeFrom( @Nonnull final GraphQLInputObjectType type,
                                          @Nonnull final Map<GraphQLType, String> typeMap )
  {
    return MethodSpec.methodBuilder( "maybeFrom" )
      .addModifiers( Modifier.PUBLIC, Modifier.STATIC )
      .addAnnotation( JavaGenUtil.NULLABLE_CLASSNAME )
      .returns( ClassName.bestGuess( typeMap.get( type ) ) )
      .addParameter( ParameterSpec.builder( VALUE_MAP, "args", Modifier.FINAL )
                       .addAnnotation( JavaGenUtil.NULLABLE_CLASSNAME )
                       .build() )
      .addStatement( "return null == args ? null : from( args )" )
      .build();
  }

  @Nonnull
  private MethodSpec buildInputFrom( @Nonnull final GraphQLInputObjectType type,
                                     @Nonnull final Map<GraphQLType, String> typeMap,
                                     @Nonnull final Map<GraphQLInputObjectField, TypeName> fieldTypes )
  {
    final MethodSpec.Builder method = MethodSpec.methodBuilder( "from" );
    method.addModifiers( Modifier.PUBLIC, Modifier.STATIC );
    method.addAnnotation( JavaGenUtil.NONNULL_CLASSNAME );
    final ClassName self = ClassName.bestGuess( typeMap.get( type ) );
    method.returns( self );

    method.addParameter( ParameterSpec.builder( VALUE_MAP, "args", Modifier.FINAL )
                           .addAnnotation( JavaGenUtil.NONNULL_CLASSNAME )
                           .build() );

    boolean suppressedUnchecked = false;

    final List<String> params = new ArrayList<>();
    final List<Object> args = new ArrayList<>();
    args.add( self );
    for ( final GraphQLInputObjectField field : type.getFields() )
    {
      final String name = VAR_PREFIX + field.getName();
      final TypeName typeName = fieldTypes.get( field );
      final GraphQLType graphQLType = field.getType();

      final boolean isInputType = GraphQLTypeUtil.unwrapAll( graphQLType ) instanceof GraphQLInputObjectType;
      final boolean isListType = JavaGenUtil.isList( graphQLType );

      if ( ( isInputType || isListType ) && !suppressedUnchecked )
      {
        suppressedUnchecked = true;
        method.addAnnotation( AnnotationSpec.builder( SuppressWarnings.class )
                                .addMember( "value", "$S", "unchecked" )
                                .build() );
      }

      final TypeName javaType =
        isInputType && isListType ? JavaGenUtil.listOf( VALUE_MAP ) : isInputType ? VALUE_MAP : typeName;
      method.addStatement( "final $T $N = ($T) args.get( $S )",
                           javaType,
                           name,
                           javaType.isPrimitive() ? javaType.box() : javaType,
                           field.getName() );
      if ( isInputType )
      {
        if ( isListType )
        {
          final String prefix;
          if ( GraphQLTypeUtil.isNonNull( graphQLType ) )
          {
            prefix = "";
          }
          else
          {
            prefix = "null == $N ? null : ";
            args.add( name );
          }
          final boolean listMayContainNulls =
            !GraphQLTypeUtil.isNonNull( GraphQLTypeUtil.unwrapOne( GraphQLTypeUtil.unwrapNonNull( graphQLType ) ) );

          params.add( prefix + "$N.stream().map( $T::$N ).collect( $T.toList() )" );
          args.add( name );
          args.add( ( (ParameterizedTypeName) typeName ).typeArguments.get( 0 ) );
          args.add( listMayContainNulls ? "maybeFrom" : "from" );
          args.add( Collectors.class );
        }
        else
        {
          params.add( "$T.$N( $N )" );
          args.add( typeName );
          args.add( GraphQLTypeUtil.isNonNull( graphQLType ) ? "from" : "maybeFrom" );
          args.add( name );
        }
      }
      else
      {
        params.add( "$N" );
        args.add( name );
      }
    }
    method.addStatement( "return new $T(" + String.join( ", ", params ) + ")", args.toArray() );

    return method.build();
  }

  @Nonnull
  private MethodSpec buildInputConstructor( @Nonnull final GraphQLInputObjectType type,
                                            @Nonnull final Map<GraphQLInputObjectField, TypeName> fieldTypes )
  {
    final MethodSpec.Builder ctor = MethodSpec.constructorBuilder();
    ctor.addModifiers( Modifier.PRIVATE );
    for ( final GraphQLInputObjectField field : type.getFields() )
    {
      final GraphQLInputType fieldType = field.getType();
      final TypeName javaType = fieldTypes.get( field );
      final ParameterSpec.Builder parameter = ParameterSpec.builder( javaType, field.getName(), Modifier.FINAL );
      if ( !javaType.isPrimitive() )
      {
        parameter.addAnnotation( getNullabilityAnnotation( fieldType ) );
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
  private ClassName getNullabilityAnnotation( @Nonnull final GraphQLType type )
  {
    return type instanceof GraphQLNonNull ? JavaGenUtil.NONNULL_CLASSNAME : JavaGenUtil.NULLABLE_CLASSNAME;
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
      builder.addAnnotation( getNullabilityAnnotation( field.getType() ) );
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
      builder.addAnnotation( getNullabilityAnnotation( field.getType() ) );
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
