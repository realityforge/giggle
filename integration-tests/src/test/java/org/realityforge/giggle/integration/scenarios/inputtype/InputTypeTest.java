package org.realityforge.giggle.integration.scenarios.inputtype;

import java.lang.reflect.Modifier;
import java.util.Arrays;
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
}
