package org.realityforge.giggle;

import gir.io.FileUtil;
import java.nio.file.Path;
import java.util.List;
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

  @Test
  public void processOptions_schemaNoExist()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final String output = processOptions( "--schema", "myschema.graphql" );
      assertOutputContains( output, "Error: Specified graphql schema file does not exist. Specified value: myschema.graphql" );
    } );
  }

  @Test
  public void processOptions_singleSchema()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final Environment environment = newEnvironment();
      FileUtil.write( "schema.graphql", "ContentIgnored" );
      Main.processOptions( environment, "--schema", "schema.graphql" );

      final Path file = FileUtil.getCurrentDirectory().resolve( "schema.graphql" );
      assertEquals( environment.getSchemaFiles().get( 0 ), file );
    } );
  }

  @Test
  public void processOptions_multipleSchemas()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final Environment environment = newEnvironment();
      FileUtil.write( "schema1.graphql", "ContentIgnored" );
      FileUtil.write( "schema2.graphql", "ContentIgnored" );
      Main.processOptions( environment, "--schema", "schema1.graphql", "--schema", "schema2.graphql" );

      final Path file1 = FileUtil.getCurrentDirectory().resolve( "schema1.graphql" );
      final Path file2 = FileUtil.getCurrentDirectory().resolve( "schema2.graphql" );
      final List<Path> schemaFiles = environment.getSchemaFiles();
      assertEquals( schemaFiles.get( 0 ), file1 );
      assertEquals( schemaFiles.get( 1 ), file2 );
    } );
  }

  @Nonnull
  private String processOptions( @Nonnull final String... args )
  {
    final TestHandler handler = new TestHandler();
    Main.processOptions( newEnvironment( createLogger( handler ) ), args );
    return handler.toString();
  }
}
