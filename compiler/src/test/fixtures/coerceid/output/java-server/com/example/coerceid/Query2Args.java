package com.example.coerceid;

import graphql.schema.DataFetchingEnvironment;
import java.util.Map;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class Query2Args {
  @Nullable
  private final String optionalID;

  private Query2Args(@Nullable final String optionalID) {
    this.optionalID = optionalID;
  }

  @Nonnull
  public static Query2Args from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final String $giggle$_optionalID = (String) args.get( "optionalID" );
    return new Query2Args($giggle$_optionalID);
  }

  @Nullable
  public String getOptionalID() {
    return optionalID;
  }
}
