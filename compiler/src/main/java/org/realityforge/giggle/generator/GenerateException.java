package org.realityforge.giggle.generator;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GenerateException
  extends RuntimeException
{
  @Nonnull
  private final String _name;

  public GenerateException( @Nonnull final String name, @Nonnull final String message, @Nullable final Throwable cause )
  {
    super( message, cause );
    _name = Objects.requireNonNull( name );
  }

  @Nonnull
  public String getName()
  {
    return _name;
  }
}
