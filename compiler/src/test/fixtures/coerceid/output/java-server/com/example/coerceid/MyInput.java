package com.example.coerceid;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class MyInput {
  @Nonnull
  private final String id;

  @Nullable
  private final String otherId;

  private MyInput(@Nonnull final String id, @Nullable final String otherId) {
    this.id = Objects.requireNonNull( id );
    this.otherId = otherId;
  }

  @Nonnull
  public static MyInput from(@Nonnull final Map<String, Object> args) {
    final String $giggle$_id = (String) args.get( "id" );
    final String $giggle$_otherId = (String) args.get( "otherId" );
    return new MyInput($giggle$_id, $giggle$_otherId);
  }

  @Nullable
  public static MyInput maybeFrom(@Nullable final Map<String, Object> args) {
    return null == args ? null : from( args );
  }

  @Nonnull
  public String getId() {
    return id;
  }

  @Nullable
  public String getOtherId() {
    return otherId;
  }

  @Override
  public boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( !( o instanceof MyInput ) ) {
      return false;
    } else {
      final MyInput that = (MyInput) o;
      return Objects.equals( id, that.id ) && Objects.equals( otherId, that.otherId );
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash( id, otherId );
  }

  @Override
  public String toString() {
    return "MyInput[id=" + id + ", otherId=" + otherId + "]";
  }
}
