package org.realityforge.giggle.integration.scenarios;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import static org.testng.Assert.*;

public abstract class AbstractScenarioTest
{
  @SuppressWarnings( "unchecked" )
  protected final void assertTypeMapping( final Map<String, String> expectedTypeMapping )
    throws IOException
  {
    final InputStream types = getClass().getResourceAsStream( "types.mapping" );
    assertNotNull( types );
    final Properties properties = new Properties();
    properties.load( types );
    assertEquals( new HashMap<>( (Map) properties ), expectedTypeMapping );
  }
}
