package org.realityforge.giggle;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import javax.annotation.Nonnull;

final class Environment
{
  @Nonnull
  private final Path _currentDirectory;
  @Nonnull
  private final Logger _logger;
  @Nonnull
  private final List<Path> _schemaFiles = new ArrayList<>();
  @Nonnull
  private final List<Path> _documentFiles = new ArrayList<>();

  Environment( @Nonnull final Path currentDirectory, @Nonnull final Logger logger )
  {
    _currentDirectory = Objects.requireNonNull( currentDirectory );
    _logger = Objects.requireNonNull( logger );
  }

  @Nonnull
  Path currentDirectory()
  {
    return _currentDirectory;
  }

  @Nonnull
  Logger logger()
  {
    return _logger;
  }

  void addSchemaFile( @Nonnull final Path schemaFile )
  {
    _schemaFiles.add( Objects.requireNonNull( schemaFile ) );
  }

  @Nonnull
  List<Path> getSchemaFiles()
  {
    return Collections.unmodifiableList( _schemaFiles );
  }

  void addDocumentFile( @Nonnull final Path documentFile )
  {
    _documentFiles.add( Objects.requireNonNull( documentFile ) );
  }

  @Nonnull
  List<Path> getDocumentFiles()
  {
    return Collections.unmodifiableList( _documentFiles );
  }
}
