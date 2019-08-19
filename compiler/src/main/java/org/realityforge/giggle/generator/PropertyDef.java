package org.realityforge.giggle.generator;

import java.util.Objects;
import javax.annotation.Nonnull;

public final class PropertyDef
{
  @Nonnull
  private final String _key;
  private final boolean _required;
  @Nonnull
  private final String _description;

  public PropertyDef( @Nonnull final String key, final boolean required, @Nonnull final String description )
  {
    _key = Objects.requireNonNull( key );
    _required = required;
    _description = Objects.requireNonNull( description );
  }

  @Nonnull
  public String getKey()
  {
    return _key;
  }

  public boolean isRequired()
  {
    return _required;
  }

  @Nonnull
  public String getDescription()
  {
    return _description;
  }
}
