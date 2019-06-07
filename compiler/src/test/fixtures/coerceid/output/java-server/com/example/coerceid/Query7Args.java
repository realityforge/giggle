package com.example.coerceid;

import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class Query7Args {
  @Nullable
  private final List<String> optionalIDList;

  private Query7Args(@Nullable final List<String> optionalIDList) {
    this.optionalIDList = optionalIDList;
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static Query7Args from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final List<String> $giggle$_optionalIDList = (List<String>) args.get( "optionalIDList" );
    return new Query7Args($giggle$_optionalIDList);
  }

  @Nullable
  public List<String> getOptionalIDList() {
    return optionalIDList;
  }
}
