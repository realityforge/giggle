package org.realityforge.giggle.integration.scenarios.enumtype;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import org.realityforge.giggle.integration.scenarios.AbstractScenarioTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "deprecation" )
public class EnumTypeTest
  extends AbstractScenarioTest
{
  @Test
  public void scenario()
    throws Exception
  {
    assertNotNull( EventType.class.getField( EventType.RegenBurn.name() ).getAnnotation( Deprecated.class ) );
    assertTrue( Modifier.isPublic( EventType.class.getModifiers() ) );
    final Map<String, String> typeMapping = new HashMap<>();
    typeMapping.put( "EventType", EventType.class.getName() );
    assertTypeMapping( typeMapping );
  }
}
