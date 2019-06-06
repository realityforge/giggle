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
    final List<Map<String, Object>> child = (List<Map<String, Object>>) args.get( "child" );
    final String v = (String) args.get( "v" );
    return new MyRecursiveListInput(null == child ? null : child.stream().map( $e$ -> null == $e$ ? null : MyRecursiveInput.from( $e$ ) ).collect( Collectors.toList() ), v);
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
