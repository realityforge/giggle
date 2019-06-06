package org.realityforge.giggle.integration;

import gir.io.FileUtil;
import graphql.language.Document;
import graphql.schema.GraphQLSchema;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import org.realityforge.giggle.AbstractTest;
import org.realityforge.giggle.document.DocumentRepository;
import org.realityforge.giggle.generator.GeneratorContext;
import org.realityforge.giggle.generator.GeneratorRepository;
import org.realityforge.giggle.schema.SchemaRepository;
import org.realityforge.giggle.util.MappingUtil;
import static org.testng.Assert.*;

public abstract class AbstractIntegrationTest
  extends AbstractTest
{
  @Nonnull
  protected final String toJavaFile( @Nonnull final CompileResults results, @Nonnull final String simpleName )
    throws Exception
  {
    final String filename = results.getPackageName().replace( ".", "/" ) + "/" + simpleName + ".java";
    final Path fileName = results.getJavaDirectory().resolve( filename );
    return new String( Files.readAllBytes( fileName ), StandardCharsets.UTF_8 );
  }

  @Nonnull
  protected final Class<?> toJavaClass( @Nonnull final CompileResults results, @Nonnull final String simpleName )
    throws Exception
  {
    final URLClassLoader classLoader = new URLClassLoader( new URL[]{ results.getClassesDirectory().toUri().toURL() } );
    return classLoader.loadClass( results.getPackageName() + "." + simpleName );
  }

  @SuppressWarnings( "SameParameterValue" )
  @Nonnull
  protected final CompileResults compileFragment( @Nonnull final String generator,
                                                  @Nonnull final String schemaFragment )
    throws Exception
  {
    final Path schemaFile = FileUtil.createLocalTempDir().resolve( "schema.graphql" );
    FileUtil.write( schemaFile.toString(),
                    "schema {\n" +
                    "  query: Query\n" +
                    "}\n" +
                    "\n" +
                    "type Query {\n" +
                    "}\n" +
                    schemaFragment );

    return generateAndCompile( generator,
                               Collections.singletonList( schemaFile ),
                               Collections.emptyList(),
                               Collections.emptyList(),
                               Collections.emptyList(),
                               null );
  }

  @SuppressWarnings( "SameParameterValue" )
  @Nonnull
  private CompileResults generateAndCompile( @Nonnull final String generator,
                                             @Nonnull final List<Path> schemaFiles,
                                             @Nonnull final List<Path> documentFiles,
                                             @Nonnull final List<Path> typeMappingFiles,
                                             @Nonnull final List<Path> fragmentMappingFiles,
                                             @Nullable final Path additionalJavaSource )
    throws Exception
  {
    final SchemaRepository schemaRepository = new SchemaRepository();
    final DocumentRepository documentRepository = new DocumentRepository();

    final GraphQLSchema schema = schemaRepository.getSchema( schemaFiles );
    final Document document =
      documentFiles.isEmpty() ?
      Document.newDocument().build() :
      documentRepository.getDocument( schema, documentFiles );
    final Map<String, String> typeMapping =
      typeMappingFiles.isEmpty() ? Collections.emptyMap() : MappingUtil.getMapping( typeMappingFiles );
    final Map<String, String> fragmentMapping =
      fragmentMappingFiles.isEmpty() ? Collections.emptyMap() : MappingUtil.getMapping( fragmentMappingFiles );

    final GeneratorContext context =
      new GeneratorContext( schema,
                            document,
                            typeMapping,
                            fragmentMapping,
                            FileUtil.createLocalTempDir(),
                            "com.example" );

    new GeneratorRepository().generate( generator, context );

    final Path classesDir = compileCode( collectJavaFiles( context.getOutputDirectory(), additionalJavaSource ) );
    return new CompileResults( context.getPackageName(), context.getOutputDirectory(), classesDir );
  }

  /**
   * Compile the supplied java files and make sure that they compile together.
   *
   * @param javaFiles the java files.
   * @throws Exception if there is an error
   */
  @Nonnull
  private Path compileCode( @Nonnull final List<File> javaFiles )
    throws Exception
  {
    assert !javaFiles.isEmpty();
    final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    final StandardJavaFileManager fileManager = compiler.getStandardFileManager( null, null, null );
    final Path output = FileUtil.createLocalTempDir();
    final Iterable<? extends JavaFileObject> compilationUnits =
      fileManager.getJavaFileObjectsFromFiles( javaFiles );
    final DiagnosticCollector<JavaFileObject> listener = new DiagnosticCollector<>();
    final JavaCompiler.CompilationTask compilationTask =
      compiler.getTask( null,
                        fileManager,
                        listener,
                        Arrays.asList( "-d", output.toString() ),
                        null,
                        compilationUnits );
    if ( !compilationTask.call() )
    {
      fail( "Failed to compile files:\n" +
            listener.getDiagnostics().stream().map( Object::toString ).collect( Collectors.joining( "\n" ) ) );
    }
    return output;
  }

  @Nonnull
  private List<File> collectJavaFiles( @Nonnull final Path... dirs )
    throws IOException
  {
    final List<File> javaFiles = new ArrayList<>();
    for ( final Path dir : dirs )
    {
      if ( null != dir && dir.toFile().exists() )
      {
        Files
          .walk( dir )
          .filter( p -> p.toString().endsWith( ".java" ) )
          .forEach( p -> javaFiles.add( p.toFile() ) );
      }
    }
    return javaFiles;
  }
}
