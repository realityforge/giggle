package org.realityforge.giggle.integration.scenarios.inputtype.client;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.realityforge.giggle.integration.scenarios.AbstractScenarioTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class InputTypeTest
  extends AbstractScenarioTest
{
  @Test
  public void structure()
  {
    assertTrue( Modifier.isPublic( ObservationInput.class.getModifiers() ) );
    assertTrue( Modifier.isPublic( ObservationsInput.class.getModifiers() ) );
  }

  @Test
  public void ObservationInput()
  {
    final ObservationInput input = new ObservationInput( "Temp", 23 );
    assertEquals( input.getType(), "Temp" );
    assertEquals( input.getValue(), 23 );
    assertEquals( input.toString(), "ObservationInput[type=Temp, value=23]" );
    assertEquals( toJson( input ), "{\"type\":\"Temp\",\"value\":23}" );
  }

  @Test
  public void ObservationInputs()
  {
    final ObservationsInput input =
      new ObservationsInput( "Ballarat",
                             Arrays.asList( new ObservationInput( "Temp", 23 ),
                                            new ObservationInput( "Windspeed", 14 ) ) );

    assertEquals( input.getName(), "Ballarat" );
    final List<ObservationInput> observations = input.getObservations();
    assertEquals( observations.size(), 2 );
    assertEquals( observations.get( 0 ).getType(), "Temp" );
    assertEquals( observations.get( 0 ).getValue(), 23 );
    assertEquals( observations.get( 1 ).getType(), "Windspeed" );
    assertEquals( observations.get( 1 ).getValue(), 14 );
    assertEquals( input.toString(),
                  "ObservationsInput[name=Ballarat, observations=[ObservationInput[type=Temp, value=23], ObservationInput[type=Windspeed, value=14]]]" );
    assertEquals( toJson( input ),
                  "{\"name\":\"Ballarat\",\"observations\":[{\"type\":\"Temp\",\"value\":23},{\"type\":\"Windspeed\",\"value\":14}]}" );
  }

  @Test
  public void typeMapping()
    throws Exception
  {
    final Map<String, String> typeMapping = new HashMap<>();
    typeMapping.put( "ObservationsInput", ObservationsInput.class.getName() );
    typeMapping.put( "ObservationInput", ObservationInput.class.getName() );
    assertTypeMapping( typeMapping );
  }

  @Test
  public void ObservationInput_hashCode_And_Equals()
  {
    final ObservationInput input1 = new ObservationInput( "Temp", 23 );
    final ObservationInput input2 = new ObservationInput( "Temp", 23 );
    final ObservationInput input3 = new ObservationInput( "Temp", 999999 );

    assertEquals( input1, input1 );
    assertEquals( input1, input1 );
    assertEquals( input1, input2 );
    assertNotEquals( input1, input3 );
    assertEquals( input2, input1 );
    assertEquals( input2, input2 );
    assertNotEquals( input2, input3 );
    assertNotEquals( input3, input1 );
    assertNotEquals( input3, input2 );
    assertEquals( input3, input3 );

    assertEquals( input1.hashCode(), input1.hashCode() );
    assertEquals( input1.hashCode(), input1.hashCode() );
    assertEquals( input1.hashCode(), input2.hashCode() );
    assertNotEquals( input1.hashCode(), input3.hashCode() );
    assertEquals( input2.hashCode(), input1.hashCode() );
    assertEquals( input2.hashCode(), input2.hashCode() );
    assertNotEquals( input2.hashCode(), input3.hashCode() );
    assertNotEquals( input3.hashCode(), input1.hashCode() );
    assertNotEquals( input3.hashCode(), input2.hashCode() );
    assertEquals( input3.hashCode(), input3.hashCode() );
  }

  @Test
  public void ObservationInputs_hashCode_And_Equals()
  {
    final ObservationsInput input1 =
      new ObservationsInput( "Ballarat",
                             Arrays.asList( new ObservationInput( "Temp", 23 ),
                                            new ObservationInput( "Windspeed", 14 ) ) );
    final ObservationsInput input2 =
      new ObservationsInput( "Ballarat",
                             Arrays.asList( new ObservationInput( "Temp", 23 ),
                                            new ObservationInput( "Windspeed", 14 ) ) );
    final ObservationsInput input3 = new ObservationsInput( "Ballarat", Collections.emptyList() );

    assertEquals( input1, input1 );
    assertEquals( input1, input1 );
    assertEquals( input1, input2 );
    assertNotEquals( input1, input3 );
    assertEquals( input2, input1 );
    assertEquals( input2, input2 );
    assertNotEquals( input2, input3 );
    assertNotEquals( input3, input1 );
    assertNotEquals( input3, input2 );
    assertEquals( input3, input3 );

    assertEquals( input1.hashCode(), input1.hashCode() );
    assertEquals( input1.hashCode(), input1.hashCode() );
    assertEquals( input1.hashCode(), input2.hashCode() );
    assertNotEquals( input1.hashCode(), input3.hashCode() );
    assertEquals( input2.hashCode(), input1.hashCode() );
    assertEquals( input2.hashCode(), input2.hashCode() );
    assertNotEquals( input2.hashCode(), input3.hashCode() );
    assertNotEquals( input3.hashCode(), input1.hashCode() );
    assertNotEquals( input3.hashCode(), input2.hashCode() );
    assertEquals( input3.hashCode(), input3.hashCode() );
  }
}
