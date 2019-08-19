package org.realityforge.giggle;

import gir.io.FileUtil;
import graphql.language.Document;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import javax.annotation.Nonnull;
import org.realityforge.giggle.generator.Generator;
import org.realityforge.giggle.generator.GeneratorContext;
import org.realityforge.giggle.generator.GlobalGeneratorContext;
import org.realityforge.giggle.generator.PropertyDef;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class MainTest
  extends AbstractTest
{
  @Test
  public void printUsage()
  {
    final TestHandler handler = new TestHandler();
    Main.printUsage( newEnvironment( handler ) );
    assertEquals( handler.toString(),
                  "java org.realityforge.giggle.Main [options]\n" +
                  "\n" +
                  "Options:\n" +
                  "  -h, --help\n" +
                  "    print this message and exit\n" +
                  "  -q, --quiet\n" +
                  "    Do not output unless an error occurs.\n" +
                  "  -v, --verbose\n" +
                  "    Verbose output of differences.\n" +
                  "  --schema <argument>\n" +
                  "    The path to a graphql schema file.\n" +
                  "  --document <argument>\n" +
                  "    The path to a graphql document file.\n" +
                  "  --type-mapping <argument>\n" +
                  "    The path to a mapping file for types.\n" +
                  "  --fragment-mapping <argument>\n" +
                  "    The path to a mapping file for fragments.\n" +
                  "  -D, --define <argument>=<value>\n" +
                  "    Define a property used by the generators.\n" +
                  "  --package <argument>\n" +
                  "    The java package name used to generate artifacts.\n" +
                  "  --output-directory <argument>\n" +
                  "    The directory where generated files are output.\n" +
                  "  --generator <argument>\n" +
                  "    The name of a generator to run on the model.\n" +
                  "\n" +
                  "Supported Generators:\n" +
                  "  java-server\n" +
                  "  java-client" );
  }

  @Test
  public void printBanner()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final TestHandler handler = new TestHandler();
      final Environment environment = newEnvironment( handler );
      environment.setOutputDirectory( FileUtil.getCurrentDirectory().resolve( "generated" ) );
      environment.setPackageName( "com.biz" );
      environment.logger().setLevel( Level.ALL );
      Main.printBanner( environment );
      final String output = handler.toString();
      assertOutputContains( output, "Giggle Starting..." );
      assertOutputContains( output, "  Output directory: " );
      assertOutputContains( output, "  Output Package: " );
      assertOutputContains( output, "  Generators: " );
      assertOutputContains( output, "  Schema files: " );
      assertOutputContains( output, "  Document files: " );
      assertOutputContains( output, "  Type mapping files: " );
      assertOutputContains( output, "  Fragment mapping files: " );
      assertOutputNotContains( output, "  Property Defines:\n" );
    } );
  }

  @Test
  public void printBanner_withDefines()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final TestHandler handler = new TestHandler();
      final Environment environment = newEnvironment( handler );
      environment.setOutputDirectory( FileUtil.getCurrentDirectory().resolve( "generated" ) );
      environment.setPackageName( "com.biz" );
      environment.addDefine( "myKey1", "myValue" );
      environment.addDefine( "myKey2", "myValue" );
      environment.logger().setLevel( Level.ALL );
      Main.printBanner( environment );
      final String output = handler.toString();
      assertOutputContains( output, "Giggle Starting..." );
      assertOutputContains( output, "  Output directory: " );
      assertOutputContains( output, "  Output Package: " );
      assertOutputContains( output, "  Generators: " );
      assertOutputContains( output, "  Schema files: " );
      assertOutputContains( output, "  Document files: " );
      assertOutputContains( output, "  Type mapping files: " );
      assertOutputContains( output, "  Fragment mapping files: " );
      assertOutputContains( output, "  Property Defines:\n" );
      assertOutputContains( output, "    myKey1=myValue" );
      assertOutputContains( output, "    myKey2=myValue" );
    } );
  }

  @Test
  public void processOptions_unknownArgument()
  {
    assertEquals( processOptions( false, "someArg" ), "Error: Unknown argument: someArg" );
  }

  @Test
  public void processOptions()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      writeFile( "schema.graphql" );
      final TestHandler handler = new TestHandler();
      final Environment environment = newEnvironment( handler );
      processOptions( true,
                      environment,
                      "--output-directory", "output",
                      "--package", "com.example.model",
                      "--schema", "schema.graphql" );
      assertEquals( handler.toString(), "" );
      assertTrue( environment.hasOutputDirectory() );
      assertEquals( environment.getOutputDirectory(), FileUtil.getCurrentDirectory().resolve( "output" ) );
      assertTrue( environment.hasPackageName() );
      assertEquals( environment.getPackageName(), "com.example.model" );
    } );
  }

  @Test
  public void processOptions_singleGenerator()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      writeFile( "schema.graphql" );
      final TestHandler handler = new TestHandler();
      final Environment environment = newEnvironment( handler );
      processOptions( true,
                      environment,
                      "--output-directory", "output",
                      "--package", "com.example.model",
                      "--schema", "schema.graphql",
                      "--generator=java-server" );
      assertEquals( handler.toString(), "" );
      assertEquals( environment.getGenerators(), Collections.singletonList( "java-server" ) );
    } );
  }

  @Test
  public void processOptions_multipleGenerators()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      writeFile( "schema.graphql" );
      final TestHandler handler = new TestHandler();
      final Environment environment = newEnvironment( handler );
      processOptions( true,
                      environment,
                      "--output-directory", "output",
                      "--package", "com.example.model",
                      "--schema", "schema.graphql",
                      "--generator=java-server",
                      "--generator=java-client" );
      assertEquals( handler.toString(), "" );
      assertEquals( environment.getGenerators(), Arrays.asList( "java-server", "java-client" ) );
    } );
  }

  @Test
  public void processOptions_duplicateGenerators()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      writeFile( "schema.graphql" );
      final String output = processOptions( false,
                                            "--output-directory", "output",
                                            "--package", "com.example.model",
                                            "--schema", "schema.graphql",
                                            "--generator=graphql-java-server",
                                            "--generator=graphql-java-server" );
      assertOutputContains( output,
                            "Error: Duplicate generators specified: graphql-java-server" );
    } );
  }

  @Test
  public void processOptions_generatorButNoOutput()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      writeFile( "schema.graphql" );
      final String output =
        processOptions( false,
                        "--package", "com.example.model",
                        "--schema", "schema.graphql",
                        "--generator=graphql-java-server" );
      assertOutputContains( output, "Error: Must specify output directory if a generator is specified." );
    } );
  }

  @Test
  public void processOptions_outputDirectoryIsAFile()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      writeFile( "output" );
      writeFile( "schema.graphql" );
      assertEquals( processOptions( false,
                                    "--output-directory", "output",
                                    "--package", "com.example.model",
                                    "--schema", "schema.graphql" ),
                    "Error: Specified output directory exists and is not a directory. Specified value: output" );
    } );
  }

  @Test
  public void processOptions_noSchema()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final String output = processOptions( false );
      assertOutputContains( output, "Error: No schema files specified." );
    } );
  }

  @Test
  public void processOptions_schemaNoExist()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final String output = processOptions( false, "--schema", "myschema.graphql" );
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
      processOptions( true,
                      environment,
                      "--output-directory", "output",
                      "--package", "com.example.model",
                      "--schema", "schema.graphql" );

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
      processOptions( true,
                      environment,
                      "--output-directory", "output",
                      "--package", "com.example.model",
                      "--schema", "schema1.graphql",
                      "--schema", "schema2.graphql" );

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
      final String output = processOptions( false, "--document", "query.graphql" );
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
      processOptions( true,
                      environment,
                      "--output-directory", "output",
                      "--package", "com.example.model",
                      "--schema", "schema.graphql",
                      "--document", "query.graphql" );

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
      processOptions( true,
                      environment,
                      "--output-directory", "output",
                      "--package", "com.example.model",
                      "--schema", "schema.graphql",
                      "--document", "query.graphql",
                      "--document", "mutation.graphql" );

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
      processOptions( true,
                      environment,
                      "--output-directory", "output",
                      "--package", "com.example.model",
                      "--schema", "schema.graphql",
                      "--document", "query.graphql",
                      "--type-mapping", "type1-mapping.properties",
                      "--type-mapping", "type2-mapping.properties",
                      "--fragment-mapping", "fragment1-mapping.properties",
                      "--fragment-mapping", "fragment2-mapping.properties" );

      assertEquals( environment.getSchemaFiles(), Collections.singletonList( toPath( "schema.graphql" ) ) );
      assertEquals( environment.getDocumentFiles(), Collections.singletonList( toPath( "query.graphql" ) ) );
      assertEquals( environment.getTypeMappingFiles(), Arrays.asList( toPath( "type1-mapping.properties" ),
                                                                      toPath( "type2-mapping.properties" ) ) );
      assertEquals( environment.getFragmentMappingFiles(), Arrays.asList( toPath( "fragment1-mapping.properties" ),
                                                                          toPath( "fragment2-mapping.properties" ) ) );
    } );
  }

  @Test
  public void processOptions_typeMappingNoExist()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      writeFile( "schema.graphql" );
      writeFile( "query.graphql" );
      final String output = processOptions( false,
                                            "--schema", "schema.graphql",
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
      final String output = processOptions( false,
                                            "--schema", "schema.graphql",
                                            "--document", "query.graphql",
                                            "--fragment-mapping", "mapping.properties" );
      assertOutputContains( output,
                            "Error: Specified graphql fragment mapping file does not exist. Specified value: mapping.properties" );
    } );
  }

  @Test
  public void verifyTypeMapping_noMappings()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final GlobalGeneratorContext context =
        new GlobalGeneratorContext( buildGraphQLSchema( "" ),
                                    Document.newDocument().build(),
                                    Collections.emptyMap(),
                                    Collections.emptyMap(),
                                    Collections.emptyMap(),
                                    FileUtil.createLocalTempDir(),
                                    "com.example" );
      Main.verifyTypeMapping( context );
    } );
  }

  @Test
  public void verifyTypeMapping_validMappings()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final HashMap<String, String> typeMapping = new HashMap<>();
      typeMapping.put( "Person", "com.biz.Person" );
      final GlobalGeneratorContext context =
        new GlobalGeneratorContext( buildGraphQLSchema( "type Person {\n" +
                                                        "  name: String\n" +
                                                        "}\n" ),
                                    Document.newDocument().build(),
                                    typeMapping,
                                    Collections.emptyMap(),
                                    Collections.emptyMap(),
                                    FileUtil.createLocalTempDir(),
                                    "com.example" );
      Main.verifyTypeMapping( context );
    } );
  }

  @Test
  public void verifyTypeMapping_invalidMappings()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final HashMap<String, String> typeMapping = new HashMap<>();
      typeMapping.put( "Person", "com.biz.Person" );
      final GlobalGeneratorContext context =
        new GlobalGeneratorContext( buildGraphQLSchema( "" ),
                                    Document.newDocument().build(),
                                    typeMapping,
                                    Collections.emptyMap(),
                                    Collections.emptyMap(),
                                    FileUtil.createLocalTempDir(),
                                    "com.example" );
      final TerminalStateException exception =
        expectThrows( TerminalStateException.class, () -> Main.verifyTypeMapping( context ) );
      assertEquals( exception.getMessage(),
                    "Type mapping attempted to map the type named 'Person' to com.biz.Person but there is no type named 'Person'" );
      assertEquals( exception.getExitCode(), ExitCodes.BAD_TYPE_MAPPING_EXIT_CODE );
    } );
  }

  @Test
  public void processOptions_singleDefine()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      writeFile( "schema.graphql" );
      final TestHandler handler = new TestHandler();
      final Environment environment = newEnvironment( handler );
      environment.getGeneratorRepository().registerGenerator( new TestGenerator() );
      processOptions( true,
                      environment,
                      "--output-directory", "output",
                      "--package", "com.example.model",
                      "--schema", "schema.graphql",
                      "--generator=test-generator",
                      "-Dmyprop=value" );
      assertEquals( handler.toString(), "" );
      final Map<String, String> defines = environment.getDefines();
      assertEquals( defines.size(), 1 );
      assertEquals( defines.get( "myprop" ), "value" );
    } );
  }

  @Test
  public void processOptions_multipleDefines()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      writeFile( "schema.graphql" );
      final TestHandler handler = new TestHandler();
      final Environment environment = newEnvironment( handler );
      environment.getGeneratorRepository().registerGenerator( new TestGenerator() );
      processOptions( true,
                      environment,
                      "--output-directory", "output",
                      "--package", "com.example.model",
                      "--schema", "schema.graphql",
                      "--generator=test-generator",
                      "-Dmyprop=value",
                      "-Dmyprop2=value2" );
      assertEquals( handler.toString(), "" );
      final Map<String, String> defines = environment.getDefines();
      assertEquals( defines.size(), 2 );
      assertEquals( defines.get( "myprop" ), "value" );
      assertEquals( defines.get( "myprop2" ), "value2" );
    } );
  }

  @Test
  public void processOptions_requiredDefineNotPresent()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      writeFile( "schema.graphql" );
      final TestHandler handler = new TestHandler();
      final Environment environment = newEnvironment( handler );
      environment.getGeneratorRepository().registerGenerator( new TestGenerator() );
      processOptions( false,
                      environment,
                      "--output-directory", "output",
                      "--package", "com.example.model",
                      "--schema", "schema.graphql",
                      "--generator=test-generator",
                      "-Dmyprop2=value2" );
      assertEquals( handler.toString(),
                    "Error: Property named 'myprop' is required by the generator named 'test-generator' but has not been defined." );
    } );
  }

  @Test
  public void processOptions_defineNotUsed()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      writeFile( "schema.graphql" );
      final TestHandler handler = new TestHandler();
      final Environment environment = newEnvironment( handler );
      processOptions( false,
                      environment,
                      "--output-directory", "output",
                      "--package", "com.example.model",
                      "--schema", "schema.graphql",
                      "-Dnotused=value" );
      assertEquals( handler.toString(), "Error: Property defined with name 'notused' is not used by any generator." );
    } );
  }

  @Test
  public void processOptions_duplicateDefines()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      writeFile( "schema.graphql" );
      final TestHandler handler = new TestHandler();
      final Environment environment = newEnvironment( handler );
      processOptions( false,
                      environment,
                      "--output-directory", "output",
                      "--package", "com.example.model",
                      "--schema", "schema.graphql",
                      "-Dmyprop=value",
                      "-Dmyprop=value" );
      assertEquals( handler.toString(), "Error: Duplicate property defined specified: myprop" );
    } );
  }

  @SuppressWarnings( "SameParameterValue" )
  @Nonnull
  private String processOptions( final boolean expectedResult, @Nonnull final String... args )
  {
    final TestHandler handler = new TestHandler();
    processOptions( expectedResult, newEnvironment( handler ), args );
    return handler.toString();
  }

  private void processOptions( final boolean expectedResult,
                               @Nonnull final Environment environment,
                               @Nonnull final String... args )
  {
    final boolean result = Main.processOptions( environment, args );
    assertEquals( result, expectedResult, "Return value for Main.processOptions" );
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

  @Generator.MetaData( name = "test-generator" )
  private static class TestGenerator
    implements Generator
  {
    @Nonnull
    @Override
    public Set<PropertyDef> getSupportedProperties()
    {
      final Set<PropertyDef> propertyDefs = new HashSet<>();
      propertyDefs.add( new PropertyDef( "myprop", true, "a required property for testing" ) );
      propertyDefs.add( new PropertyDef( "myprop2", false, "another property for testing" ) );
      return propertyDefs;
    }

    @Override
    public void generate( @Nonnull final GeneratorContext context )
    {
    }
  }
}
