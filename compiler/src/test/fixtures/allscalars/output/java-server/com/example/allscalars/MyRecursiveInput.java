package com.example.allscalars;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class MyRecursiveInput {
  @Nullable
  private final String v;

  @Nullable
  private final MyRecursiveInput child;

  private MyRecursiveInput(@Nullable final String v, @Nullable final MyRecursiveInput child) {
    this.v = v;
    this.child = child;
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static MyRecursiveInput from(@Nonnull final Map<String, Object> args) {
    final String $giggle$_v = (String) args.get( "v" );
    final Map<String, Object> $giggle$_child = (Map<String, Object>) args.get( "child" );
    return new MyRecursiveInput($giggle$_v, MyRecursiveInput.maybeFrom( $giggle$_child ));
  }

  @Nullable
  public static MyRecursiveInput maybeFrom(@Nullable final Map<String, Object> args) {
    return null == args ? null : from( args );
  }

  @Nullable
  public String getV() {
    return v;
  }

  @Nullable
  public MyRecursiveInput getChild() {
    return child;
  }

  @Override
  public boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( !( o instanceof MyRecursiveInput ) ) {
      return false;
    } else {
      final MyRecursiveInput that = (MyRecursiveInput) o;
      return Objects.equals( v, that.v ) && Objects.equals( child, that.child );
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash( v, child );
  }

  @Override
  public String toString() {
    return "MyRecursiveInput[v=" + v + ", child=" + child + "]";
  }
}
