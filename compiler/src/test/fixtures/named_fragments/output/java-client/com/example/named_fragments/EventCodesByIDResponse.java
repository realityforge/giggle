package com.example.named_fragments;

import javax.annotation.Generated;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class EventCodesByIDResponse {
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
    @Nullable
    private String code;

    @Nullable
    private String name;

    @Nullable
    public String getCode() {
      return code;
    }

    public void setCode(@Nullable final String code) {
      this.code = code;
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
