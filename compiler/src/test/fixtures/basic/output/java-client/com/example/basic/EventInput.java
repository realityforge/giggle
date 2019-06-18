package com.example.basic;

import java.util.Date;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class EventInput {
  @Nonnull
  private final String name;

  @Nonnull
  private final EventType type;

  @Nullable
  private final Date startedAt;

  public EventInput(@Nonnull final String name, @Nonnull final EventType type,
      @Nullable final Date startedAt) {
    this.name = Objects.requireNonNull( name );
    this.type = Objects.requireNonNull( type );
    this.startedAt = startedAt;
  }

  @Nonnull
  public String getName() {
    return name;
  }

  @Nonnull
  public EventType getType() {
    return type;
  }

  @Nullable
  public Date getStartedAt() {
    return startedAt;
  }

  @Override
  public boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( !( o instanceof EventInput ) ) {
      return false;
    } else {
      final EventInput that = (EventInput) o;
      return Objects.equals( name, that.name ) && Objects.equals( type, that.type ) && Objects.equals( startedAt, that.startedAt );
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash( name, type, startedAt );
  }

  @Override
  public String toString() {
    return "EventInput[name=" + name + ", type=" + type + ", startedAt=" + startedAt + "]";
  }
}
