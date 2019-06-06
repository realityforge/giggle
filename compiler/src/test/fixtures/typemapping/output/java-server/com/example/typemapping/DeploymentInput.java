package com.example.typemapping;

import com.example.typemapping.model.EventInput;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("org.realityforge.giggle.Main")
public final class DeploymentInput {
  @Nonnull
  private final EventInput event;

  @Nonnull
  private final ResourceInput resource;

  private DeploymentInput(@Nonnull final EventInput event, @Nonnull final ResourceInput resource) {
    this.event = Objects.requireNonNull( event );
    this.resource = Objects.requireNonNull( resource );
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static DeploymentInput from(@Nonnull final Map<String, Object> args) {
    final Map<String, Object> event = (Map<String, Object>) args.get( "event" );
    final Map<String, Object> resource = (Map<String, Object>) args.get( "resource" );
    return new DeploymentInput(EventInput.from( event ), ResourceInput.from( resource ));
  }

  @Nonnull
  public EventInput getEvent() {
    return event;
  }

  @Nonnull
  public ResourceInput getResource() {
    return resource;
  }
}
