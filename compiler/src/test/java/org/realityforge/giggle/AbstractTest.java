package org.realityforge.giggle;

import gir.Gir;
import gir.Task;
import gir.io.FileUtil;
import graphql.language.Document;
import graphql.schema.GraphQLSchema;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import org.realityforge.giggle.document.DocumentRepository;
import org.realityforge.giggle.generator.GeneratorContext;
import org.realityforge.giggle.schema.SchemaRepository;
import static org.testng.Assert.*;

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

  final void assertOutputNotContains( @Nonnull final String output, @Nonnull final String text )
  {
    assertFalse( output.contains( text ),
                "Expected output\n---\n" + output + "\n---\nto not contain text\n---\n" + text + "\n---\n" );
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

  @Nonnull
  protected final GeneratorContext newContext( @Nonnull final Path outputDir )
    throws IOException
  {
    return new GeneratorContext( buildGraphQLSchema( "" ),
                                 Document.newDocument().build(),
                                 Collections.emptyMap(),
                                 Collections.emptyMap(),
                                 outputDir,
                                 "com.example" );
  }

  @Nonnull
  protected final GraphQLSchema buildGraphQLSchema( @Nonnull final String schemaExtension )
    throws IOException
  {
    final Path schemaFile =
      writeContent( "schema.graphql",
                    "schema {\n" +
                    "  query: Query\n" +
                    "}\n" +
                    "type Query {\n" +
                    "}\n" +
                    schemaExtension );

    return new SchemaRepository().getSchema( Collections.singletonList( schemaFile ) );
  }

  @Nonnull
  protected final Document buildDocument( @Nonnull final GraphQLSchema schema, @Nonnull final String content )
    throws IOException
  {
    final Path file = writeContent( "document.graphql", content );

    return new DocumentRepository().getDocument( schema, Collections.singletonList( file ) );
  }
}
