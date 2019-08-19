package org.realityforge.giggle.generator;

import graphql.language.Document;
import graphql.schema.GraphQLSchema;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class GeneratorContext
{
  @Nonnull
  private final GeneratorEntry _generator;
  @Nonnull
  private final GlobalGeneratorContext _context;

  public GeneratorContext( @Nonnull final GeneratorEntry generator, @Nonnull final GlobalGeneratorContext context )
  {
    _generator = Objects.requireNonNull( generator );
    _context = Objects.requireNonNull( context );
  }

  @Nonnull
  public GraphQLSchema getSchema()
  {
    return _context.getSchema();
  }

  @Nonnull
  public Document getDocument()
  {
    return _context.getDocument();
  }

  @Nonnull
  public Map<String, String> getTypeMapping()
  {
    return _context.getTypeMapping();
  }

  @Nonnull
  public Map<String, String> getFragmentMapping()
  {
    return _context.getFragmentMapping();
  }

  @Nonnull
  public Path getOutputDirectory()
  {
    return _context.getOutputDirectory();
  }

  @Nonnull
  public String getPackageName()
  {
    return _context.getPackageName();
  }
}
