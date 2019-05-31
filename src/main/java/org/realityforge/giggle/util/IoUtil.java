package org.realityforge.giggle.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import javax.annotation.Nonnull;

public final class IoUtil
{
  private IoUtil()
  {
  }

  /**
   * Recursively delete directory if it exists.
   *
   * @param directory the directory to delete.
   */
  @SuppressWarnings( "ResultOfMethodCallIgnored" )
  public static void deleteDirIfExists( @Nonnull final Path directory )
    throws IOException
  {
    if ( directory.toFile().exists() )
    {
      Files.walk( directory ).sorted( Comparator.reverseOrder() ).map( Path::toFile ).forEach( File::delete );
    }
  }
}
