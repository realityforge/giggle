package com.example.named_fragments;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class SpecificEventQueryResponse {
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
  }
}
