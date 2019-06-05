package org.realityforge.giggle.generator.java;

import org.realityforge.giggle.AbstractTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class NamingUtilTest
  extends AbstractTest
{
  @Test
  public void uppercaseFirstCharacter()
  {
    assertEquals( NamingUtil.uppercaseFirstCharacter( "hello" ), "Hello" );
    assertEquals( NamingUtil.uppercaseFirstCharacter( "Hi" ), "Hi" );
  }
}
