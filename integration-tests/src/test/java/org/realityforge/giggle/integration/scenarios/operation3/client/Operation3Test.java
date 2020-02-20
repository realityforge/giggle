package org.realityforge.giggle.integration.scenarios.operation3.client;

import org.realityforge.giggle.integration.scenarios.AbstractScenarioTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class Operation3Test
  extends AbstractScenarioTest
{
  @Test
  public void operationDocument()
  {
    assertEquals( new EventQuery.Question( "X" ).getQuery(), "query q($id:ID!) {event(id:$id) {id name}}" );
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
    final EventResponse response = fromJson( json, EventResponse.class );
    final EventResponse.Event event = response.getEvent();
    assertNotNull( event );
    assertEquals( event.getId(), "22" );
    assertEquals( event.getName(), "Bob's Track" );
  }

  @Test
  public void EventQuery_Variables()
  {
    final EventQuery.Question question = new EventQuery.Question( "73" );
    assertEquals( question.getQuery(), "query q($id:ID!) {event(id:$id) {id name}}" );
    final EventQuery.Variables variables = question.getVariables();
    assertEquals( variables.getId(), "73" );
  }
}
