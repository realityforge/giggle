package org.realityforge.giggle;

import java.io.Console;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class Environment
{
  @Nullable
  private final Console _console;
  @Nonnull
  private final Path _currentDirectory;
  @Nonnull
  private final Logger _logger;
  @Nonnull
  private final List<Path> _schemaFiles = new ArrayList<>();

  Environment( @Nullable final Console console, @Nonnull final Path currentDirectory, @Nonnull final Logger logger )
  {
    _console = console;
    _currentDirectory = Objects.requireNonNull( currentDirectory );
    _logger = Objects.requireNonNull( logger );
  }

  @Nullable
  Console console()
  {
    return _console;
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
}
