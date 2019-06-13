package com.example.allscalars;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class MyInput {
  @Nullable
  private final String v;

  @Nonnull
  private final MyRecursiveInput other;

  @Nullable
  private final MyRecursiveListInput data;

  private MyInput(@Nullable final String v, @Nonnull final MyRecursiveInput other,
      @Nullable final MyRecursiveListInput data) {
    this.v = v;
    this.other = Objects.requireNonNull( other );
    this.data = data;
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static MyInput from(@Nonnull final Map<String, Object> args) {
    final String $giggle$_v = (String) args.get( "v" );
    final Map<String, Object> $giggle$_other = (Map<String, Object>) args.get( "other" );
    final Map<String, Object> $giggle$_data = (Map<String, Object>) args.get( "data" );
    return new MyInput($giggle$_v, MyRecursiveInput.from( $giggle$_other ), MyRecursiveListInput.maybeFrom( $giggle$_data ));
  }

  @Nullable
  public static MyInput maybeFrom(@Nullable final Map<String, Object> args) {
    return null == args ? null : from( args );
  }

  @Nullable
  public String getV() {
    return v;
  }

  @Nonnull
  public MyRecursiveInput getOther() {
    return other;
  }

  @Nullable
  public MyRecursiveListInput getData() {
    return data;
  }

  @Override
  public boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( !( o instanceof MyInput ) ) {
      return false;
    } else {
      final MyInput that = (MyInput) o;
      return Objects.equals( v, that.v ) && Objects.equals( other, that.other ) && Objects.equals( data, that.data );
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash( v, other, data );
  }

  @Override
  public String toString() {
    return "MyInput[v=" + v + ", other=" + other + ", data=" + data + "]";
  }
}
