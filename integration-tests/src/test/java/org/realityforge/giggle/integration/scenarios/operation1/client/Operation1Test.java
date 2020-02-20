package org.realityforge.giggle.integration.scenarios.operation1.client;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.realityforge.giggle.integration.scenarios.AbstractScenarioTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class Operation1Test
  extends AbstractScenarioTest
{
  @Test
  public void structure()
  {
    assertTrue( Modifier.isPublic( SpecificEventResponse.class.getModifiers() ) );
    assertTrue( Modifier.isPublic( SpecificEventQuery.class.getModifiers() ) );
    assertTrue( Modifier.isPublic( SpecificEventQuery.Answer.class.getModifiers() ) );
  }

  @Test
  public void operationDocument()
  {
    assertEquals( new SpecificEventQuery.Question().getQuery(), "query {event(id:1) {id name type}}" );
  }

  @Test
  public void decodeFullResponse()
  {
    final String json =
      "{\n" +
      "  \"event\": {\n" +
      "    \"id\": \"22\",\n" +
      "    \"name\": \"Bob's Track\",\n" +
      "    \"type\": \"Wildfire\"\n" +
      "  }\n" +
      "}\n";
    final SpecificEventResponse response = fromJson( json, SpecificEventResponse.class );
    final SpecificEventResponse.Event event = response.getEvent();
    assertNotNull( event );
    assertEquals( event.getId(), "22" );
    assertEquals( event.getName(), "Bob's Track" );
    assertEquals( event.getType(), EventType.Wildfire );
  }

  @Test
  public void decodeResponseOmittingSomeNullables()
  {
    final String json =
      "{\n" +
      "  \"event\": {\n" +
      "    \"id\": \"22\"\n" +
      "  }\n" +
      "}\n";
    final SpecificEventResponse response = fromJson( json, SpecificEventResponse.class );
    final SpecificEventResponse.Event event = response.getEvent();
    assertNotNull( event );
    assertEquals( event.getId(), "22" );
    assertNull( event.getName() );
    assertNull( event.getType() );
  }

  @Test
  public void decodeResponseNullTopLevelField()
  {
    final String json =
      "{\n" +
      "  \"event\": null\n" +
      "}\n";
    final SpecificEventResponse response = fromJson( json, SpecificEventResponse.class );
    final SpecificEventResponse.Event event = response.getEvent();
    assertNull( event );
  }

  @Test
  public void decodeResponseEmptyData()
  {
    final SpecificEventResponse response = fromJson( "{}", SpecificEventResponse.class );
    final SpecificEventResponse.Event event = response.getEvent();
    assertNull( event );
  }

  @Test
  public void typeMapping()
    throws Exception
  {
    final Map<String, String> typeMapping = new HashMap<>();
    typeMapping.put( "EventType", EventType.class.getName() );
    assertTypeMapping( typeMapping );
  }

  @Test
  public void decodeAnswer()
  {
    final String json =
      "{\n" +
      "  \"data\": {\n" +
      "    \"event\": {\n" +
      "      \"id\": \"22\",\n" +
      "      \"name\": \"Bob's Track\",\n" +
      "      \"type\": \"Wildfire\"\n" +
      "    }\n" +
      "  }\n" +
      "}\n";
    final SpecificEventQuery.Answer answer = fromJson( json, SpecificEventQuery.Answer.class );
    assertTrue( answer.hasData() );
    assertFalse( answer.hasErrors() );
    final SpecificEventResponse.Event event = answer.getData().getEvent();
    assertNotNull( event );
    assertEquals( event.getId(), "22" );
    assertEquals( event.getName(), "Bob's Track" );
    assertEquals( event.getType(), EventType.Wildfire );
  }

  @Test
  public void decodeAnswerWithPartialError()
  {
    final String json =
      "{\n" +
      "  \"data\": {\n" +
      "    \"event\": {\n" +
      "      \"id\": \"22\",\n" +
      "      \"type\": \"Wildfire\"\n" +
      "    }\n" +
      "  },\n" +
      "  \"errors\": [\n" +
      "    {\n" +
      "      \"message\": \"Name for event with ID 22 could not be fetched.\",\n" +
      "      \"locations\": [ { \"line\": 6, \"column\": 7 } ],\n" +
      "      \"path\": [ \"event\", \"name\" ]\n" +
      "    }\n" +
      "  ]\n" +
      "}\n";
    final SpecificEventQuery.Answer answer = fromJson( json, SpecificEventQuery.Answer.class );
    assertTrue( answer.hasData() );
    assertTrue( answer.hasErrors() );
    final SpecificEventResponse.Event event = answer.getData().getEvent();
    assertNotNull( event );
    assertEquals( event.getId(), "22" );
    assertNull( event.getName() );
    assertEquals( event.getType(), EventType.Wildfire );
    final GraphQLError[] errors = answer.getErrors();
    assertNotNull( errors );
    assertEquals( errors.length, 1 );
    final GraphQLError error = errors[ 0 ];
    assertEquals( error.getMessage(), "Name for event with ID 22 could not be fetched." );
    final GraphQLError.Location[] locations = error.getLocations();
    assertNotNull( locations );
    assertEquals( locations.length, 1 );
    assertEquals( locations[ 0 ].getLine(), 6 );
    assertEquals( locations[ 0 ].getColumn(), 7 );
    final Object[] path = error.getPath();
    assertNotNull( path );
    assertEquals( Arrays.asList( path ), Arrays.asList( "event", "name" ) );
    assertNull( error.getExtensions() );
  }
}
