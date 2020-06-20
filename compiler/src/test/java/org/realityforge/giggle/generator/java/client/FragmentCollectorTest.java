package org.realityforge.giggle.generator.java.client;

import graphql.language.Document;
import graphql.language.FragmentDefinition;
import graphql.language.OperationDefinition;
import graphql.schema.GraphQLSchema;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.realityforge.giggle.AbstractTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class FragmentCollectorTest
  extends AbstractTest
{
  @Test
  public void collectFragments_noFragments()
    throws Exception
  {
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

    final List<FragmentDefinition> fragmentDefinitions = collectFragments( document, "myQuery" );

    assertEquals( fragmentDefinitions.size(), 0 );
  }

  @Test
  public void collectFragments_singleFragment()
    throws Exception
  {
    final GraphQLSchema schema =
      buildGraphQLSchema( "type Person {\n" +
                          "  name: String\n" +
                          "}\n" +
                          "extend type Query {\n" +
                          "  person: Person" +
                          "}\n" );

    final Document document =
      buildDocument( schema,
                     "fragment Nameable on Person { name }\n" +
                     "query aQuery {\n" +
                     "  person {\n" +
                     "    ...Nameable\n" +
                     "  }\n" +
                     "}\n" );

    final List<FragmentDefinition> fragmentDefinitions = collectFragments( document, "aQuery" );

    assertEquals( fragmentDefinitions.size(), 1 );
    assertEquals( fragmentDefinitions.get( 0 ).getName(), "Nameable" );
  }

  @Test
  public void collectFragments_multipleFragments()
    throws Exception
  {
    final GraphQLSchema schema =
      buildGraphQLSchema( "type Person {\n" +
                          "  name: String\n" +
                          "  age: Int\n" +
                          "}\n" +
                          "extend type Query {\n" +
                          "  person: Person" +
                          "}\n" );

    final Document document =
      buildDocument( schema,
                     "fragment Nameable on Person { name }\n" +
                     "fragment Aging on Person { age }\n" +
                     "query aQuery {\n" +
                     "  person {\n" +
                     "    ...Nameable\n" +
                     "    ...Aging\n" +
                     "  }\n" +
                     "}\n" );

    final List<FragmentDefinition> fragmentDefinitions = collectFragments( document, "aQuery" );
    assertEquals( fragmentDefinitions.stream()
                    .map( FragmentDefinition::getName )
                    .sorted()
                    .collect( Collectors.toList() ),
                  Arrays.asList( "Aging", "Nameable" ) );
  }

  @Test
  public void collectFragments_nestedFragments()
    throws Exception
  {
    final GraphQLSchema schema =
      buildGraphQLSchema( "type Person {\n" +
                          "  id: ID!\n" +
                          "  name: String\n" +
                          "  age: Int\n" +
                          "}\n" +
                          "extend type Query {\n" +
                          "  person: Person" +
                          "}\n" );

    final Document document =
      buildDocument( schema,
                     "fragment Nameable on Person { name }\n" +
                     "fragment Personish on Person { ...Nameable age }\n" +
                     "query aQuery {\n" +
                     "  person {\n" +
                     "    id\n" +
                     "    ...Personish\n" +
                     "  }\n" +
                     "}\n" );

    final List<FragmentDefinition> fragmentDefinitions = collectFragments( document, "aQuery" );
    assertEquals( fragmentDefinitions.stream()
                    .map( FragmentDefinition::getName )
                    .sorted()
                    .collect( Collectors.toList() ),
                  Arrays.asList( "Nameable", "Personish" ) );
  }

  @Test
  public void collectFragments_fragmentNestedInType()
    throws Exception
  {
    final GraphQLSchema schema =
      buildGraphQLSchema( "type Agency {\n" +
                          "  id: ID!\n" +
                          "  name: String\n" +
                          "}\n" +
                          "type Person {\n" +
                          "  id: ID!\n" +
                          "  name: String\n" +
                          "  agency: Agency\n" +
                          "}\n" +
                          "extend type Query {\n" +
                          "  person: Person" +
                          "}\n" );

    final Document document =
      buildDocument( schema,
                     "fragment Nameable on Agency { name }\n" +
                     "query aQuery {\n" +
                     "  person {\n" +
                     "    id\n" +
                     "    agency {\n" +
                     "      ...Nameable\n" +
                     "    }\n" +
                     "  }\n" +
                     "}\n" );

    final List<FragmentDefinition> fragmentDefinitions = collectFragments( document, "aQuery" );
    assertEquals( fragmentDefinitions.stream()
                    .map( FragmentDefinition::getName )
                    .sorted()
                    .collect( Collectors.toList() ),
                  Collections.singletonList( "Nameable" ) );
  }

  @Test
  public void collectFragments_fragmentNestedInInlineFragment()
    throws Exception
  {
    final GraphQLSchema schema =
      buildGraphQLSchema( "type Agency {\n" +
                          "  id: ID!\n" +
                          "  name: String\n" +
                          "}\n" +
                          "type Person {\n" +
                          "  id: ID!\n" +
                          "  name: String\n" +
                          "  agency: Agency\n" +
                          "}\n" +
                          "extend type Query {\n" +
                          "  person: Person" +
                          "}\n" );

    final Document document =
      buildDocument( schema,
                     "fragment Nameable on Agency { name }\n" +
                     "query aQuery {\n" +
                     "  person {\n" +
                     "    id\n" +
                     "    ... @include(if: true) {\n" +
                     "      agency {\n" +
                     "        ...Nameable\n" +
                     "      }\n" +
                     "    }\n" +
                     "  }\n" +
                     "}\n" );

    final List<FragmentDefinition> fragmentDefinitions = collectFragments( document, "aQuery" );
    assertEquals( fragmentDefinitions.stream()
                    .map( FragmentDefinition::getName )
                    .sorted()
                    .collect( Collectors.toList() ),
                  Collections.singletonList( "Nameable" ) );
  }

  @Nonnull
  private List<FragmentDefinition> collectFragments( @Nonnull final Document document,
                                                     @Nonnull final String operationName )
  {
    final OperationDefinition operation = document.getDefinitions()
      .stream()
      .filter( d -> d instanceof OperationDefinition )
      .map( d -> (OperationDefinition) d )
      .filter( d -> d.getName().equals( operationName ) )
      .findAny()
      .orElseThrow( AssertionError::new );

    return new FragmentCollector( document ).collectFragments( operation.getSelectionSet() );
  }
}
