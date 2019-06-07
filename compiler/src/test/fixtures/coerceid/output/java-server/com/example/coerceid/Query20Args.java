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
public final class Query20Args {
  @Nullable
  private final List<MyCoercableInput> optionalCoercedInputList;

  private Query20Args(@Nullable final List<MyCoercableInput> optionalCoercedInputList) {
    this.optionalCoercedInputList = optionalCoercedInputList;
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static Query20Args from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final List<Map<String, Object>> $giggle$_optionalCoercedInputList = (List<Map<String, Object>>) args.get( "optionalCoercedInputList" );
    return coerceTrap( environment, () -> new Query20Args(null == $giggle$_optionalCoercedInputList ? null : $giggle$_optionalCoercedInputList.stream().map( MyCoercableInput::maybeFrom ).collect( Collectors.toList() )) );
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
  public List<MyCoercableInput> getOptionalCoercedInputList() {
    return optionalCoercedInputList;
  }
}
