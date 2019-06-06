package com.example.typemapping;

import com.example.typemapping.model.EventInput;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
    final Map<String, Object> $giggle$_event = (Map<String, Object>) args.get( "event" );
    final Map<String, Object> $giggle$_resource = (Map<String, Object>) args.get( "resource" );
    return new DeploymentInput(EventInput.from( $giggle$_event ), ResourceInput.from( $giggle$_resource ));
  }

  @Nullable
  public static DeploymentInput maybeFrom(@Nullable final Map<String, Object> args) {
    return null == args ? null : from( args );
  }

  @Nonnull
  public EventInput getEvent() {
    return event;
  }

  @Nonnull
  public ResourceInput getResource() {
    return resource;
  }

  @Override
  public final boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( !( o instanceof DeploymentInput ) ) {
      return false;
    } else {
      final DeploymentInput that = (DeploymentInput) o;
      return Objects.equals( event, that.event ) && Objects.equals( resource, that.resource );
    }
  }
}
