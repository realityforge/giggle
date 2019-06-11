package com.example.typemapping;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
    final String $giggle$_name = (String) args.get( "name" );
    final ResourceType $giggle$_type = (ResourceType) args.get( "type" );
    return new ResourceInput($giggle$_name, $giggle$_type);
  }

  @Nullable
  public static ResourceInput maybeFrom(@Nullable final Map<String, Object> args) {
    return null == args ? null : from( args );
  }

  @Nonnull
  public String getName() {
    return name;
  }

  @Nonnull
  public ResourceType getType() {
    return type;
  }

  @Override
  public boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( !( o instanceof ResourceInput ) ) {
      return false;
    } else {
      final ResourceInput that = (ResourceInput) o;
      return Objects.equals( name, that.name ) && Objects.equals( type, that.type );
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash( name, type );
  }

  @Override
  public String toString() {
    return "ResourceInput[name=" + name + ", type=" + type + "]";
  }
}
