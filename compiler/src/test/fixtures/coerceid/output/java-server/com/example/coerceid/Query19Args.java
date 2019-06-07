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

@Generated("org.realityforge.giggle.Main")
public final class Query19Args {
  @Nonnull
  private final List<MyCoercableInput> requiredCoercedInputListContainingNulls;

  private Query19Args(
      @Nonnull final List<MyCoercableInput> requiredCoercedInputListContainingNulls) {
    this.requiredCoercedInputListContainingNulls = Objects.requireNonNull( requiredCoercedInputListContainingNulls );
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static Query19Args from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final List<Map<String, Object>> $giggle$_requiredCoercedInputListContainingNulls = (List<Map<String, Object>>) args.get( "requiredCoercedInputListContainingNulls" );
    return coerceTrap( environment, () -> new Query19Args($giggle$_requiredCoercedInputListContainingNulls.stream().map( MyCoercableInput::maybeFrom ).collect( Collectors.toList() )) );
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
  public List<MyCoercableInput> getRequiredCoercedInputListContainingNulls() {
    return requiredCoercedInputListContainingNulls;
  }
}
