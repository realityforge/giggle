package org.realityforge.giggle.integration.scenarios.inputtype3;

import graphql.schema.CoercingParseValueException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.realityforge.giggle.integration.scenarios.AbstractScenarioTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class InputType3Test
  extends AbstractScenarioTest
{
  @Test
  public void ObservationInput_from()
  {
    final HashMap<String, Object> args = new HashMap<>();
    args.put( "oid", "47" );
    args.put( "type", "Temp" );
    args.put( "value", 23 );
    final ObservationInput input = ObservationInput.from( args );
    assertEquals( input.getOid(), 47 );
    assertEquals( input.getType(), "Temp" );
    assertEquals( input.getValue(), 23 );
    assertEquals( input.toString(), "ObservationInput[oid=47, type=Temp, value=23]" );
  }

  @Test
  public void ObservationInput_bad_idCoerce()
  {
    final HashMap<String, Object> args = new HashMap<>();
    args.put( "oid", "abc" );
    args.put( "type", "Temp" );
    args.put( "value", 23 );
    final CoercingParseValueException exception =
      expectThrows( CoercingParseValueException.class, () -> ObservationInput.from( args ) );
    assertEquals( exception.getMessage(), "Failed to parse input field $giggle$_oid that was expected to be a numeric ID type. Actual value = 'abc'" );
  }

  @Test
  public void ObservationInputs_from()
  {
    final HashMap<String, Object> input1Args = new HashMap<>();
    input1Args.put( "oid", "47" );
    input1Args.put( "type", "Temp" );
    input1Args.put( "value", 23 );
    final HashMap<String, Object> input2Args = new HashMap<>();
    input2Args.put( "oid", "52" );
    input2Args.put( "type", "Windspeed" );
    input2Args.put( "value", 14 );
    final HashMap<String, Object> args = new HashMap<>();
    args.put( "id", "42" );
    args.put( "name", "Ballarat" );
    args.put( "observations", Arrays.asList( input1Args, input2Args ) );
    final ObservationsInput input = ObservationsInput.from( args );
    assertEquals( input.getId(), 42 );
    assertEquals( input.getName(), "Ballarat" );
    final List<ObservationInput> observations = input.getObservations();
    assertEquals( observations.size(), 2 );
    assertEquals( observations.get( 0 ).getOid(), 47 );
    assertEquals( observations.get( 0 ).getType(), "Temp" );
    assertEquals( observations.get( 0 ).getValue(), 23 );
    assertEquals( observations.get( 1 ).getOid(), 52 );
    assertEquals( observations.get( 1 ).getType(), "Windspeed" );
    assertEquals( observations.get( 1 ).getValue(), 14 );
    assertEquals( input.toString(),
                  "ObservationsInput[id=42, name=Ballarat, observations=[ObservationInput[oid=47, type=Temp, value=23], ObservationInput[oid=52, type=Windspeed, value=14]]]" );
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
