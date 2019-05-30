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
  @Nonnull
  private final List<Path> _enumMappingFiles = new ArrayList<>();
  @Nonnull
  private final List<Path> _typeMappingFiles = new ArrayList<>();
  @Nonnull
  private final List<Path> _fragmentMappingFiles = new ArrayList<>();
  @Nonnull
  private final List<Path> _operationMappingFiles = new ArrayList<>();

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

  void addSchemaFile( @Nonnull final Path file )
  {
    _schemaFiles.add( Objects.requireNonNull( file ) );
  }

  @Nonnull
  List<Path> getSchemaFiles()
  {
    return Collections.unmodifiableList( _schemaFiles );
  }

  void addDocumentFile( @Nonnull final Path file )
  {
    _documentFiles.add( Objects.requireNonNull( file ) );
  }

  @Nonnull
  List<Path> getDocumentFiles()
  {
    return Collections.unmodifiableList( _documentFiles );
  }

  void addEnumMappingFile( @Nonnull final Path file )
  {
    _enumMappingFiles.add( Objects.requireNonNull( file ) );
  }

  @Nonnull
  List<Path> getEnumMappingFiles()
  {
    return Collections.unmodifiableList( _enumMappingFiles );
  }

  void addTypeMappingFile( @Nonnull final Path file )
  {
    _typeMappingFiles.add( Objects.requireNonNull( file ) );
  }

  @Nonnull
  List<Path> getTypeMappingFiles()
  {
    return Collections.unmodifiableList( _typeMappingFiles );
  }

  void addFragmentMappingFile( @Nonnull final Path file )
  {
    _fragmentMappingFiles.add( Objects.requireNonNull( file ) );
  }

  @Nonnull
  List<Path> getFragmentMappingFiles()
  {
    return Collections.unmodifiableList( _fragmentMappingFiles );
  }

  void addOperationMappingFile( @Nonnull final Path file )
  {
    _operationMappingFiles.add( Objects.requireNonNull( file ) );
  }

  @Nonnull
  List<Path> getOperationMappingFiles()
  {
    return Collections.unmodifiableList( _operationMappingFiles );
  }
}
