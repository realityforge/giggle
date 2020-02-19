package org.realityforge.giggle.generator.java;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import graphql.Scalars;
import graphql.language.ListType;
import graphql.language.NonNullType;
import graphql.language.Type;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLDirectiveContainer;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLInputObjectField;
import graphql.schema.GraphQLNamedType;
import graphql.schema.GraphQLType;
import graphql.schema.GraphQLTypeUtil;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import org.realityforge.giggle.generator.GeneratorContext;

public final class JavaGenUtil
{
  @Nonnull
  private static final ScalarTypeRegistry c_scalarTypeRegistry = ScalarTypeRegistry.createDefault();

  private JavaGenUtil()
  {
  }

  public static void writeTopLevelType( @Nonnull final GeneratorContext context,
                                        @Nonnull final TypeSpec.Builder builder )
    throws IOException
  {
    writeGeneratedAnnotation( builder );
    JavaFile.builder( context.getPackageName(), builder.build() ).
      skipJavaLangImports( true ).
      build().
      writeTo( context.getOutputDirectory() );
  }

  private static void writeGeneratedAnnotation( @Nonnull final TypeSpec.Builder builder )
  {
    Class<?> generated;
    try
    {
      generated = Class.forName( "javax.annotation.processing.Generated" );
    }
    catch ( final ClassNotFoundException ignored )
    {
      try
      {
        generated = Class.forName( "javax.annotation.Generated" );
      }
      catch ( final ClassNotFoundException ignored2 )
      {
        //Generate no annotation
        return;
      }
    }
    builder.addAnnotation( AnnotationSpec.builder( ClassName.get( generated ) )
                             .addMember( "value", "$S", "org.realityforge.giggle.Main" )
                             .build() );
  }

  @Nonnull
  public static TypeName getJavaType( @Nonnull final Map<GraphQLNamedType, String> typeMap,
                                      @Nonnull final Type<?> varType )
  {
    Type<?> type = varType;
    boolean isNonNull = false;
    boolean isList = false;
    if ( type instanceof NonNullType )
    {
      isNonNull = true;
      type = ( (NonNullType) type ).getType();
    }
    if ( type instanceof ListType )
    {
      isList = true;
      type = ( (ListType) type ).getType();
      if ( type instanceof NonNullType )
      {
        type = ( (NonNullType) type ).getType();
      }
    }

    final graphql.language.TypeName typeName = (graphql.language.TypeName) type;

    final String name =
      typeMap.entrySet()
        .stream()
        .filter( e -> e.getKey().getName().equals( typeName.getName() ) )
        .findAny()
        .map( Map.Entry::getValue )
        .orElse( null );
    if ( null != name )
    {
      return wrap( isList, isNonNull, ClassName.bestGuess( name ) );
    }
    else
    {
      return getScalarJavaType( isList, isNonNull, typeName.getName() );
    }
  }

  @Nonnull
  public static TypeName getJavaType( @Nonnull final Map<GraphQLNamedType, String> typeMap,
                                      @Nonnull final GraphQLDirectiveContainer container,
                                      @Nonnull final GraphQLType specifiedType )
  {
    final GraphQLType valueType = getValueType( specifiedType );
    final GraphQLNamedType type = GraphQLTypeUtil.unwrapAll( valueType );
    final String typeName = typeMap.get( type );
    if ( null != typeName )
    {
      return wrap( isList( valueType ), GraphQLTypeUtil.isNonNull( valueType ), ClassName.bestGuess( typeName ) );
    }
    else
    {
      return getScalarJavaType( container, isList( valueType ), GraphQLTypeUtil.isNonNull( valueType ), type );
    }
  }

  @Nonnull
  private static GraphQLType getValueType( @Nonnull final GraphQLType type )
  {
    return type instanceof GraphQLInputObjectField ? ( (GraphQLInputObjectField) type ).getType() :
           type instanceof GraphQLArgument ? ( (GraphQLArgument) type ).getType() :
           type instanceof GraphQLFieldDefinition ? ( (GraphQLFieldDefinition) type ).getType() :
           type;
  }

  @Nonnull
  private static TypeName getScalarJavaType( @Nonnull final GraphQLDirectiveContainer container,
                                             final boolean isList,
                                             final boolean isNonnull,
                                             @Nonnull final GraphQLNamedType type )
  {

    if ( Scalars.GraphQLID.equals( type ) && hasNumericDirective( container ) )
    {
      return wrap( isList, isNonnull, TypeName.INT );
    }
    else
    {
      return getScalarJavaType( isList, isNonnull, type );
    }
  }

  @Nonnull
  private static TypeName getScalarJavaType( final boolean isList,
                                             final boolean isNonnull,
                                             @Nonnull final GraphQLNamedType type )
  {
    return getScalarJavaType( isList, isNonnull, type.getName() );
  }

  @Nonnull
  private static TypeName getScalarJavaType( final boolean isList,
                                             final boolean isNonnull,
                                             @Nonnull final String type )
  {
    return wrap( isList, isNonnull, c_scalarTypeRegistry.getType( type ) );
  }

  @Nonnull
  private static TypeName wrap( final boolean isList, final boolean isNonnull, @Nonnull final TypeName type )
  {
    final boolean primitive = type.isPrimitive();
    return isList ?
           listOf( primitive ? type.box() : type ) :
           primitive && !isNonnull ? type.box() : type;
  }

  @Nonnull
  public static TypeName listOf( @Nonnull final TypeName typeName )
  {
    return ParameterizedTypeName.get( ClassName.get( List.class ), typeName );
  }

  public static boolean isList( @Nonnull final GraphQLType type )
  {
    return GraphQLTypeUtil.isList( type ) ||
           ( GraphQLTypeUtil.isWrapped( type ) && isList( GraphQLTypeUtil.unwrapOne( type ) ) );
  }

  public static boolean hasNumericDirective( @Nonnull final GraphQLDirectiveContainer type )
  {
    return null != type.getDirective( "numeric" );
  }
}
