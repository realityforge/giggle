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

  public MyRecursiveListInput(@Nullable final List<MyRecursiveInput> child,
      @Nullable final String v) {
    this.child = child;
    this.v = v;
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static MyRecursiveListInput from(@Nonnull final Map<String, Object> args) {
    final List<Map<String, Object>> child = (List<Map<String, Object>>) args.get( "child" );
    final String v = (String) args.get( "v" );
    return new MyRecursiveListInput(child.stream().map( $element$ -> MyRecursiveInput.from( $element$ ) ).collect( Collectors.toList() ), v);
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
