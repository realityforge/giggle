package org.realityforge.giggle.generator.java;

import com.squareup.javapoet.TypeName;
import org.realityforge.giggle.AbstractTest;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ScalarTypeRegistryTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final ScalarTypeRegistry registry = new ScalarTypeRegistry();

    assertFalse( registry.isRegistered( "Velocity" ) );
    assertFalse( registry.isRegistered( ValueUtil.randomString() ) );

    assertThrows( () -> registry.getType( "Velocity" ) );

    registry.registerType( "Velocity", TypeName.FLOAT );
    assertFalse( registry.isRegistered( ValueUtil.randomString() ) );

    assertTrue( registry.isRegistered( "Velocity" ) );

    assertEquals( registry.getType( "Velocity" ), TypeName.FLOAT );
  }
}
