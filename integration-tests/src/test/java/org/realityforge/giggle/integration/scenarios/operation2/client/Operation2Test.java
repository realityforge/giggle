package org.realityforge.giggle.integration.scenarios.operation2.client;

import org.realityforge.giggle.integration.scenarios.AbstractScenarioTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class Operation2Test
  extends AbstractScenarioTest
{
  @Test
  public void operationDocument()
    throws Exception
  {
    assertEquals( getResourceAsString( "specificEvent.graphql" ),
                  "fragment EventName on Event {name} query specificEvent {event(id:1) {id ...EventName}}" );
    assertEquals( getResourceAsString( "otherEvent.graphql" ),
                  "fragment EventName on Event {name} fragment EventType on Event {type} query otherEvent {event(id:2) {id ...EventName ...EventType}}" );
  }

  @Test
  public void decode_SpecificEventResponse()
  {
    final String json =
      "{\n" +
      "  \"event\": {\n" +
      "    \"id\": \"22\",\n" +
      "    \"name\": \"Bob's Track\"\n" +
      "  }\n" +
      "}\n";
    final SpecificEventResponse response = fromJson( json, SpecificEventResponse.class );
    final SpecificEventResponse.Event event = response.getEvent();
    assertNotNull( event );
    assertEquals( event.getId(), "22" );
    assertEquals( event.getName(), "Bob's Track" );
  }

  @Test
  public void decode_OtherEventResponse()
  {
    final String json =
      "{\n" +
      "  \"event\": {\n" +
      "    \"id\": \"22\",\n" +
      "    \"name\": \"Bob's Track\",\n" +
      "    \"type\": \"Wildfire\"\n" +
      "  }\n" +
      "}\n";
    final OtherEventResponse response = fromJson( json, OtherEventResponse.class );
    final OtherEventResponse.Event event = response.getEvent();
    assertNotNull( event );
    assertEquals( event.getId(), "22" );
    assertEquals( event.getName(), "Bob's Track" );
    assertEquals( event.getType(), EventType.Wildfire );
  }
}
