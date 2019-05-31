package org.realityforge.giggle.generator;

import graphql.language.Document;
import graphql.schema.GraphQLSchema;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

public final class GeneratorContext
{
  @Nonnull
  private final GraphQLSchema _schema;
  @Nonnull
  private final Document _document;
  @Nonnull
  private final Map<String, String> _typeMapping;
  @Nonnull
  private final Map<String, String> _fragmentMapping;
  @Nonnull
  private final Path _outputDirectory;
  @Nonnull
  private final String _packageName;

  public GeneratorContext( @Nonnull final GraphQLSchema schema,
                           @Nonnull final Document document,
                           @Nonnull final Map<String, String> typeMapping,
                           @Nonnull final Map<String, String> fragmentMapping,
                           @Nonnull final Path outputDirectory,
                           @Nonnull final String packageName )
  {
    _schema = Objects.requireNonNull( schema );
    _document = Objects.requireNonNull( document );
    _typeMapping = Objects.requireNonNull( typeMapping );
    _fragmentMapping = Objects.requireNonNull( fragmentMapping );
    _outputDirectory = Objects.requireNonNull( outputDirectory );
    _packageName = Objects.requireNonNull( packageName );
  }

  @Nonnull
  public GraphQLSchema getSchema()
  {
    return _schema;
  }

  @Nonnull
  public Document getDocument()
  {
    return _document;
  }

  @Nonnull
  public Map<String, String> getTypeMapping()
  {
    return _typeMapping;
  }

  @Nonnull
  public Map<String, String> getFragmentMapping()
  {
    return _fragmentMapping;
  }

  @Nonnull
  public Path getOutputDirectory()
  {
    return _outputDirectory;
  }

  @Nonnull
  public String getPackageName()
  {
    return _packageName;
  }
}
