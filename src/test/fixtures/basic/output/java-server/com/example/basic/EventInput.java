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
  private String name;

  @Nullable
  private Date startedAt;

  @Nonnull
  private EventType type;

  public EventInput(@Nonnull final String name, @Nullable final Date startedAt,
      @Nonnull final EventType type) {
    this.name = Objects.requireNonNull( name );
    this.startedAt = startedAt;
    this.type = Objects.requireNonNull( type );
  }

  @Nonnull
  public static EventInput from(@Nonnull final Map<String, Object> args) {
    final String name = (String) args.get( "name" );
    final Date startedAt = (Date) args.get( "startedAt" );
    final EventType type = (EventType) args.get( "type" );
    return new EventInput(name, startedAt, type);
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
}
