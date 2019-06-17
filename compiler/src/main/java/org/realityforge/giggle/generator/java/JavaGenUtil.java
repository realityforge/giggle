package org.realityforge.giggle.generator.java;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import graphql.Scalars;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLDirectiveContainer;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLInputObjectField;
import graphql.schema.GraphQLType;
import graphql.schema.GraphQLTypeUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import org.realityforge.giggle.generator.GeneratorContext;

public final class JavaGenUtil
{
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
  public static TypeName getJavaType( @Nonnull final Map<GraphQLType, String> typeMap,
                                      @Nonnull final GraphQLDirectiveContainer container )
  {
    final GraphQLType valueType =
      getValueType( container );
    final GraphQLType type = GraphQLTypeUtil.unwrapAll( valueType );
    final String typeName = typeMap.get( type );
    if ( null != typeName )
    {
      return wrap( isList( valueType ), ClassName.bestGuess( typeName ) );
    }
    else
    {
      return getScalarJavaType( container, isList( valueType ), GraphQLTypeUtil.isNonNull( valueType ), type );
    }
  }

  @Nonnull
  private static GraphQLType getValueType( @Nonnull final GraphQLDirectiveContainer type )
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
                                             @Nonnull final GraphQLType type )
  {
    if ( Scalars.GraphQLInt.equals( type ) )
    {
      return wrapPrimitive( isList, isNonnull, TypeName.INT );
    }
    else if ( Scalars.GraphQLBoolean.equals( type ) )
    {
      return wrapPrimitive( isList, isNonnull, TypeName.BOOLEAN );
    }
    else if ( Scalars.GraphQLByte.equals( type ) )
    {
      return wrapPrimitive( isList, isNonnull, TypeName.BYTE );
    }
    else if ( Scalars.GraphQLShort.equals( type ) )
    {
      return wrapPrimitive( isList, isNonnull, TypeName.SHORT );
    }
    else if ( Scalars.GraphQLLong.equals( type ) )
    {
      return wrapPrimitive( isList, isNonnull, TypeName.LONG );
    }
    else if ( Scalars.GraphQLChar.equals( type ) )
    {
      return wrapPrimitive( isList, isNonnull, TypeName.CHAR );
    }
    else if ( Scalars.GraphQLFloat.equals( type ) )
    {
      return wrapPrimitive( isList, isNonnull, TypeName.FLOAT );
    }
    else if ( Scalars.GraphQLID.equals( type ) )
    {
      if ( hasNumericDirective( container ) )
      {
        return wrapPrimitive( isList, isNonnull, TypeName.INT );
      }
      else
      {
        return wrap( isList, ClassName.get( String.class ) );
      }
    }
    else if ( Scalars.GraphQLString.equals( type ) )
    {
      return wrap( isList, ClassName.get( String.class ) );
    }
    else if ( Scalars.GraphQLBigDecimal.equals( type ) )
    {
      return wrap( isList, ClassName.get( BigDecimal.class ) );
    }
    else if ( Scalars.GraphQLBigInteger.equals( type ) )
    {
      return wrap( isList, ClassName.get( BigInteger.class ) );
    }
    else if ( type.getName().equals( "Date" ) || type.getName().equals( "DateTime" ) )
    {
      //TODO: Support other scalars through some sort of mapping configuration
      return wrap( isList, ClassName.get( Date.class ) );
    }
    else
    {
      throw new IllegalStateException( "Unknown type " + type.getName() );
    }
  }

  @Nonnull
  private static TypeName wrap( final boolean isList, @Nonnull final TypeName type )
  {
    return isList ? listOf( type ) : type;
  }

  @Nonnull
  private static TypeName wrapPrimitive( final boolean isList, final boolean isNonnull, @Nonnull final TypeName type )
  {
    return isList ? listOf( type.box() ) : isNonnull ? type : type.box();
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
