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
  private Event2 event2;

  @Nullable
  public Event getEvent() {
    return event;
  }

  public void setEvent(@Nullable final Event event) {
    this.event = event;
  }

  @Nullable
  public Event2 getEvent2() {
    return event2;
  }

  public void setEvent2(@Nullable final Event2 event2) {
    this.event2 = event2;
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
    private LocalDate startedAt2;

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
    public LocalDate getStartedAt2() {
      return startedAt2;
    }

    public void setStartedAt2(@Nullable final LocalDate startedAt2) {
      this.startedAt2 = startedAt2;
    }

    @Nullable
    public LocalDateTime getReportedAt() {
      return reportedAt;
    }

    public void setReportedAt(@Nullable final LocalDateTime reportedAt) {
      this.reportedAt = reportedAt;
    }
  }

  public static final class Event2 {
    @Nonnull
    private String id;

    @Nonnull
    public String getId() {
      return id;
    }

    public void setId(@Nonnull final String id) {
      this.id = id;
    }
  }
}
