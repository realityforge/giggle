package com.example.allscalars;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class MyInput {
  @Nullable
  private final MyRecursiveListInput data;

  @Nonnull
  private final MyRecursiveInput other;

  @Nullable
  private final String v;

  public MyInput(@Nullable final MyRecursiveListInput data, @Nonnull final MyRecursiveInput other,
      @Nullable final String v) {
    this.data = data;
    this.other = Objects.requireNonNull( other );
    this.v = v;
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static MyInput from(@Nonnull final Map<String, Object> args) {
    final Map<String, Object> data = (Map<String, Object>) args.get( "data" );
    final Map<String, Object> other = (Map<String, Object>) args.get( "other" );
    final String v = (String) args.get( "v" );
    return new MyInput(MyRecursiveListInput.from( data ), MyRecursiveInput.from( other ), v);
  }

  @Nullable
  public MyRecursiveListInput getData() {
    return data;
  }

  @Nonnull
  public MyRecursiveInput getOther() {
    return other;
  }

  @Nullable
  public String getV() {
    return v;
  }
}
