package org.realityforge.giggle.document;

import gir.io.FileUtil;
import graphql.language.Definition;
import graphql.language.Document;
import graphql.language.FragmentDefinition;
import graphql.language.OperationDefinition;
import graphql.schema.GraphQLSchema;
import graphql.validation.ValidationError;
import graphql.validation.ValidationErrorType;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.realityforge.giggle.AbstractTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DocumentRepositoryTest
  extends AbstractTest
{
  @Test
  public void getDocument_singleComponent_simpleQuery()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final GraphQLSchema schema =
        buildGraphQLSchema( "type Person {\n" +
                            "  name: String\n" +
                            "}\n" +
                            "extend type Query {\n" +
                            "  person: Person" +
                            "}\n" );

      final Document document =
        buildDocument( schema,
                       "query myQuery {\n" +
                       "  person {\n" +
                       "    name\n" +
                       "  }\n" +
                       "}\n" );

      final List<Definition> definitions = document.getDefinitions();
      assertEquals( definitions.size(), 1 );
      final Definition definition2 = definitions.get( 0 );
      assertTrue( definition2 instanceof OperationDefinition );
      final OperationDefinition operation = (OperationDefinition) definition2;
      assertEquals( operation.getName(), "myQuery" );
    } );
  }

  @Test
  public void getDocument_singleComponent_containsFragment()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final GraphQLSchema schema =
        buildGraphQLSchema( "type Person {\n" +
                            "  name: String\n" +
                            "}\n" +
                            "extend type Query {\n" +
                            "  person: Person" +
                            "}\n" );

      final Document document =
        buildDocument( schema, "fragment NameParts on Person {\n" +
                               "  name\n" +
                               "}\n" +
                               "query myQuery {\n" +
                               "  person {\n" +
                               "    ...NameParts\n" +
                               "  }\n" +
                               "}\n" );
      final List<Definition> definitions = document.getDefinitions();
      assertEquals( definitions.size(), 2 );
      final Definition definition = definitions.get( 0 );
      assertTrue( definition instanceof FragmentDefinition );
      final FragmentDefinition fragment = (FragmentDefinition) definition;
      assertEquals( fragment.getName(), "NameParts" );
      final Definition definition2 = definitions.get( 1 );
      assertTrue( definition2 instanceof OperationDefinition );
      final OperationDefinition operation = (OperationDefinition) definition2;
      assertEquals( operation.getName(), "myQuery" );
    } );
  }

  @Test
  public void getDocument_multipleComponent_containsFragment()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final GraphQLSchema schema =
        buildGraphQLSchema( "type Person {\n" +
                            "  name: String\n" +
                            "  age: Int\n" +
                            "}\n" +
                            "extend type Query {\n" +
                            "  person: Person" +
                            "}\n" );

      final Path documentFile1 =
        writeContent( "document1.graphql",
                      "fragment NameParts on Person {\n" +
                      "  name\n" +
                      "}\n" );
      final Path documentFile2 =
        writeContent( "document2.graphql",
                      "query myQuery {\n" +
                      "  person {\n" +
                      "    ...NameParts\n" +
                      "  }\n" +
                      "}\n" );

      final Document document =
        new DocumentRepository().getDocument( schema, Arrays.asList( documentFile1, documentFile2 ) );
      final List<Definition> definitions = document.getDefinitions();
      assertEquals( definitions.size(), 2 );
      final Definition definition = definitions.get( 0 );
      assertTrue( definition instanceof FragmentDefinition );
      final FragmentDefinition fragment = (FragmentDefinition) definition;
      assertEquals( fragment.getName(), "NameParts" );
      final Definition definition2 = definitions.get( 1 );
      assertTrue( definition2 instanceof OperationDefinition );
      final OperationDefinition operation = (OperationDefinition) definition2;
      assertEquals( operation.getName(), "myQuery" );
    } );
  }

  @Test
  public void getDocument_fileNotPresent()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final GraphQLSchema schema =
        buildGraphQLSchema( "type Person {\n" +
                            "  name: String\n" +
                            "}\n" +
                            "extend type Query {\n" +
                            "  person: Person" +
                            "}\n" );

      final Path documentFile = FileUtil.getCurrentDirectory().resolve( "document.graphql" );

      final DocumentReadException exception =
        expectThrows( DocumentReadException.class,
                      () -> new DocumentRepository().getDocument( schema, Collections.singletonList( documentFile ) ) );
      assertEquals( exception.getMessage(), "Error reading file " + documentFile );
    } );
  }

  @Test
  public void getDocument_documentInvalid()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final GraphQLSchema schema =
        buildGraphQLSchema( "type Person {\n" +
                            "  name: String\n" +
                            "}\n" +
                            "extend type Query {\n" +
                            "  person: Person" +
                            "}\n" );

      final Path documentFile =
        writeContent( "document.graphql",
                      "fragment NameParts on Person {\n" +
                      "  name\n" +
                      "}\n" );

      final DocumentValidateException exception =
        expectThrows( DocumentValidateException.class,
                      () -> new DocumentRepository().getDocument( schema, Collections.singletonList( documentFile ) ) );
      final List<ValidationError> errors = exception.getErrors();
      assertEquals( errors.size(), 1 );
      final ValidationError error = errors.get( 0 );
      assertEquals( error.getMessage(), "Validation error of type UnusedFragment: Unused fragment NameParts" );
    } );
  }

  @Test
  public void getDocument_documentInvalid_duplicateFragments()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final GraphQLSchema schema =
        buildGraphQLSchema( "type Person {\n" +
                            "  name: String\n" +
                            "}\n" +
                            "extend type Query {\n" +
                            "  person: Person" +
                            "}\n" );

      final Path documentFile =
        writeContent( "document.graphql",
                      "fragment NameParts on Person {\n" +
                      "  name\n" +
                      "}\n" +
                      "fragment NameParts on Person {\n" +
                      "  name\n" +
                      "}\n" +
                      "query myQuery {\n" +
                      "  person {\n" +
                      "    ...NameParts\n" +
                      "  }\n" +
                      "}\n" );

      final DocumentValidateException exception =
        expectThrows( DocumentValidateException.class,
                      () -> new DocumentRepository().getDocument( schema, Collections.singletonList( documentFile ) ) );
      final List<ValidationError> errors = exception.getErrors();
      assertEquals( errors.size(), 1 );
      final ValidationError error = errors.get( 0 );
      assertEquals( error.getMessage(),
                    "Validation error of type FragmentCycle: Multiple fragments defined with the name 'NameParts'" );
    } );
  }

  @Test
  public void getDocument_documentInvalid_anonymousOperation()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final GraphQLSchema schema =
        buildGraphQLSchema( "type Person {\n" +
                            "  name: String\n" +
                            "}\n" +
                            "extend type Query {\n" +
                            "  person: Person" +
                            "}\n" );

      final Path documentFile =
        writeContent( "document.graphql",
                      "{\n" +
                      "  person {\n" +
                      "    name\n" +
                      "  }\n" +
                      "}\n" );

      final DocumentValidateException exception =
        expectThrows( DocumentValidateException.class,
                      () -> new DocumentRepository().getDocument( schema, Collections.singletonList( documentFile ) ) );
      final List<ValidationError> errors = exception.getErrors();
      assertEquals( errors.size(), 1 );
      final ValidationError error = errors.get( 0 );
      assertEquals( error.getValidationErrorType(), ValidationErrorType.LoneAnonymousOperationViolation );
      assertEquals( error.getMessage(),
                    "Validation error of type LoneAnonymousOperationViolation: Giggle does not allow anonymous operations." );
    } );
  }
}
