package org.realityforge.giggle;

import gir.io.FileUtil;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
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
                  "\t\tThe path to a graphql schema file.\n" +
                  "\t-d, --document <argument>\n" +
                  "\t\tThe path to a graphql document file.\n" +
                  "\t--type-mapping <argument>\n" +
                  "\t\tThe path to a mapping file for types.\n" +
                  "\t--fragment-mapping <argument>\n" +
                  "\t\tThe path to a mapping file for fragments.\n" +
                  "\t--operation-mapping <argument>\n" +
                  "\t\tThe path to a mapping file for operations." );
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
      assertOutputContains( output,
                            "Error: Specified graphql schema file does not exist. Specified value: myschema.graphql" );
    } );
  }

  @Test
  public void processOptions_singleSchema()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final Environment environment = newEnvironment();
      writeFile( "schema.graphql" );
      Main.processOptions( environment, "--schema", "schema.graphql" );

      assertEquals( environment.getSchemaFiles().get( 0 ), toPath( "schema.graphql" ) );
    } );
  }

  @Test
  public void processOptions_multipleSchemas()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final Environment environment = newEnvironment();
      writeFile( "schema1.graphql" );
      writeFile( "schema2.graphql" );
      Main.processOptions( environment, "--schema", "schema1.graphql", "--schema", "schema2.graphql" );

      final List<Path> schemaFiles = environment.getSchemaFiles();
      assertEquals( schemaFiles.get( 0 ), toPath( "schema1.graphql" ) );
      assertEquals( schemaFiles.get( 1 ), toPath( "schema2.graphql" ) );
    } );
  }

  @Test
  public void processOptions_documentNoExist()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      writeFile( "schema.graphql" );
      final String output = processOptions( "--schema", "schema.graphql", "--document", "query.graphql" );
      assertOutputContains( output,
                            "Error: Specified graphql document file does not exist. Specified value: query.graphql" );
    } );
  }

  @Test
  public void processOptions_singleDocument()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final Environment environment = newEnvironment();
      writeFile( "schema.graphql" );
      writeFile( "query.graphql" );
      Main.processOptions( environment, "--schema", "schema.graphql", "--document", "query.graphql" );

      assertEquals( environment.getDocumentFiles().get( 0 ), toPath( "query.graphql" ) );
    } );
  }

  @Test
  public void processOptions_multipleDocuments()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final Environment environment = newEnvironment();
      writeFile( "schema.graphql" );
      writeFile( "query.graphql" );
      writeFile( "mutation.graphql" );
      Main.processOptions( environment,
                           "--schema",
                           "schema.graphql",
                           "--document",
                           "query.graphql",
                           "--document",
                           "mutation.graphql" );

      final List<Path> documentFiles = environment.getDocumentFiles();
      assertEquals( documentFiles.get( 0 ), toPath( "query.graphql" ) );
      assertEquals( documentFiles.get( 1 ), toPath( "mutation.graphql" ) );
    } );
  }

  @Test
  public void processOptions_mappingFiles()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final Environment environment = newEnvironment();
      writeFile( "schema.graphql" );
      writeFile( "query.graphql" );
      writeFile( "type1-mapping.properties" );
      writeFile( "type2-mapping.properties" );
      writeFile( "fragment1-mapping.properties" );
      writeFile( "fragment2-mapping.properties" );
      writeFile( "operation1-mapping.properties" );
      writeFile( "operation2-mapping.properties" );
      Main.processOptions( environment,
                           "--schema", "schema.graphql",
                           "--document", "query.graphql",
                           "--type-mapping", "type1-mapping.properties",
                           "--type-mapping", "type2-mapping.properties",
                           "--fragment-mapping", "fragment1-mapping.properties",
                           "--fragment-mapping", "fragment2-mapping.properties",
                           "--operation-mapping", "operation1-mapping.properties",
                           "--operation-mapping", "operation2-mapping.properties" );

      assertEquals( environment.getSchemaFiles(), Collections.singletonList( toPath( "schema.graphql" ) ) );
      assertEquals( environment.getDocumentFiles(), Collections.singletonList( toPath( "query.graphql" ) ) );
      assertEquals( environment.getTypeMappingFiles(), Arrays.asList( toPath( "type1-mapping.properties" ),
                                                                      toPath( "type2-mapping.properties" ) ) );
      assertEquals( environment.getFragmentMappingFiles(), Arrays.asList( toPath( "fragment1-mapping.properties" ),
                                                                          toPath( "fragment2-mapping.properties" ) ) );
      assertEquals( environment.getOperationMappingFiles(), Arrays.asList( toPath( "operation1-mapping.properties" ),
                                                                           toPath( "operation2-mapping.properties" ) ) );
    } );
  }

  @Test
  public void processOptions_typeMappingNoExist()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      writeFile( "schema.graphql" );
      writeFile( "query.graphql" );
      final String output = processOptions( "--schema", "schema.graphql",
                                            "--document", "query.graphql",
                                            "--type-mapping", "mapping.properties" );
      assertOutputContains( output,
                            "Error: Specified graphql type mapping file does not exist. Specified value: mapping.properties" );
    } );
  }

  @Test
  public void processOptions_fragmentMappingNoExist()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      writeFile( "schema.graphql" );
      writeFile( "query.graphql" );
      final String output = processOptions( "--schema", "schema.graphql",
                                            "--document", "query.graphql",
                                            "--fragment-mapping", "mapping.properties" );
      assertOutputContains( output,
                            "Error: Specified graphql fragment mapping file does not exist. Specified value: mapping.properties" );
    } );
  }

  @Test
  public void processOptions_operationMappingNoExist()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      writeFile( "schema.graphql" );
      writeFile( "query.graphql" );
      final String output = processOptions( "--schema", "schema.graphql",
                                            "--document", "query.graphql",
                                            "--operation-mapping", "mapping.properties" );
      assertOutputContains( output,
                            "Error: Specified graphql operation mapping file does not exist. Specified value: mapping.properties" );
    } );
  }

  @Nonnull
  private String processOptions( @Nonnull final String... args )
  {
    final TestHandler handler = new TestHandler();
    Main.processOptions( newEnvironment( createLogger( handler ) ), args );
    return handler.toString();
  }

  private void writeFile( @Nonnull final String path )
    throws IOException
  {
    FileUtil.write( path, "ContentIgnored" );
  }

  @Nonnull
  private Path toPath( @Nonnull final String filename )
  {
    return FileUtil.getCurrentDirectory().resolve( filename );
  }
}
