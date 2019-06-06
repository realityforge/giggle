package org.realityforge.giggle.integration.scenarios.enumtype;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import org.realityforge.giggle.integration.scenarios.AbstractScenarioTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class EnumTypeTest
  extends AbstractScenarioTest
{
  @Test
  public void structure()
  {
    assertTrue( Modifier.isPublic( EventType.class.getModifiers() ) );
  }

  @SuppressWarnings( "deprecation" )
  @Test
  public void deprecatedEnumValue()
    throws Exception
  {
    assertNotNull( EventType.class.getField( EventType.RegenBurn.name() ).getAnnotation( Deprecated.class ) );
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
