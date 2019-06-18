package com.example.basic;

import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("org.realityforge.giggle.Main")
public final class ObservationInput {
  @Nonnull
  private final String type;

  private final int value;

  public ObservationInput(@Nonnull final String type, final int value) {
    this.type = Objects.requireNonNull( type );
    this.value = Objects.requireNonNull( value );
  }

  @Nonnull
  public String getType() {
    return type;
  }

  public int getValue() {
    return value;
  }

  @Override
  public boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( !( o instanceof ObservationInput ) ) {
      return false;
    } else {
      final ObservationInput that = (ObservationInput) o;
      return Objects.equals( type, that.type ) && Objects.equals( value, that.value );
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash( type, value );
  }

  @Override
  public String toString() {
    return "ObservationInput[type=" + type + ", value=" + value + "]";
  }
}
