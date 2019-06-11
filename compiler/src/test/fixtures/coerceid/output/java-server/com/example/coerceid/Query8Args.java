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
public final class Query8Args {
  @Nonnull
  private final List<Integer> requiredCoercedIDList;

  private Query8Args(@Nonnull final List<Integer> requiredCoercedIDList) {
    this.requiredCoercedIDList = Objects.requireNonNull( requiredCoercedIDList );
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static Query8Args from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final List<String> $giggle$_requiredCoercedIDList = (List<String>) args.get( "requiredCoercedIDList" );
    return coerceTrap( environment, () -> new Query8Args($giggle$_requiredCoercedIDList.stream().map( v -> coerceID( "requiredCoercedIDList", v ) ).collect( Collectors.toList() )) );
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

  @Nonnull
  public List<Integer> getRequiredCoercedIDList() {
    return requiredCoercedIDList;
  }
}
