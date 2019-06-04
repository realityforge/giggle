package org.realityforge.giggle.integration;

import java.nio.file.Path;
import java.util.Objects;
import javax.annotation.Nonnull;

public final class CompileResults
{
  @Nonnull
  private final String _packageName;
  @Nonnull
  private final Path _javaDirectory;
  @Nonnull
  private final Path _classesDirectory;

  public CompileResults( @Nonnull final String packageName,
                  @Nonnull final Path javaDirectory,
                  @Nonnull final Path classesDirectory )
  {
    _packageName = Objects.requireNonNull( packageName );
    _javaDirectory = Objects.requireNonNull( javaDirectory );
    _classesDirectory = Objects.requireNonNull( classesDirectory );
  }

  @Nonnull
  public String getPackageName()
  {
    return _packageName;
  }

  @Nonnull
  public Path getJavaDirectory()
  {
    return _javaDirectory;
  }

  @Nonnull
  public Path getClassesDirectory()
  {
    return _classesDirectory;
  }
}
