package org.realityforge.giggle;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
  private final List<Path> _typeMappingFiles = new ArrayList<>();
  @Nonnull
  private final List<Path> _fragmentMappingFiles = new ArrayList<>();
  @Nonnull
  private final Map<String, String> _defines = new HashMap<>();
  @Nullable
  private Path _outputDirectory;
  @Nullable
  private String _packageName;
  @Nonnull
  private final List<String> _generators = new ArrayList<>();

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

  boolean hasOutputDirectory()
  {
    return null != _outputDirectory;
  }

  @Nonnull
  Path getOutputDirectory()
  {
    return Objects.requireNonNull( _outputDirectory );
  }

  void setOutputDirectory( @Nonnull final Path outputDirectory )
  {
    _outputDirectory = Objects.requireNonNull( outputDirectory );
  }

  @Nonnull
  Map<String, String> getDefines()
  {
    return Collections.unmodifiableMap( _defines );
  }

  void addDefine( @Nonnull final String key, @Nonnull final String value )
  {
    _defines.put( key, value );
  }

  boolean hasPackageName()
  {
    return null != _packageName;
  }

  @Nonnull
  String getPackageName()
  {
    return Objects.requireNonNull( _packageName );
  }

  void setPackageName( @Nonnull final String packageName )
  {
    _packageName = Objects.requireNonNull( packageName );
  }

  void addGenerator( @Nonnull final String generator )
  {
    _generators.add( Objects.requireNonNull( generator ) );
  }

  @Nonnull
  List<String> getGenerators()
  {
    return Collections.unmodifiableList( _generators );
  }
}
