package org.realityforge.giggle.integration.scenarios.operation2.client;

import org.realityforge.giggle.integration.scenarios.AbstractScenarioTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class Operation2Test
  extends AbstractScenarioTest
{
  @Test
  public void operationDocument()
  {
    assertEquals( new SpecificEventQuery.Question().getQuery(), "query {event(id:1) {id name}}" );
    assertEquals( new OtherEventQuery.Question().getQuery(), "query {event(id:2) {id name type}}" );
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
