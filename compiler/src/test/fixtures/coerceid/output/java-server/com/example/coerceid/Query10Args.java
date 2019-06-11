package com.example.coerceid;

import graphql.schema.CoercingParseValueException;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class Query10Args {
  @Nullable
  private final List<Integer> optionalCoercedIDList;

  private Query10Args(@Nullable final List<Integer> optionalCoercedIDList) {
    this.optionalCoercedIDList = optionalCoercedIDList;
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static Query10Args from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final List<String> $giggle$_optionalCoercedIDList = (List<String>) args.get( "optionalCoercedIDList" );
    return coerceTrap( environment, () -> new Query10Args(null == $giggle$_optionalCoercedIDList ? null : $giggle$_optionalCoercedIDList.stream().map( v -> maybeCoerceID( "optionalCoercedIDList", v ) ).collect( Collectors.toList() )) );
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
      throw new CoercingParseValueException( "Failed to parse argument '" + name + "' that was expected to be a numeric ID type. Actual value = '" + value + "'" );
    }
  }

  @Nullable
  private static Integer maybeCoerceID(@Nonnull final String name, @Nullable final String value) {
    return null == value ? null : coerceID( name, value );
  }

  @Nullable
  public List<Integer> getOptionalCoercedIDList() {
    return optionalCoercedIDList;
  }
}
