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

  private MyInput(@Nullable final MyRecursiveListInput data, @Nonnull final MyRecursiveInput other,
      @Nullable final String v) {
    this.data = data;
    this.other = Objects.requireNonNull( other );
    this.v = v;
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static MyInput from(@Nonnull final Map<String, Object> args) {
    final Map<String, Object> $giggle$_data = (Map<String, Object>) args.get( "$giggle$_data" );
    final Map<String, Object> $giggle$_other = (Map<String, Object>) args.get( "$giggle$_other" );
    final String $giggle$_v = (String) args.get( "$giggle$_v" );
    return new MyInput(MyRecursiveListInput.maybeFrom( $giggle$_data ), MyRecursiveInput.from( $giggle$_other ), $giggle$_v);
  }

  @Nullable
  public static MyInput maybeFrom(@Nullable final Map<String, Object> args) {
    return null == args ? null : from( args );
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
