package org.realityforge.giggle.integration.scenarios.inputtype;

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

  @Test( description = "Generated from with simple types" )
  public void ObservationInput_from()
  {
    final HashMap<String, Object> args = new HashMap<>();
    args.put( "type", "Temp" );
    args.put( "value", 23 );
    final ObservationInput input = ObservationInput.from( args );
    assertEquals( input.getType(), "Temp" );
    assertEquals( input.getValue(), 23 );
  }

  @Test( description = "Generated from with complex types" )
  public void ObservationInputs_from()
  {
    final HashMap<String, Object> input1Args = new HashMap<>();
    input1Args.put( "type", "Temp" );
    input1Args.put( "value", 23 );
    final HashMap<String, Object> input2Args = new HashMap<>();
    input2Args.put( "type", "Windspeed" );
    input2Args.put( "value", 14 );
    final HashMap<String, Object> args = new HashMap<>();
    args.put( "name", "Ballarat" );
    args.put( "observations", Arrays.asList( input1Args, input2Args ) );
    final ObservationsInput input = ObservationsInput.from( args );
    assertEquals( input.getName(), "Ballarat" );
    final List<ObservationInput> observations = input.getObservations();
    assertEquals( observations.size(), 2 );
    assertEquals( observations.get( 0 ).getType(), "Temp" );
    assertEquals( observations.get( 0 ).getValue(), 23 );
    assertEquals( observations.get( 1 ).getType(), "Windspeed" );
    assertEquals( observations.get( 1 ).getValue(), 14 );
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
    final HashMap<String, Object> args1 = new HashMap<>();
    args1.put( "type", "Temp" );
    args1.put( "value", 23 );
    final ObservationInput input1 = ObservationInput.from( args1 );
    final ObservationInput input2 = ObservationInput.from( args1 );
    final HashMap<String, Object> args3 = new HashMap<>();
    args3.put( "type", "Temp" );
    args3.put( "value", 999999 );
    final ObservationInput input3 = ObservationInput.from( args3 );

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

  public void ObservationInputs_hashCode_And_Equals()
  {
    final HashMap<String, Object> input1Args = new HashMap<>();
    input1Args.put( "type", "Temp" );
    input1Args.put( "value", 23 );
    final HashMap<String, Object> input2Args = new HashMap<>();
    input2Args.put( "type", "Windspeed" );
    input2Args.put( "value", 14 );
    final HashMap<String, Object> args1 = new HashMap<>();
    args1.put( "name", "Ballarat" );
    args1.put( "observations", Arrays.asList( input1Args, input2Args ) );
    final ObservationsInput input1 = ObservationsInput.from( args1 );
    final ObservationsInput input2 = ObservationsInput.from( args1 );
    final HashMap<String, Object> args3 = new HashMap<>();
    args3.put( "name", "Ballarat" );
    args3.put( "observations", Collections.emptyList() );
    final ObservationsInput input3 = ObservationsInput.from( args3 );

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
