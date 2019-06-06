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
    final String $giggle$_type = (String) args.get( "type" );
    final int $giggle$_value = (Integer) args.get( "value" );
    return new ObservationInput($giggle$_type, $giggle$_value);
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

  @Override
  public final boolean equals(final Object o) {
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
  public final int hashCode() {
    return Objects.hash( type, value );
  }
}
