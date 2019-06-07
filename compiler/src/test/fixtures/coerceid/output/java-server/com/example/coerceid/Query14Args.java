package com.example.coerceid;

import graphql.schema.CoercingParseValueException;
import graphql.schema.DataFetchingEnvironment;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class Query14Args {
  @Nullable
  private final MyCoercableInput optionalCoercedInput;

  private Query14Args(@Nullable final MyCoercableInput optionalCoercedInput) {
    this.optionalCoercedInput = optionalCoercedInput;
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static Query14Args from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final Map<String, Object> $giggle$_optionalCoercedInput = (Map<String, Object>) args.get( "optionalCoercedInput" );
    return coerceTrap( environment, () -> new Query14Args(MyCoercableInput.maybeFrom( $giggle$_optionalCoercedInput )) );
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

  @Nullable
  public MyCoercableInput getOptionalCoercedInput() {
    return optionalCoercedInput;
  }
}
