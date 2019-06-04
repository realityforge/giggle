package org.realityforge.giggle.integration.java.server;

import java.util.Arrays;
import org.realityforge.giggle.integration.AbstractIntegrationTest;
import org.realityforge.giggle.integration.CompileResults;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class EnumTest
  extends AbstractIntegrationTest
{
  @Test
  public void enumType()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final CompileResults results =
        compileFragment( "java-server",
                         "enum EventState {\n" +
                         "  GOING\n" +
                         "  SAFE\n" +
                         "}\n" );
      final String name = "EventState";
      final Class<?> type = toJavaClass( results, name );
      assertTrue( type.isEnum() );
      final Object[] constants = type.getEnumConstants();
      assertNotNull( constants );
      assertEquals( constants.length, 2 );
      assertTrue( Arrays.stream( constants ).anyMatch( c -> c.toString().equals( "GOING" ) ) );
      assertTrue( Arrays.stream( constants ).anyMatch( c -> c.toString().equals( "SAFE" ) ) );
      assertEquals( toJavaFile( results, name ),
                    "package com.example;\n" +
                    "\n" +
                    "public enum EventState {\n" +
                    "  GOING,\n" +
                    "\n" +
                    "  SAFE\n" +
                    "}\n" );
    } );
  }

  @Test
  public void enumWithDeprecation()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final CompileResults results =
        compileFragment( "java-server",
                         "enum EventType {\n" +
                         "  Burn\n" +
                         "  Wildfire\n" +
                         "  RegenBurn @deprecated(reason: \"Use `Burn`.\")\n" +
                         "}\n" );
      final String eventType = "EventType";
      final Class<?> type = toJavaClass( results, eventType );
      assertTrue( type.isEnum() );
      final Object[] constants = type.getEnumConstants();
      assertNotNull( constants );
      assertEquals( constants.length, 3 );
      assertTrue( Arrays.stream( constants ).anyMatch( c -> c.toString().equals( "Burn" ) ) );
      assertTrue( Arrays.stream( constants ).anyMatch( c -> c.toString().equals( "Wildfire" ) ) );
      assertTrue( Arrays.stream( constants ).anyMatch( c -> c.toString().equals( "RegenBurn" ) ) );
      assertNotNull( type.getField( "RegenBurn" ).getAnnotation( Deprecated.class ) );

      assertEquals( toJavaFile( results, eventType ),
                    "package com.example;\n" +
                    "\n" +
                    "public enum EventType {\n" +
                    "  Burn,\n" +
                    "\n" +
                    "  /**\n" +
                    "   * @deprecated Use `Burn`.\n" +
                    "   */\n" +
                    "  @Deprecated\n" +
                    "  RegenBurn,\n" +
                    "\n" +
                    "  Wildfire\n" +
                    "}\n" );
    } );
  }

  @Test
  public void enumWithDescription()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final CompileResults results =
        compileFragment( "java-server",
                         "\"\"\"Classification of Event\"\"\"\n" +
                         "enum EventType {\n" +
                         "  \"\"\"\n" +
                         "    A wildfire set intentionally for purposes of forest management, farming, prairie restoration or greenhouse gas abatement.\n" +
                         "  \"\"\"\n" +
                         "  Burn\n" +
                         "  Wildfire\n" +
                         "  RegenBurn @deprecated(reason: \"Use `Burn`.\")\n" +
                         "}\n" );

      assertEquals( toJavaFile( results, "EventType" ),
                    "package com.example;\n" +
                    "\n" +
                    "/**\n" +
                    " * Classification of Event\n" +
                    " */\n" +
                    "public enum EventType {\n" +
                    "  /**\n" +
                    "   * A wildfire set intentionally for purposes of forest management, farming, prairie restoration or greenhouse gas abatement.\n" +
                    "   */\n" +
                    "  Burn,\n" +
                    "\n" +
                    "  /**\n" +
                    "   * @deprecated Use `Burn`.\n" +
                    "   */\n" +
                    "  @Deprecated\n" +
                    "  RegenBurn,\n" +
                    "\n" +
                    "  Wildfire\n" +
                    "}\n" );
    } );
  }
}
