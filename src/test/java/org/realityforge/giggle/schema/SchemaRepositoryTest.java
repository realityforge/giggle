package org.realityforge.giggle.schema;

import gir.io.FileUtil;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.errors.SchemaProblem;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.realityforge.giggle.AbstractTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SchemaRepositoryTest
  extends AbstractTest
{
  @Test
  public void getSchema_singleFile()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      FileUtil.write( "schema.graphql",
                      "schema {\n" +
                      "  query: Query\n" +
                      "}\n" +
                      "type Query {\n" +
                      "}" );
      final SchemaRepository schemaRepository = new SchemaRepository();
      final Path schemaFile = FileUtil.getCurrentDirectory().resolve( "schema.graphql" );
      final List<Path> components = Collections.singletonList( schemaFile );

      final GraphQLSchema schema1 = schemaRepository.getSchema( components );
      final GraphQLSchema schema2 = schemaRepository.getSchema( components );

      // They must be the same instance
      assertSame( schema1, schema2 );

      //Slightly different file with trailing whitespace
      FileUtil.write( "schema.graphql",
                      "schema {\n" +
                      "  query: Query\n" +
                      "}\n" +
                      "type Query {\n" +
                      "}  " );

      final GraphQLSchema schema3 = schemaRepository.getSchema( components );

      assertNotSame( schema1, schema3 );
      assertNotSame( schema2, schema3 );
    } );
  }

  @Test
  public void getSchema_multipleFiles()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      FileUtil.write( "schema1.graphql",
                      "schema {\n" +
                      "  query: Query\n" +
                      "}\n" +
                      "type Query {\n" +
                      "}" );
      FileUtil.write( "schema2.graphql",
                      " type Person {\n" +
                      "}\n" +
                      "extend type Query {\n" +
                      "  person(id: ID!): Person\n" +
                      "}" );
      final SchemaRepository schemaRepository = new SchemaRepository();
      final Path schemaFile1 = FileUtil.getCurrentDirectory().resolve( "schema1.graphql" );
      final Path schemaFile2 = FileUtil.getCurrentDirectory().resolve( "schema2.graphql" );
      final List<Path> components = Arrays.asList( schemaFile1, schemaFile2 );

      final GraphQLSchema schema1 = schemaRepository.getSchema( components );
      final GraphQLSchema schema2 = schemaRepository.getSchema( components );

      assertNotNull( schema1.getQueryType() );
      assertEquals( schema1.getQueryType().getChildren().size(), 1 );
      assertNull( schema1.getMutationType() );
      assertNull( schema1.getSubscriptionType() );

      // They must be the same instance
      assertSame( schema1, schema2 );

      //Slightly different file with trailing whitespace
      FileUtil.write( "schema1.graphql",
                      "schema {\n" +
                      "  query: Query\n" +
                      "}\n" +
                      "type Query {\n" +
                      "}  " );

      final GraphQLSchema schema3 = schemaRepository.getSchema( components );

      assertNotNull( schema3.getQueryType() );
      assertEquals( schema3.getQueryType().getChildren().size(), 1 );
      assertNull( schema3.getMutationType() );
      assertNull( schema3.getSubscriptionType() );

      assertNotSame( schema1, schema3 );
      assertNotSame( schema2, schema3 );

      // Change the order of the files produces a different instance
      final GraphQLSchema schema4 = schemaRepository.getSchema( Arrays.asList( schemaFile2, schemaFile1 ) );

      assertNotSame( schema4, schema3 );
    } );
  }

  @Test
  public void getSchema_fileMissing()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final SchemaRepository schemaRepository = new SchemaRepository();
      final Path schemaFile = FileUtil.getCurrentDirectory().resolve( "schema.graphql" );
      final List<Path> components = Collections.singletonList( schemaFile );

      final SchemaReadException exception =
        expectThrows( SchemaReadException.class, () -> schemaRepository.getSchema( components ) );
      assertTrue( exception.getMessage().startsWith( "Error reading schema file " ) );
    } );
  }

  @Test
  public void getSchema_includeNonStandardScalars()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      FileUtil.write( "schema.graphql",
                      "schema {\n" +
                      "  query: Query\n" +
                      "}\n" +
                      "type Query {\n" +
                      "}\n" +
                      "scalar Date\n" +
                      "type Person {\n" +
                      "  DOB: Date\n" +
                      "}" );
      final SchemaRepository schemaRepository = new SchemaRepository();
      final Path schemaFile = FileUtil.getCurrentDirectory().resolve( "schema.graphql" );
      final List<Path> components = Collections.singletonList( schemaFile );

      final GraphQLSchema schema = schemaRepository.getSchema( components );

      assertNotNull( schema.getType( "Date" ) );
    } );
  }

  @Test
  public void getSchema_SchemaProblem()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      FileUtil.write( "schema.graphql",
                      "schema {\n" +
                      "  query: Query\n" +
                      "}\n" +
                      "type Query {\n" +
                      "}\n" +
                      "type Person {\n" +
                      "  DOB: Date\n" +
                      "}" );
      final SchemaRepository schemaRepository = new SchemaRepository();
      final Path schemaFile = FileUtil.getCurrentDirectory().resolve( "schema.graphql" );
      final List<Path> components = Collections.singletonList( schemaFile );

      final SchemaProblem exception =
        expectThrows( SchemaProblem.class, () -> schemaRepository.getSchema( components ) );

      final List<GraphQLError> errors = exception.getErrors();

      assertEquals( errors.size(), 1 );
      final GraphQLError error = errors.iterator().next();
      assertEquals( error.getMessage(), "The field type 'Date' is not present when resolving type 'Person' [@7:1]" );
      assertEquals( error.getErrorType(), ErrorType.ValidationError );
      final List<SourceLocation> locations = error.getLocations();
      assertEquals( locations.size(), 1 );
      final SourceLocation location = locations.get( 0 );
      assertEquals( location.getSourceName(), schemaFile.toString() );
      assertEquals( location.getLine(), 7 );
      assertEquals( location.getColumn(), 1 );
    } );
  }
}
