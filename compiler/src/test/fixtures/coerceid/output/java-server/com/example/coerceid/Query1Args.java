package com.example.coerceid;

import graphql.schema.DataFetchingEnvironment;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("org.realityforge.giggle.Main")
public final class Query1Args {
  @Nonnull
  private final String requiredID;

  private Query1Args(@Nonnull final String requiredID) {
    this.requiredID = Objects.requireNonNull( requiredID );
  }

  @Nonnull
  public static Query1Args from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final String $giggle$_requiredID = (String) args.get( "requiredID" );
    return new Query1Args($giggle$_requiredID);
  }

  @Nonnull
  public String getRequiredID() {
    return requiredID;
  }
}
