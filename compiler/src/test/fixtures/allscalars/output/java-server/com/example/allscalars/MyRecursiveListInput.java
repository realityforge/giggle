package com.example.allscalars;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class MyRecursiveListInput {
  @Nullable
  private final String v;

  @Nullable
  private final List<MyRecursiveInput> child;

  private MyRecursiveListInput(@Nullable final String v,
      @Nullable final List<MyRecursiveInput> child) {
    this.v = v;
    this.child = child;
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static MyRecursiveListInput from(@Nonnull final Map<String, Object> args) {
    final String $giggle$_v = (String) args.get( "v" );
    final List<Map<String, Object>> $giggle$_child = (List<Map<String, Object>>) args.get( "child" );
    return new MyRecursiveListInput($giggle$_v, null == $giggle$_child ? null : $giggle$_child.stream().map( MyRecursiveInput::maybeFrom ).collect( Collectors.toList() ));
  }

  @Nullable
  public static MyRecursiveListInput maybeFrom(@Nullable final Map<String, Object> args) {
    return null == args ? null : from( args );
  }

  @Nullable
  public String getV() {
    return v;
  }

  @Nullable
  public List<MyRecursiveInput> getChild() {
    return child;
  }

  @Override
  public boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( !( o instanceof MyRecursiveListInput ) ) {
      return false;
    } else {
      final MyRecursiveListInput that = (MyRecursiveListInput) o;
      return Objects.equals( v, that.v ) && Objects.equals( child, that.child );
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash( v, child );
  }

  @Override
  public String toString() {
    return "MyRecursiveListInput[v=" + v + ", child=" + child + "]";
  }
}
