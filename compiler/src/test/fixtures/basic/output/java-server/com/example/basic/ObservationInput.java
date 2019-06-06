package com.example.basic;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class ObservationInput {
  @Nonnull
  private final String type;

  private final int value;

  private ObservationInput(@Nonnull final String type, final int value) {
    this.type = Objects.requireNonNull( type );
    this.value = Objects.requireNonNull( value );
  }

  @Nonnull
  public static ObservationInput from(@Nonnull final Map<String, Object> args) {
    final String type = (String) args.get( "type" );
    final int value = (Integer) args.get( "value" );
    return new ObservationInput(type, value);
  }

  @Nullable
  public static ObservationInput maybeFrom(@Nullable final Map<String, Object> args) {
    return null == args ? null : from( args );
  }

  @Nonnull
  public String getType() {
    return type;
  }

  public int getValue() {
    return value;
  }
}
