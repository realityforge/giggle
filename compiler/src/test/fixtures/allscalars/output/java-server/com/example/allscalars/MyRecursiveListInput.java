package com.example.allscalars;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class MyRecursiveListInput {
  @Nullable
  private final List<MyRecursiveInput> child;

  @Nullable
  private final String v;

  private MyRecursiveListInput(@Nullable final List<MyRecursiveInput> child,
      @Nullable final String v) {
    this.child = child;
    this.v = v;
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static MyRecursiveListInput from(@Nonnull final Map<String, Object> args) {
    final List<Map<String, Object>> $giggle$_child = (List<Map<String, Object>>) args.get( "$giggle$_child" );
    final String $giggle$_v = (String) args.get( "$giggle$_v" );
    return new MyRecursiveListInput(null == $giggle$_child ? null : $giggle$_child.stream().map( MyRecursiveInput::maybeFrom ).collect( Collectors.toList() ), $giggle$_v);
  }

  @Nullable
  public static MyRecursiveListInput maybeFrom(@Nullable final Map<String, Object> args) {
    return null == args ? null : from( args );
  }

  @Nullable
  public List<MyRecursiveInput> getChild() {
    return child;
  }

  @Nullable
  public String getV() {
    return v;
  }
}
