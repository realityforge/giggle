package com.example.basic;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class CreateEventArgs {
  @Nullable
  private final String clientMutationId;

  @Nonnull
  private final EventInput event;

  public CreateEventArgs(@Nullable final String clientMutationId, @Nonnull final EventInput event) {
    this.clientMutationId = clientMutationId;
    this.event = Objects.requireNonNull( event );
  }

  @Nonnull
  public static CreateEventArgs from(@Nonnull final Map<String, Object> args) {
    final String clientMutationId = (String) args.get( "clientMutationId" );
    final Map<String, Object> event = (Map<String, Object>) args.get( "event" );
    return new CreateEventArgs(clientMutationId, EventInput.from( event ));
  }

  @Nullable
  public String getClientMutationId() {
    return clientMutationId;
  }

  @Nonnull
  public EventInput getEvent() {
    return event;
  }
}
