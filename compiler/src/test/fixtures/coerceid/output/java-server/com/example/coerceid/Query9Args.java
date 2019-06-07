package com.example.coerceid;

import graphql.schema.CoercingParseValueException;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class Query9Args {
  @Nonnull
  private final List<Integer> requiredCoercedIDListContainingNulls;

  private Query9Args(@Nonnull final List<Integer> requiredCoercedIDListContainingNulls) {
    this.requiredCoercedIDListContainingNulls = Objects.requireNonNull( requiredCoercedIDListContainingNulls );
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static Query9Args from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final List<String> $giggle$_requiredCoercedIDListContainingNulls = (List<String>) args.get( "requiredCoercedIDListContainingNulls" );
    return coerceTrap( environment, () -> new Query9Args($giggle$_requiredCoercedIDListContainingNulls.stream().map( v -> maybeCoerceID( "$giggle$_requiredCoercedIDListContainingNulls", v ) ).collect( Collectors.toList() )) );
  }

  @Nonnull
  private static <T> T coerceTrap(@Nonnull final DataFetchingEnvironment environment,
      @Nonnull final Supplier<T> supplier) {
    try {
      return supplier.get();
    } catch ( final CoercingParseValueException e ) {
      throw new CoercingParseValueException( e.getMessage(), e.getCause(), environment.getMergedField().getSingleField().getSourceLocation() );
    }
  }

  @Nonnull
  private static Integer coerceID(@Nonnull final String name, @Nonnull final String value) {
    try {
      return Integer.decode( value );
    } catch ( final NumberFormatException e ) {
      throw new CoercingParseValueException( "Failed to parse argument " + name + " that was expected to be a numeric ID type. Actual value = '" + value + "'" );
    }
  }

  @Nullable
  private static Integer maybeCoerceID(@Nonnull final String name, @Nullable final String value) {
    return null == value ? null : coerceID( name, value );
  }

  @Nonnull
  public List<Integer> getRequiredCoercedIDListContainingNulls() {
    return requiredCoercedIDListContainingNulls;
  }
}
