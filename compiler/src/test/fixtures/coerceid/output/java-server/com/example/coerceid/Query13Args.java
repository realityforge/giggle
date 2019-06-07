package com.example.coerceid;

import graphql.schema.CoercingParseValueException;
import graphql.schema.DataFetchingEnvironment;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("org.realityforge.giggle.Main")
public final class Query13Args {
  @Nonnull
  private final MyCoercableInput requiredCoercedInput;

  private Query13Args(@Nonnull final MyCoercableInput requiredCoercedInput) {
    this.requiredCoercedInput = Objects.requireNonNull( requiredCoercedInput );
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static Query13Args from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final Map<String, Object> $giggle$_requiredCoercedInput = (Map<String, Object>) args.get( "requiredCoercedInput" );
    return coerceTrap( environment, () -> new Query13Args(MyCoercableInput.from( $giggle$_requiredCoercedInput )) );
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
  public MyCoercableInput getRequiredCoercedInput() {
    return requiredCoercedInput;
  }
}
