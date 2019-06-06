package com.example.basic;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class EventInput {
  @Nonnull
  private final String name;

  @Nullable
  private final Date startedAt;

  @Nonnull
  private final EventType type;

  private EventInput(@Nonnull final String name, @Nullable final Date startedAt,
      @Nonnull final EventType type) {
    this.name = Objects.requireNonNull( name );
    this.startedAt = startedAt;
    this.type = Objects.requireNonNull( type );
  }

  @Nonnull
  public static EventInput from(@Nonnull final Map<String, Object> args) {
    final String $giggle$_name = (String) args.get( "name" );
    final Date $giggle$_startedAt = (Date) args.get( "startedAt" );
    final EventType $giggle$_type = (EventType) args.get( "type" );
    return new EventInput($giggle$_name, $giggle$_startedAt, $giggle$_type);
  }

  @Nullable
  public static EventInput maybeFrom(@Nullable final Map<String, Object> args) {
    return null == args ? null : from( args );
  }

  @Nonnull
  public String getName() {
    return name;
  }

  @Nullable
  public Date getStartedAt() {
    return startedAt;
  }

  @Nonnull
  public EventType getType() {
    return type;
  }

  @Override
  public final boolean equals(final Object o) {
    if ( !( o instanceof EventInput ) ) {
      return false;
    }
    final EventInput that = (EventInput) o;
    if ( !Objects.equals( name, that.name ) ) {
      return false;
    }
    if ( !Objects.equals( startedAt, that.startedAt ) ) {
      return false;
    }
    if ( !Objects.equals( type, that.type ) ) {
      return false;
    }
    return true;
  }
}
