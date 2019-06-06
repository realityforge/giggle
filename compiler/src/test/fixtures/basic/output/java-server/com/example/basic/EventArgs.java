package com.example.basic;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("org.realityforge.giggle.Main")
public final class EventArgs {
  @Nonnull
  private final String id;

  private EventArgs(@Nonnull final String id) {
    this.id = Objects.requireNonNull( id );
  }

  @Nonnull
  public static EventArgs from(@Nonnull final Map<String, Object> args) {
    final String $giggle$_id = (String) args.get( "id" );
    return new EventArgs($giggle$_id);
  }

  @Nonnull
  public String getId() {
    return id;
  }
}
