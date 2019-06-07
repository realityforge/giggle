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
public final class Query18Args {
  @Nonnull
  private final List<MyCoercableInput> requiredCoercedInputList;

  private Query18Args(@Nonnull final List<MyCoercableInput> requiredCoercedInputList) {
    this.requiredCoercedInputList = Objects.requireNonNull( requiredCoercedInputList );
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static Query18Args from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final List<Map<String, Object>> $giggle$_requiredCoercedInputList = (List<Map<String, Object>>) args.get( "requiredCoercedInputList" );
    return coerceTrap( environment, () -> new Query18Args($giggle$_requiredCoercedInputList.stream().map( MyCoercableInput::from ).collect( Collectors.toList() )) );
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
  public List<MyCoercableInput> getRequiredCoercedInputList() {
    return requiredCoercedInputList;
  }
}
