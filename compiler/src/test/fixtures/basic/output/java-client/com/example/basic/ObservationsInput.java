package com.example.basic;

import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("org.realityforge.giggle.Main")
public final class ObservationsInput {
  @Nonnull
  private final String name;

  @Nonnull
  private final List<ObservationInput> type;

  public ObservationsInput(@Nonnull final String name, @Nonnull final List<ObservationInput> type) {
    this.name = Objects.requireNonNull( name );
    this.type = Objects.requireNonNull( type );
  }

  @Nonnull
  public String getName() {
    return name;
  }

  @Nonnull
  public List<ObservationInput> getType() {
    return type;
  }

  @Override
  public boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( !( o instanceof ObservationsInput ) ) {
      return false;
    } else {
      final ObservationsInput that = (ObservationsInput) o;
      return Objects.equals( name, that.name ) && Objects.equals( type, that.type );
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash( name, type );
  }

  @Override
  public String toString() {
    return "ObservationsInput[name=" + name + ", type=" + type + "]";
  }
}
