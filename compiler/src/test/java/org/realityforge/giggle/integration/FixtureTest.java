package org.realityforge.giggle.integration;

import gir.io.FileUtil;
import graphql.language.Document;
import graphql.schema.GraphQLSchema;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import org.realityforge.giggle.AbstractTest;
import org.realityforge.giggle.document.DocumentRepository;
import org.realityforge.giggle.document.DocumentValidateException;
import org.realityforge.giggle.generator.GeneratorRepository;
import org.realityforge.giggle.generator.GlobalGeneratorContext;
import org.realityforge.giggle.schema.SchemaRepository;
import org.realityforge.giggle.util.MappingUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class FixtureTest
  extends AbstractTest
{
  @DataProvider( name = "fixtureTests" )
  public Object[][] fixtureTests()
  {
    final List<Object[]> results = new ArrayList<>();
    final File file = fixtureDir().toFile();
    final File[] dirs = file.listFiles( File::isDirectory );
    if ( null != dirs )
    {
      for ( final File dir : dirs )
      {
        final File[] generatorDirs = new File( dir, "output" ).listFiles( File::isDirectory );
        if ( null != generatorDirs )
        {
          for ( final File generatorDir : generatorDirs )
          {
            results.add( new Object[]{ dir.getName(), generatorDir.getName() } );
          }
        }
      }
    }
    return results.toArray( new Object[][]{} );
  }

  @Test( dataProvider = "fixtureTests" )
  public void fixtureTest( @Nonnull final String name, @Nonnull final String generator )
    throws Exception
  {
    final Path baseDir = fixtureDir().resolve( name );
    final Path inputDir = baseDir.resolve( "input" );
    final Path generatorOutputDir = baseDir.resolve( "output" ).resolve( generator );
    final List<Path> defineFiles = collectFilesRecursively( inputDir, generator + ".properties" );
    final List<Path> schemaFiles = collectFilesRecursively( inputDir, ".graphqls" );
    final List<Path> documentFiles = collectFilesRecursively( inputDir, ".graphql" );
    final List<Path> typeMappingFiles = collectFilesRecursively( inputDir, "types.properties" );
    final List<Path> fragmentMappingFiles = collectFilesRecursively( inputDir, "fragments.properties" );

    final SchemaRepository schemaRepository = new SchemaRepository();
    final DocumentRepository documentRepository = new DocumentRepository();

    final GraphQLSchema schema = schemaRepository.getSchema( schemaFiles );
    final Document document = getDocument( documentRepository, schema, documentFiles );
    final Map<String, String> typeMapping =
      typeMappingFiles.isEmpty() ? Collections.emptyMap() : MappingUtil.getMapping( typeMappingFiles );
    final Map<String, String> fragmentMapping =
      fragmentMappingFiles.isEmpty() ? Collections.emptyMap() : MappingUtil.getMapping( fragmentMappingFiles );
    final Path outputDirectory = FileUtil.createLocalTempDir();

    final Map<String, String> defines = new HashMap<>();
    defineFiles.forEach( d -> loadDefines( defines, d ) );

    final GlobalGeneratorContext context =
      new GlobalGeneratorContext( schema,
                                  document,
                                  typeMapping,
                                  fragmentMapping,
                                  defines,
                                  outputDirectory,
                                  "com.example." + name );

    new GeneratorRepository().getGenerator( generator ).generate( context );

    if ( outputFixtureData() )
    {
      FileUtil.deleteDirIfExists( generatorOutputDir );
      FileUtil.copyDirectory( outputDirectory, generatorOutputDir );
    }

    final List<File> javaFiles = collectJavaFiles( outputDirectory, inputDir.resolve( "java" ) );

    if ( "java-cdi-client".equals( generator ) )
    {
      // The java-cdi-client generator builds on output of java-client so we need to include
      // the output of that generator during compilation
      final Path javaClientOutput = generatorOutputDir.getParent().resolve( "java-client" );
      javaFiles.addAll( collectJavaFiles( javaClientOutput, inputDir.resolve( "java" ) ) );
    }
    final List<File> classpathEntries = collectLibs( generator );
    classpathEntries.addAll( collectLibs( "all" ) );

    ensureGeneratedCodeCompiles( javaFiles, classpathEntries );

    assertDirectoriesEquivalent( outputDirectory, generatorOutputDir );
  }

  @Nonnull
  private Document getDocument( final DocumentRepository documentRepository,
                                final GraphQLSchema schema,
                                final List<Path> documentFiles )
  {
    if ( documentFiles.isEmpty() )
    {
      return Document.newDocument().build();
    }
    else
    {
      try
      {
        return documentRepository.getDocument( schema, documentFiles );
      }
      catch ( final DocumentValidateException e )
      {
        fail( "Failed to read document due to " + e.getErrors() );
        throw e;
      }
    }
  }

  @Nonnull
  private List<File> collectLibs( @Nonnull final String generator )
  {
    final String libraries = System.getProperty( "giggle.fixture." + generator + ".libs" );
    return null == libraries ?
           new ArrayList<>() :
           Arrays
             .stream( libraries.split( File.pathSeparator ) )
             .map( File::new )
             .collect( Collectors.toList() );
  }

  private void loadDefines( @Nonnull final Map<String, String> defines, @Nonnull final Path path )
  {
    final Properties properties = new Properties();
    try
    {
      properties.load( new FileInputStream( path.toFile() ) );
    }
    catch ( final IOException ioe )
    {
      assertNull( ioe );
    }
    for ( final String propertyName : properties.stringPropertyNames() )
    {
      defines.put( propertyName, properties.getProperty( propertyName ) );
    }
  }

  @Nonnull
  private List<Path> collectFilesRecursively( @Nonnull final Path dir, @Nonnull final String suffix )
    throws IOException
  {
    return Files
      .walk( dir )
      .filter( path -> path.toString().endsWith( suffix ) )
      .sorted()
      .collect( Collectors.toList() );
  }

  private void assertDirectoriesEquivalent( @Nonnull final Path actualDir, @Nonnull final Path expectedDir )
    throws IOException
  {
    final List<Path> actualFiles =
      Files
        .walk( actualDir )
        .filter( Files::isRegularFile )
        .map( actualDir::relativize )
        .sorted()
        .collect( Collectors.toList() );
    final List<Path> expectedFiles =
      Files
        .walk( expectedDir )
        .filter( Files::isRegularFile )
        .map( expectedDir::relativize )
        .sorted()
        .collect( Collectors.toList() );

    assertEquals( actualFiles, expectedFiles, "Expected output files do not match actual output files" );

    for ( final Path file : actualFiles )
    {
      final Path actual = actualDir.resolve( file );
      final Path expected = expectedDir.resolve( file );
      final String actualContent = new String( Files.readAllBytes( actual ), StandardCharsets.US_ASCII );
      final String expectedContent = new String( Files.readAllBytes( expected ), StandardCharsets.US_ASCII );
      assertEquals( actualContent, expectedContent, "Actual file content does not match expected for file " + file );
    }
  }

  /**
   * Compile the supplied java files and make sure that they compile together.
   *
   * @param javaFiles the java files.
   * @throws Exception if there is an error
   */
  private void ensureGeneratedCodeCompiles( @Nonnull final List<File> javaFiles,
                                            @Nonnull final List<File> classpathEntries )
    throws Exception
  {
    if ( !javaFiles.isEmpty() )
    {
      final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      final StandardJavaFileManager fileManager = compiler.getStandardFileManager( null, null, null );
      final Path output = FileUtil.createLocalTempDir();
      final Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles( javaFiles );
      final DiagnosticCollector<JavaFileObject> listener = new DiagnosticCollector<>();
      final String classpath =
        classpathEntries.stream().map( File::getAbsolutePath ).collect( Collectors.joining( File.pathSeparator ) );
      final JavaCompiler.CompilationTask compilationTask =
        compiler.getTask( null,
                          fileManager,
                          listener,
                          Arrays.asList( "-d", output.toString(), "-cp", classpath, "-Xlint:all,-processing,-serial" ),
                          null,
                          compilationUnits );
      if ( !compilationTask.call() )
      {
        fail( "Failed to compile files:\n" +
              listener.getDiagnostics().stream().map( Object::toString ).collect( Collectors.joining( "\n" ) ) );
      }
    }
  }

  @Nonnull
  private List<File> collectJavaFiles( @Nonnull final Path... dirs )
    throws IOException
  {
    final List<File> javaFiles = new ArrayList<>();
    for ( final Path dir : dirs )
    {
      if ( dir.toFile().exists() )
      {
        Files
          .walk( dir )
          .filter( p -> p.toString().endsWith( ".java" ) )
          .forEach( p -> javaFiles.add( p.toFile() ) );
      }
    }
    return javaFiles;
  }

  @Nonnull
  private Path fixtureDir()
  {
    final String property = System.getProperty( "giggle.fixture_dir" );
    assertNotNull( property, "Require the jvm system setting 'giggle.fixture_dir' to be set" );
    return Paths.get( property );
  }

  private boolean outputFixtureData()
  {
    return System.getProperty( "giggle.output_fixture_data", "false" ).equals( "true" );
  }
}
