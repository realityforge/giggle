package org.realityforge.giggle.integration.scenarios.enumtype;

import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.Properties;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "deprecation" )
public class EnumTypeTest
{
  @Test
  public void scenario()
    throws Exception
  {
    assertNotNull( EventType.class.getField( EventType.RegenBurn.name() ).getAnnotation( Deprecated.class ) );
    assertTrue( Modifier.isPublic( EventType.class.getModifiers() ) );
    final InputStream types = EventType.class.getResourceAsStream( "types.mapping" );
    assertNotNull( types );
    final Properties properties = new Properties();
    properties.load( types );
    assertEquals( properties.size(), 1 );
    assertEquals( properties.get( "EventType" ), EventType.class.getName() );
  }
}
