package org.realityforge.giggle.generator;

import java.util.Objects;
import javax.annotation.Nonnull;

public class NoSuchGeneratorException
  extends RuntimeException
{
  @Nonnull
  private final String _name;

  public NoSuchGeneratorException( @Nonnull final String name )
  {
    _name = Objects.requireNonNull( name );
  }

  @Nonnull
  public String getName()
  {
    return _name;
  }
}
