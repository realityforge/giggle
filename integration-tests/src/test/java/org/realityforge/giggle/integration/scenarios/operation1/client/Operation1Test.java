package org.realityforge.giggle.integration.scenarios.operation1.client;

import java.lang.reflect.Modifier;
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
  }

  @Test
  public void operationDocument()
    throws Exception
  {
    assertEquals( getResourceAsString( "specificEvent.graphql" ), "query specificEvent {event(id:1) {id name type}}" );
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
}
