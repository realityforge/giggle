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
                  "query specificEvent {event(id:1) {id ...EventName}} fragment EventName on Event {name}" );
    assertEquals( getResourceAsString( "otherEvent.graphql" ),
                  "query otherEvent {event(id:2) {id ...EventName ...EventType}} fragment EventName on Event {name} fragment EventType on Event {type}" );
  }

  @Test
  public void decode_SpecificEventQueryResponse()
  {
    final String json =
      "{\n" +
      "  \"event\": {\n" +
      "    \"id\": \"22\",\n" +
      "    \"name\": \"Bob's Track\"\n" +
      "  }\n" +
      "}\n";
    final SpecificEventQueryResponse response = fromJson( json, SpecificEventQueryResponse.class );
    final SpecificEventQueryResponse.Event event = response.getEvent();
    assertNotNull( event );
    assertEquals( event.getId(), "22" );
    assertEquals( event.getName(), "Bob's Track" );
  }

  @Test
  public void decode_OtherEventQueryResponse()
  {
    final String json =
      "{\n" +
      "  \"event\": {\n" +
      "    \"id\": \"22\",\n" +
      "    \"name\": \"Bob's Track\",\n" +
      "    \"type\": \"Wildfire\"\n" +
      "  }\n" +
      "}\n";
    final OtherEventQueryResponse response = fromJson( json, OtherEventQueryResponse.class );
    final OtherEventQueryResponse.Event event = response.getEvent();
    assertNotNull( event );
    assertEquals( event.getId(), "22" );
    assertEquals( event.getName(), "Bob's Track" );
    assertEquals( event.getType(), EventType.Wildfire );
  }
}
