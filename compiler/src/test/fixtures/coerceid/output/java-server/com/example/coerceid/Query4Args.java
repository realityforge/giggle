package com.example.coerceid;

import graphql.schema.CoercingParseValueException;
import graphql.schema.DataFetchingEnvironment;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class Query4Args {
  @Nullable
  private final Integer optionalCoercedID;

  private Query4Args(@Nullable final Integer optionalCoercedID) {
    this.optionalCoercedID = optionalCoercedID;
  }

  @Nonnull
  public static Query4Args from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final String $giggle$_optionalCoercedID = (String) args.get( "optionalCoercedID" );
    return coerceTrap( environment, () -> new Query4Args(maybeCoerceID( "optionalCoercedID", $giggle$_optionalCoercedID )) );
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
  public Integer getOptionalCoercedID() {
    return optionalCoercedID;
  }
}
