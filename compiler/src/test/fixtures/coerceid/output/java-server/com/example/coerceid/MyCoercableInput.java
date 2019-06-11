package com.example.coerceid;

import graphql.schema.CoercingParseValueException;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class MyCoercableInput {
  private final int id;

  @Nullable
  private final Integer otherId;

  private MyCoercableInput(final int id, @Nullable final Integer otherId) {
    this.id = Objects.requireNonNull( id );
    this.otherId = otherId;
  }

  @Nonnull
  public static MyCoercableInput from(@Nonnull final Map<String, Object> args) {
    final String $giggle$_id = (String) args.get( "id" );
    final String $giggle$_otherId = (String) args.get( "otherId" );
    return new MyCoercableInput(coerceID( "id", $giggle$_id ), maybeCoerceID( "otherId", $giggle$_otherId ));
  }

  @Nullable
  public static MyCoercableInput maybeFrom(@Nullable final Map<String, Object> args) {
    return null == args ? null : from( args );
  }

  @Nonnull
  private static Integer coerceID(@Nonnull final String name, @Nonnull final String value) {
    try {
      return Integer.decode( value );
    } catch ( final NumberFormatException e ) {
      throw new CoercingParseValueException( "Failed to parse input field '" + name + "' that was expected to be a numeric ID type. Actual value = '" + value + "'" );
    }
  }

  @Nullable
  private static Integer maybeCoerceID(@Nonnull final String name, @Nullable final String value) {
    return null == value ? null : coerceID( name, value );
  }

  public int getId() {
    return id;
  }

  @Nullable
  public Integer getOtherId() {
    return otherId;
  }

  @Override
  public final boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( !( o instanceof MyCoercableInput ) ) {
      return false;
    } else {
      final MyCoercableInput that = (MyCoercableInput) o;
      return Objects.equals( id, that.id ) && Objects.equals( otherId, that.otherId );
    }
  }

  @Override
  public final int hashCode() {
    return Objects.hash( id, otherId );
  }

  @Override
  public final String toString() {
    return "MyCoercableInput[id=" + id + ", otherId=" + otherId + "]";
  }
}
