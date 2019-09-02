package com.example.basic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class SpecificEventResponse {
  /**
   * Comment
   */
  @Nullable
  private Event event;

  @Nullable
  public Event getEvent() {
    return event;
  }

  public void setEvent(@Nullable final Event event) {
    this.event = event;
  }

  public static final class Event {
    @Nonnull
    private String id;

    @Nullable
    private String name;

    @Nullable
    private EventType type;

    @Nullable
    private LocalDate startedAt;

    @Nullable
    private LocalDateTime reportedAt;

    @Nonnull
    public String getId() {
      return id;
    }

    public void setId(@Nonnull final String id) {
      this.id = id;
    }

    @Nullable
    public String getName() {
      return name;
    }

    public void setName(@Nullable final String name) {
      this.name = name;
    }

    @Nullable
    public EventType getType() {
      return type;
    }

    public void setType(@Nullable final EventType type) {
      this.type = type;
    }

    @Nullable
    public LocalDate getStartedAt() {
      return startedAt;
    }

    public void setStartedAt(@Nullable final LocalDate startedAt) {
      this.startedAt = startedAt;
    }

    @Nullable
    public LocalDateTime getReportedAt() {
      return reportedAt;
    }

    public void setReportedAt(@Nullable final LocalDateTime reportedAt) {
      this.reportedAt = reportedAt;
    }
  }
}
