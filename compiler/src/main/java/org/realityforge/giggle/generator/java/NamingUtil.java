package org.realityforge.giggle.generator.java;

import javax.annotation.Nonnull;

public final class NamingUtil
{
  private NamingUtil()
  {
  }

  @Nonnull
  public static String lowercaseFirstCharacter( @Nonnull final String name )
  {
    return Character.toLowerCase( name.charAt( 0 ) ) + ( name.length() > 1 ? name.substring( 1 ) : "" );
  }

  @Nonnull
  public static String uppercaseFirstCharacter( @Nonnull final String name )
  {
    return Character.toUpperCase( name.charAt( 0 ) ) + ( name.length() > 1 ? name.substring( 1 ) : "" );
  }
}
