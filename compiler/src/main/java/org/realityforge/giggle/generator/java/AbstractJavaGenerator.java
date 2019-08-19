package org.realityforge.giggle.generator.java;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeSpec;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLEnumValueDefinition;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLType;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import org.realityforge.giggle.generator.Generator;
import org.realityforge.giggle.generator.GeneratorContext;

public abstract class AbstractJavaGenerator
  implements Generator
{
  /**
   * Prefix for synthesized variables. Used to guarantee uniqueness in the presence of user supplied variables.
   */
  protected static final String GEN_PREFIX = "$giggle$_";

  /**
   * Ensure that the supplied text is cleaned for insertion into javadoc.
   *
   * @param text the text.
   * @return the cleaned text.
   */
  @Nonnull
  protected final String asJavadoc( @Nonnull final String text )
  {
    return text.trim() + "\n";
  }

  /**
   * Return true if type does not look like types derived from the
   * {@link graphql.introspection.Introspection#__Schema introspection schema}
   *
   * @param type the type.
   * @return true if it is not an introspection type.
   */
  protected final boolean isNotIntrospectionType( @Nonnull final GraphQLType type )
  {
    return !type.getName().startsWith( "__" );
  }

  protected final void writeTypeMappingFile( @Nonnull final GeneratorContext context,
                                             @Nonnull final Map<GraphQLType, String> typeMap )
    throws IOException
  {
    final String typeMappingContent =
      typeMap.keySet()
        .stream()
        .map( type -> type.getName() + "=" + typeMap.get( type ) )
        .sorted()
        .collect( Collectors.joining( "\n" ) ) + "\n";
    writeFile( getPackageOutputDirectory( context ).resolve( "types.mapping" ), typeMappingContent );
  }

  @Nonnull
  protected final Path getPackageOutputDirectory( @Nonnull final GeneratorContext context )
  {
    return context.getOutputDirectory().resolve( context.getPackageName().replaceAll( "\\.", File.separator ) );
  }

  protected final void writeFile( @Nonnull final Path file, @Nonnull final String content )
    throws IOException
  {
    writeFile( file, content.getBytes( StandardCharsets.US_ASCII ) );
  }

  protected final void writeFile( @Nonnull final Path file, @Nonnull final byte[] bytes )
    throws IOException
  {
    final Path dir = file.getParent();
    if ( !Files.exists( dir ) )
    {
      Files.createDirectories( dir );
    }
    Files.write( file, bytes );
  }

  /**
   * Return a list of types that can be generated and have not been supplied by an external tool.
   * The method removes introspection types and any existing types and returns the remaining types.
   *
   * @param schema   the schema to process.
   * @param existing existing types.
   * @return the list of types
   */
  @Nonnull
  protected final List<GraphQLType> extractTypesToGenerate( @Nonnull final GraphQLSchema schema,
                                                            @Nonnull final Map<GraphQLType, String> existing )
  {
    return schema.getAllTypesAsList()
      .stream()
      .filter( this::isNotIntrospectionType )
      .filter( t -> !existing.containsKey( t ) )
      .collect( Collectors.toList() );
  }

  @Nonnull
  protected final Map<GraphQLType, String> extractGeneratedDataTypes( @Nonnull final GeneratorContext context,
                                                                      @Nonnull final List<GraphQLType> types )
  {
    final Map<GraphQLType, String> generatedTypeMap = new HashMap<>();
    for ( final GraphQLType type : types )
    {
      if ( type instanceof GraphQLEnumType || type instanceof GraphQLInputObjectType )
      {
        generatedTypeMap.put( type, context.getPackageName() + "." + type.getName() );
      }
    }
    return generatedTypeMap;
  }

  @Nonnull
  protected final Map<GraphQLType, String> buildTypeMapping( @Nonnull final GeneratorContext context )
  {
    final Map<GraphQLType, String> typeMap = new HashMap<>();
    context.getTypeMapping().forEach( ( key, value ) -> typeMap.put( context.getSchema().getType( key ), value ) );
    return typeMap;
  }

  protected final void emitEnum( @Nonnull final GeneratorContext context, @Nonnull final GraphQLEnumType type )
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
