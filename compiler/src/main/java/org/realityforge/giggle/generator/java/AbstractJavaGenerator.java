package org.realityforge.giggle.generator.java;

import graphql.schema.GraphQLType;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.realityforge.giggle.generator.Generator;
import org.realityforge.giggle.generator.GeneratorContext;

public abstract class AbstractJavaGenerator
  implements Generator
{
  /**
   * Prefix for synthesized variables. Used to guarantee uniqueness in the presence of user supplied variables.
   */
  protected static final String VAR_PREFIX = "$giggle$_";

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
    final Path typeMappingFile = context.getOutputDirectory()
      .resolve( context.getPackageName().replaceAll( "\\.", File.separator ) )
      .resolve( "types.mapping" );
    final String typeMappingContent =
      typeMap.keySet()
        .stream()
        .map( type -> type.getName() + "=" + typeMap.get( type ) )
        .sorted()
        .collect( Collectors.joining( "\n" ) ) + "\n";
    final Path dir = typeMappingFile.getParent();
    if ( !Files.exists( dir ) )
    {
      Files.createDirectories( dir );
    }
    Files.write( typeMappingFile, typeMappingContent.getBytes( StandardCharsets.US_ASCII ) );
  }

  @Nonnull
  protected final Map<GraphQLType, String> buildTypeMapping( @Nonnull final GeneratorContext context )
  {
    final Map<GraphQLType, String> typeMap = new HashMap<>();
    context.getTypeMapping().forEach( ( key, value ) -> typeMap.put( context.getSchema().getType( key ), value ) );
    return typeMap;
  }
}
