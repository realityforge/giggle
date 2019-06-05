package com.example.basic;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("org.realityforge.giggle.Main")
public final class ObservationInput {
  @Nonnull
  private final String type;

  @Nonnull
  private final int value;

  public ObservationInput(@Nonnull final String type, @Nonnull final int value) {
    this.type = Objects.requireNonNull( type );
    this.value = Objects.requireNonNull( value );
  }

  @Nonnull
  public static ObservationInput from(@Nonnull final Map<String, Object> args) {
    final String type = (String) args.get( "type" );
    final int value = (Integer) args.get( "value" );
    return new ObservationInput(type, value);
  }

  @Nonnull
  public String getType() {
    return type;
  }

  @Nonnull
  public int getValue() {
    return value;
  }
}
