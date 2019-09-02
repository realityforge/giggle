package com.example.basic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
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
  private final LocalDate startedAt;

  @Nullable
  private final LocalDateTime reportedAt;

  private EventInput(@Nonnull final String name, @Nonnull final EventType type,
      @Nullable final LocalDate startedAt, @Nullable final LocalDateTime reportedAt) {
    this.name = Objects.requireNonNull( name );
    this.type = Objects.requireNonNull( type );
    this.startedAt = startedAt;
    this.reportedAt = reportedAt;
  }

  @Nonnull
  public static EventInput from(@Nonnull final Map<String, Object> args) {
    final String $giggle$_name = (String) args.get( "name" );
    final EventType $giggle$_type = (EventType) args.get( "type" );
    final LocalDate $giggle$_startedAt = (LocalDate) args.get( "startedAt" );
    final LocalDateTime $giggle$_reportedAt = (LocalDateTime) args.get( "reportedAt" );
    return new EventInput($giggle$_name, $giggle$_type, $giggle$_startedAt, $giggle$_reportedAt);
  }

  @Nullable
  public static EventInput maybeFrom(@Nullable final Map<String, Object> args) {
    return null == args ? null : from( args );
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
  public LocalDate getStartedAt() {
    return startedAt;
  }

  @Nullable
  public LocalDateTime getReportedAt() {
    return reportedAt;
  }

  @Override
  public boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( !( o instanceof EventInput ) ) {
      return false;
    } else {
      final EventInput that = (EventInput) o;
      return Objects.equals( name, that.name ) && Objects.equals( type, that.type ) && Objects.equals( startedAt, that.startedAt ) && Objects.equals( reportedAt, that.reportedAt );
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash( name, type, startedAt, reportedAt );
  }

  @Override
  public String toString() {
    return "EventInput[name=" + name + ", type=" + type + ", startedAt=" + startedAt + ", reportedAt=" + reportedAt + "]";
  }
}
