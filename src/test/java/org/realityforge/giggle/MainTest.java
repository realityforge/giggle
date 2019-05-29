package org.realityforge.giggle;

import java.util.logging.LogRecord;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class MainTest
  extends AbstractTest
{
  @Test
  public void printUsage()
  {
    final TestHandler handler = new TestHandler();
    Main.printUsage( newEnvironment( createLogger( handler ) ) );
    assertEquals( handler.toString(),
                  "java org.realityforge.giggle.Main [options]\n" +
                  "\tOptions:\n" +
                  "\t-h, --help\n" +
                  "\t\tprint this message and exit\n" +
                  "\t-q, --quiet\n" +
                  "\t\tDo not output unless an error occurs.\n" +
                  "\t-v, --verbose\n" +
                  "\t\tVerbose output of differences.\n" +
                  "\t-s, --schema <argument>\n" +
                  "\t\tThe path to a graphql schema file." );
  }

  @Test
  public void processOptions_unknownArgument()
  {
    assertEquals( processOptions( "someArg" ), "Error: Unknown argument: someArg" );
  }

  @Test
  public void processOptions_noSchema()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final String output = processOptions();
      assertOutputContains( output, "Error: No schema files specified." );
    } );
  }

  @Nonnull
  private String processOptions( @Nonnull final String... args )
  {
    final TestHandler handler = new TestHandler();
    Main.processOptions( newEnvironment( createLogger( handler ) ), args );
    return handler.getRecords().stream().map( LogRecord::getMessage ).collect( Collectors.joining( "\n" ) );
  }
}
