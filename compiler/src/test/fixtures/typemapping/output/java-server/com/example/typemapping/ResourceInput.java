package com.example.typemapping;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("org.realityforge.giggle.Main")
public final class ResourceInput {
  @Nonnull
  private final String name;

  @Nonnull
  private final ResourceType type;

  private ResourceInput(@Nonnull final String name, @Nonnull final ResourceType type) {
    this.name = Objects.requireNonNull( name );
    this.type = Objects.requireNonNull( type );
  }

  @Nonnull
  public static ResourceInput from(@Nonnull final Map<String, Object> args) {
    final String name = (String) args.get( "name" );
    final ResourceType type = (ResourceType) args.get( "type" );
    return new ResourceInput(name, type);
  }

  @Nonnull
  public String getName() {
    return name;
  }

  @Nonnull
  public ResourceType getType() {
    return type;
  }
}
