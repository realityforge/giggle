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
import com.squareup.javapoet.TypeVariableName;
import graphql.Scalars;
import graphql.schema.CoercingParseValueException;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLDirective;
import graphql.schema.GraphQLDirectiveContainer;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLFieldsContainer;
import graphql.schema.GraphQLInputObjectField;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLNamedType;
import graphql.schema.GraphQLType;
import graphql.schema.GraphQLTypeUtil;
import graphql.schema.GraphQLUnmodifiedType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
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
    final Map<GraphQLNamedType, String> inputTypeMap = buildTypeMapping( context );
    final List<GraphQLType> types = extractTypesToGenerate( context.getSchema(), inputTypeMap );
    final Map<GraphQLNamedType, String> generatedTypeMap = extractGeneratedDataTypes( context, types );
    final Map<GraphQLNamedType, String> fullTypeMap = new HashMap<>( inputTypeMap );
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
              container.getName() + NamingUtil.uppercaseFirstCharacter( definition.getName() );
            emitArgs( context, fullTypeMap, NamingUtil.uppercaseFirstCharacter( argsName ) + "Args", arguments );
          }
        }
      }
    }
    writeTypeMappingFile( context, generatedTypeMap );
  }

  private void emitArgs( @Nonnull final GeneratorContext context,
                         @Nonnull final Map<GraphQLNamedType, String> typeMap,
                         @Nonnull final String name,
                         @Nonnull final List<GraphQLArgument> arguments )
    throws IOException
  {
    final TypeSpec.Builder builder = TypeSpec.classBuilder( name );
    builder.addModifiers( Modifier.PUBLIC, Modifier.FINAL );

    final Map<GraphQLArgument, TypeName> argTypes = new HashMap<>();
    for ( final GraphQLArgument argument : arguments )
    {
      argTypes.put( argument, JavaGenUtil.getJavaType( typeMap, argument, argument.getType() ) );
    }

    builder.addMethod( buildArgsFrom( name, arguments, argTypes ) );
    buildArgsCoerceMethods( arguments, builder );
    builder.addMethod( buildArgsConstructor( arguments, argTypes ) );
    for ( final GraphQLArgument argument : arguments )
    {
      builder.addField( buildArgsArgField( argument, argTypes ) );
      builder.addMethod( buildArgsArgGetter( argument, argTypes ) );
    }

    JavaGenUtil.writeTopLevelType( context, builder );
  }

  private void buildArgsCoerceMethods( @Nonnull final List<GraphQLArgument> arguments,
                                       @Nonnull final TypeSpec.Builder builder )
  {
    boolean coerceTrapRequired = false;
    boolean coerceForID = false;
    boolean maybeCoerceForID = false;
    for ( final GraphQLArgument argument : arguments )
    {
      final GraphQLUnmodifiedType inputType = GraphQLTypeUtil.unwrapAll( argument.getType() );
      if ( isNumericIDType( argument, inputType ) )
      {
        coerceForID = true;
        coerceTrapRequired = true;
        final boolean isListType = JavaGenUtil.isList( argument.getType() );
        final boolean nonNull = GraphQLTypeUtil.isNonNull( argument.getType() );
        final boolean listMayContainNulls =
          isListType &&
          !GraphQLTypeUtil.isNonNull( GraphQLTypeUtil.unwrapOne( GraphQLTypeUtil.unwrapNonNull( argument.getType() ) ) );
        if ( listMayContainNulls || ( !isListType && !nonNull ) )
        {
          maybeCoerceForID = true;
        }
      }
      else if ( isCoerceTrapRequired( argument ) )
      {
        coerceTrapRequired = true;
      }
    }
    if ( coerceTrapRequired )
    {
      builder.addMethod( buildArgsCoerceTrap() );
      if ( coerceForID )
      {
        builder.addMethod( buildCoerceID( "argument" ) );
        if ( maybeCoerceForID )
        {
          builder.addMethod( buildMaybeCoerceID() );
        }
      }
    }
  }

  private boolean isCoerceTrapRequired( @Nonnull final GraphQLArgument type )
  {
    final GraphQLUnmodifiedType inputType = GraphQLTypeUtil.unwrapAll( type.getType() );
    return isNumericIDType( type, inputType ) ||
           ( inputType instanceof GraphQLInputObjectType &&
             ( (GraphQLInputObjectType) inputType ).getFields()
               .stream()
               .anyMatch( this::isCoerceTrapRequired ) );
  }

  private boolean isNumericIDType( @Nonnull final GraphQLDirectiveContainer container,
                                   @Nonnull final GraphQLNamedType inputType )
  {
    return Scalars.GraphQLID.getName().equals( inputType.getName() ) &&
           JavaGenUtil.hasNumericDirective( container );
  }

  private boolean isCoerceTrapRequired( @Nonnull final GraphQLInputObjectField type )
  {
    return isCoerceTrapRequired( type, new HashSet<>() );
  }

  private boolean isCoerceTrapRequired( @Nonnull final GraphQLInputObjectField type,
                                        @Nonnull final Set<String> typesProcessed )
  {
    final GraphQLUnmodifiedType inputType = GraphQLTypeUtil.unwrapAll( type.getType() );
    if ( isNumericIDType( type, inputType ) )
    {
      return true;
    }
    else
    {
      final String name = inputType.getName();
      if ( inputType instanceof GraphQLInputObjectType && !typesProcessed.contains( name ) )
      {
        typesProcessed.add( name );
        final List<GraphQLInputObjectField> fields = ( (GraphQLInputObjectType) inputType ).getFields();
        return fields.stream().anyMatch( f -> isCoerceTrapRequired( f, typesProcessed ) );
      }
      else
      {
        return false;
      }
    }
  }

  @Nonnull
  private MethodSpec buildArgsCoerceTrap()
  {
    final MethodSpec.Builder method = MethodSpec.methodBuilder( "coerceTrap" );
    method.addModifiers( Modifier.PRIVATE, Modifier.STATIC );
    method.addAnnotation( Nonnull.class );
    final TypeVariableName type = TypeVariableName.get( "T" );
    method.addTypeVariable( type );
    method.returns( type );
    method.addParameter( ParameterSpec.builder( DataFetchingEnvironment.class, "environment", Modifier.FINAL )
                           .addAnnotation( Nonnull.class )
                           .build() );
    method.addParameter( ParameterSpec.builder( ParameterizedTypeName.get( ClassName.get( Supplier.class ), type ),
                                                "supplier",
                                                Modifier.FINAL )
                           .addAnnotation( Nonnull.class )
                           .build() );
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "try" );
    block.addStatement( "return supplier.get()" );
    block.nextControlFlow( "catch ( final $T e )", CoercingParseValueException.class );
    block.addStatement(
      "throw new $T( e.getMessage(), e.getCause(), environment.getMergedField().getSingleField().getSourceLocation() )",
      CoercingParseValueException.class );
    block.endControlFlow();
    method.addCode( block.build() );
    return method.build();
  }

  @Nonnull
  private MethodSpec buildCoerceID( @Nonnull final String type )
  {
    final CodeBlock.Builder code = CodeBlock.builder();
    code.beginControlFlow( "try" );
    code.addStatement( "return $T.decode( value )", Integer.class );
    code.nextControlFlow( "catch ( final $T e )", NumberFormatException.class );
    code.addStatement(
      "throw new $T( \"Failed to parse $N '\" + name + \"' that was expected to be a numeric ID type. Actual value = '\" + value + \"'\" )",
      CoercingParseValueException.class,
      type );
    code.endControlFlow();

    return MethodSpec.methodBuilder( "coerceID" )
      .addModifiers( Modifier.PRIVATE, Modifier.STATIC )
      .addParameter( ParameterSpec.builder( String.class, "name", Modifier.FINAL )
                       .addAnnotation( Nonnull.class )
                       .build() )
      .addParameter( ParameterSpec.builder( String.class, "value", Modifier.FINAL )
                       .addAnnotation( Nonnull.class )
                       .build() )
      .addAnnotation( Nonnull.class )
      .returns( Integer.class )
      .addCode( code.build() )
      .build();
  }

  @Nonnull
  private MethodSpec buildMaybeCoerceID()
  {
    return MethodSpec.methodBuilder( "maybeCoerceID" )
      .addModifiers( Modifier.PRIVATE, Modifier.STATIC )
      .addParameter( ParameterSpec.builder( String.class, "name", Modifier.FINAL )
                       .addAnnotation( Nonnull.class )
                       .build() )
      .addParameter( ParameterSpec.builder( String.class, "value", Modifier.FINAL )
                       .addAnnotation( Nullable.class )
                       .build() )
      .addAnnotation( Nullable.class )
      .returns( Integer.class )
      .addStatement( "return null == value ? null : coerceID( name, value )" )
      .build();
  }

  @Nonnull
  private MethodSpec buildArgsFrom( @Nonnull final String className,
                                    @Nonnull final List<GraphQLArgument> arguments,
                                    @Nonnull final Map<GraphQLArgument, TypeName> argTypes )
  {
    final MethodSpec.Builder method = MethodSpec.methodBuilder( "from" );
    method.addModifiers( Modifier.PUBLIC, Modifier.STATIC );
    method.addAnnotation( Nonnull.class );
    final ClassName self = ClassName.bestGuess( className );
    method.returns( self );

    method.addParameter( ParameterSpec.builder( DataFetchingEnvironment.class, "environment", Modifier.FINAL )
                           .addAnnotation( Nonnull.class )
                           .build() );

    method.addStatement( "final $T args = environment.getArguments()", VALUE_MAP );

    boolean suppressedUnchecked = false;
    boolean coerceTrapRequired = false;

    final List<String> params = new ArrayList<>();
    final List<Object> args = new ArrayList<>();
    args.add( self );
    for ( final GraphQLArgument argument : arguments )
    {
      final String argName = argument.getName();
      final String name = GEN_PREFIX + argName;
      final TypeName typeName = argTypes.get( argument );
      final GraphQLType graphQLType = argument.getType();

      final GraphQLUnmodifiedType unwrappedType = GraphQLTypeUtil.unwrapAll( graphQLType );
      final boolean isInputType = unwrappedType instanceof GraphQLInputObjectType;
      final boolean coerceRequired = isCoerceTrapRequired( argument );
      final boolean isListType = JavaGenUtil.isList( graphQLType );
      final boolean nonNull = GraphQLTypeUtil.isNonNull( graphQLType );
      final boolean listMayContainNulls =
        isListType &&
        !GraphQLTypeUtil.isNonNull( GraphQLTypeUtil.unwrapOne( GraphQLTypeUtil.unwrapNonNull( graphQLType ) ) );

      if ( coerceRequired )
      {
        coerceTrapRequired = true;
      }

      if ( ( isInputType || isListType ) && !suppressedUnchecked )
      {
        suppressedUnchecked = true;
        method.addAnnotation( AnnotationSpec.builder( SuppressWarnings.class )
                                .addMember( "value", "$S", "unchecked" )
                                .build() );
      }

      final TypeName javaType =
        isInputType && isListType ? JavaGenUtil.listOf( VALUE_MAP ) :
        isInputType ? VALUE_MAP :
        isListType && coerceRequired ? JavaGenUtil.listOf( ClassName.get( String.class ) ) :
        coerceRequired ? ClassName.get( String.class ) :
        typeName;
      method.addStatement( "final $T $N = ($T) args.get( $S )",
                           javaType,
                           name,
                           javaType.isPrimitive() ? javaType.box() : javaType,
                           argName );
      if ( isInputType )
      {
        if ( isListType )
        {
          final String prefix;
          if ( nonNull )
          {
            prefix = "";
          }
          else
          {
            prefix = "null == $N ? null : ";
            args.add( name );
          }

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
          args.add( nonNull ? "from" : "maybeFrom" );
          args.add( name );
        }
      }
      else if ( coerceRequired && isListType )
      {
        final String prefix;
        if ( nonNull )
        {
          prefix = "";
        }
        else
        {
          prefix = "null == $N ? null : ";
          args.add( name );
        }
        params.add( prefix + "$N.stream().map( v -> $N( $S, v ) ).collect( $T.toList() )" );
        args.add( name );
        args.add( listMayContainNulls ? "maybeCoerceID" : "coerceID" );
        args.add( argName );
        args.add( Collectors.class );
      }
      else if ( coerceRequired )
      {
        // The only non-input type that requires coercing is an ID that coerced to an integer
        params.add( "$N( $S, $N )" );
        args.add( nonNull ? "coerceID" : "maybeCoerceID" );
        args.add( argName );
        args.add( name );
      }
      else
      {
        // The only non-input type that requires coercing is an ID that coerced to an integer
        params.add( "$N" );
        args.add( name );
      }
    }
    if ( coerceTrapRequired )
    {
      method.addStatement( "return coerceTrap( environment, () -> new $T(" + String.join( ", ", params ) + ") )",
                           args.toArray() );
    }
    else
    {
      method.addStatement( "return new $T(" + String.join( ", ", params ) + ")", args.toArray() );
    }

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
        parameter.addAnnotation( GraphQLTypeUtil.isNonNull( fieldType ) ? Nonnull.class : Nullable.class );
      }
      ctor.addParameter( parameter.build() );
      if ( GraphQLTypeUtil.isNonNull( fieldType ) && !javaType.isPrimitive() )
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
      builder.addAnnotation( GraphQLTypeUtil.isNonNull( argument.getType() ) ? Nonnull.class : Nullable.class );
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
      builder.addAnnotation( GraphQLTypeUtil.isNonNull( argument.getType() ) ? Nonnull.class : Nullable.class );
    }

    final String description = argument.getDescription();
    if ( null != description )
    {
      builder.addJavadoc( asJavadoc( description ) );
    }
    return builder.build();
  }

  private void emitInput( @Nonnull final GeneratorContext context,
                          @Nonnull final Map<GraphQLNamedType, String> typeMap,
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
      fieldTypes.put( field, JavaGenUtil.getJavaType( typeMap, field, field.getType() ) );
    }

    builder.addMethod( buildInputFrom( type, typeMap, fieldTypes ) );
    builder.addMethod( buildInputMaybeFrom( type, typeMap ) );
    buildInputCoerceMethods( type.getFields(), builder );
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

  private void buildInputCoerceMethods( @Nonnull final List<GraphQLInputObjectField> fields,
                                        @Nonnull final TypeSpec.Builder builder )
  {
    boolean coerceForID = false;
    boolean maybeCoerceForID = false;
    for ( final GraphQLInputObjectField field : fields )
    {
      final GraphQLUnmodifiedType inputType = GraphQLTypeUtil.unwrapAll( field.getType() );
      if ( isNumericIDType( field, inputType ) )
      {
        coerceForID = true;
        final boolean isListType = JavaGenUtil.isList( field.getType() );
        final boolean nonNull = GraphQLTypeUtil.isNonNull( field.getType() );
        final boolean listMayContainNulls =
          isListType &&
          !GraphQLTypeUtil.isNonNull( GraphQLTypeUtil.unwrapOne( GraphQLTypeUtil.unwrapNonNull( inputType ) ) );
        if ( listMayContainNulls || ( !isListType && !nonNull ) )
        {
          maybeCoerceForID = true;
        }
      }
    }
    if ( coerceForID )
    {
      builder.addMethod( buildCoerceID( "input field" ) );
      if ( maybeCoerceForID )
      {
        builder.addMethod( buildMaybeCoerceID() );
      }
    }
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
  private MethodSpec buildInputMaybeFrom( @Nonnull final GraphQLInputObjectType type,
                                          @Nonnull final Map<GraphQLNamedType, String> typeMap )
  {
    return MethodSpec.methodBuilder( "maybeFrom" )
      .addModifiers( Modifier.PUBLIC, Modifier.STATIC )
      .addAnnotation( Nullable.class )
      .returns( ClassName.bestGuess( typeMap.get( type ) ) )
      .addParameter( ParameterSpec.builder( VALUE_MAP, "args", Modifier.FINAL )
                       .addAnnotation( Nullable.class )
                       .build() )
      .addStatement( "return null == args ? null : from( args )" )
      .build();
  }

  @Nonnull
  private MethodSpec buildInputFrom( @Nonnull final GraphQLInputObjectType type,
                                     @Nonnull final Map<GraphQLNamedType, String> typeMap,
                                     @Nonnull final Map<GraphQLInputObjectField, TypeName> fieldTypes )
  {
    final MethodSpec.Builder method = MethodSpec.methodBuilder( "from" );
    method.addModifiers( Modifier.PUBLIC, Modifier.STATIC );
    method.addAnnotation( Nonnull.class );
    final ClassName self = ClassName.bestGuess( typeMap.get( type ) );
    method.returns( self );

    method.addParameter( ParameterSpec.builder( VALUE_MAP, "args", Modifier.FINAL )
                           .addAnnotation( Nonnull.class )
                           .build() );

    boolean suppressedUnchecked = false;

    final List<String> params = new ArrayList<>();
    final List<Object> args = new ArrayList<>();
    args.add( self );
    for ( final GraphQLInputObjectField field : type.getFields() )
    {
      final String fieldName = field.getName();
      final String name = GEN_PREFIX + fieldName;
      final TypeName typeName = fieldTypes.get( field );
      final GraphQLType graphQLType = field.getType();

      final GraphQLUnmodifiedType unwrappedType = GraphQLTypeUtil.unwrapAll( graphQLType );
      final boolean isInputType = unwrappedType instanceof GraphQLInputObjectType;
      final boolean coerceRequired = isNumericIDType( field, unwrappedType );
      final boolean isListType = JavaGenUtil.isList( graphQLType );
      final boolean nonNull = GraphQLTypeUtil.isNonNull( graphQLType );
      final boolean listMayContainNulls =
        isListType &&
        !GraphQLTypeUtil.isNonNull( GraphQLTypeUtil.unwrapOne( GraphQLTypeUtil.unwrapNonNull( graphQLType ) ) );

      if ( ( isInputType || isListType ) && !suppressedUnchecked )
      {
        suppressedUnchecked = true;
        method.addAnnotation( AnnotationSpec.builder( SuppressWarnings.class )
                                .addMember( "value", "$S", "unchecked" )
                                .build() );
      }

      final TypeName javaType =
        isInputType && isListType ? JavaGenUtil.listOf( VALUE_MAP ) :
        isInputType ? VALUE_MAP :
        isListType && coerceRequired ? JavaGenUtil.listOf( ClassName.get( String.class ) ) :
        coerceRequired ? ClassName.get( String.class ) :
        typeName;
      method.addStatement( "final $T $N = ($T) args.get( $S )",
                           javaType,
                           name,
                           javaType.isPrimitive() ? javaType.box() : javaType,
                           fieldName );
      if ( isInputType )
      {
        if ( isListType )
        {
          final String prefix;
          if ( nonNull )
          {
            prefix = "";
          }
          else
          {
            prefix = "null == $N ? null : ";
            args.add( name );
          }

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
          args.add( nonNull ? "from" : "maybeFrom" );
          args.add( name );
        }
      }
      else if ( coerceRequired && isListType )
      {
        final String prefix;
        if ( nonNull )
        {
          prefix = "";
        }
        else
        {
          prefix = "null == $N ? null : ";
          args.add( name );
        }
        params.add( prefix + "$N.stream().map( v -> $N( $S, v ) ).collect( $T.toList() )" );
        args.add( name );
        args.add( listMayContainNulls ? "maybeCoerceID" : "coerceID" );
        args.add( fieldName );
        args.add( Collectors.class );
      }
      else if ( coerceRequired )
      {
        // The only non-input type that requires coercing is an ID that coerced to an integer
        params.add( "$N( $S, $N )" );
        args.add( nonNull ? "coerceID" : "maybeCoerceID" );
        args.add( fieldName );
        args.add( name );
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
}
