package com.example.allscalars;

import java.util.Map;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class MyRecursiveInput {
  @Nullable
  private final MyRecursiveInput child;

  @Nullable
  private final String v;

  private MyRecursiveInput(@Nullable final MyRecursiveInput child, @Nullable final String v) {
    this.child = child;
    this.v = v;
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static MyRecursiveInput from(@Nonnull final Map<String, Object> args) {
    final Map<String, Object> child = (Map<String, Object>) args.get( "child" );
    final String v = (String) args.get( "v" );
    return new MyRecursiveInput(MyRecursiveInput.maybeFrom( child ), v);
  }

  @Nullable
  public static MyRecursiveInput maybeFrom(@Nullable final Map<String, Object> args) {
    return null == args ? null : from( args );
  }

  @Nullable
  public MyRecursiveInput getChild() {
    return child;
  }

  @Nullable
  public String getV() {
    return v;
  }
}
