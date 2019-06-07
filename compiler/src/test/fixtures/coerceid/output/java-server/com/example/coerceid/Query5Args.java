package com.example.coerceid;

import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("org.realityforge.giggle.Main")
public final class Query5Args {
  @Nonnull
  private final List<String> requiredIDList;

  private Query5Args(@Nonnull final List<String> requiredIDList) {
    this.requiredIDList = Objects.requireNonNull( requiredIDList );
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static Query5Args from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final List<String> $giggle$_requiredIDList = (List<String>) args.get( "requiredIDList" );
    return new Query5Args($giggle$_requiredIDList);
  }

  @Nonnull
  public List<String> getRequiredIDList() {
    return requiredIDList;
  }
}
