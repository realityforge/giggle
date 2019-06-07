package com.example.coerceid;

import graphql.schema.CoercingParseValueException;
import graphql.schema.DataFetchingEnvironment;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("org.realityforge.giggle.Main")
public final class Query3Args {
  private final int requiredCoercedID;

  private Query3Args(final int requiredCoercedID) {
    this.requiredCoercedID = requiredCoercedID;
  }

  @Nonnull
  public static Query3Args from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final String $giggle$_requiredCoercedID = (String) args.get( "requiredCoercedID" );
    return coerceTrap( environment, () -> new Query3Args(coerceID( "$giggle$_requiredCoercedID", $giggle$_requiredCoercedID )) );
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

  public int getRequiredCoercedID() {
    return requiredCoercedID;
  }
}
