package org.realityforge.giggle;

import gir.Gir;
import gir.Task;
import gir.io.FileUtil;
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

  final void assertOutputContains( @Nonnull final String output, @Nonnull final String text )
  {
    assertTrue( output.contains( text ),
                "Expected output\n---\n" + output + "\n---\nto contain text\n---\n" + text + "\n---\n" );
  }

  final void assertOutputDoesNotContain( @Nonnull final String output, @Nonnull final String text )
  {
    assertFalse( output.contains( text ),
                 "Expected output\n---\n" + output + "\n---\nto not contain text\n---\n" + text + "\n---\n" );
  }

  final Environment newEnvironment()
  {
    return newEnvironment( createLogger() );
  }

  final Environment newEnvironment( @Nonnull final Logger logger )
  {
    return new Environment( null, FileUtil.getCurrentDirectory(), logger );
  }

  @Nonnull
  final Logger createLogger()
  {
    return Logger.getAnonymousLogger();
  }

  @Nonnull
  final Logger createLogger( @Nonnull final TestHandler handler )
  {
    final Logger logger = Logger.getAnonymousLogger();
    logger.setUseParentHandlers( false );
    logger.addHandler( handler );
    return logger;
  }
}
