package org.realityforge.giggle;

import gir.Gir;
import gir.Task;
import gir.io.FileUtil;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import static org.testng.Assert.*;

@SuppressWarnings( "SameParameterValue" )
public abstract class AbstractTest
{
  protected final void inIsolatedDirectory( @Nonnull final Task task )
    throws Exception
  {
    Gir.go( () -> FileUtil.inTempDir( task ) );
  }

  @Nonnull
  protected final Path writeContent( @Nonnull final String path, @Nonnull final String content )
    throws IOException
  {
    FileUtil.write( path, content );
    return FileUtil.getCurrentDirectory().resolve( path );
  }

  final void assertOutputContains( @Nonnull final String output, @Nonnull final String text )
  {
    assertTrue( output.contains( text ),
                "Expected output\n---\n" + output + "\n---\nto contain text\n---\n" + text + "\n---\n" );
  }

  final Environment newEnvironment()
  {
    return newEnvironment( new TestHandler() );
  }

  final Environment newEnvironment( @Nonnull final TestHandler handler )
  {
    return new Environment( FileUtil.getCurrentDirectory(), createLogger( handler ) );
  }

  @Nonnull
  private Logger createLogger( @Nonnull final TestHandler handler )
  {
    final Logger logger = Logger.getAnonymousLogger();
    logger.setUseParentHandlers( false );
    logger.addHandler( handler );
    return logger;
  }
}
