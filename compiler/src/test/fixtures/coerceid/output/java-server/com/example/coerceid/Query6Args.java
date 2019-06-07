package com.example.coerceid;

import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("org.realityforge.giggle.Main")
public final class Query6Args {
  @Nonnull
  private final List<String> requiredIDListContainingNulls;

  private Query6Args(@Nonnull final List<String> requiredIDListContainingNulls) {
    this.requiredIDListContainingNulls = Objects.requireNonNull( requiredIDListContainingNulls );
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static Query6Args from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final List<String> $giggle$_requiredIDListContainingNulls = (List<String>) args.get( "requiredIDListContainingNulls" );
    return new Query6Args($giggle$_requiredIDListContainingNulls);
  }

  @Nonnull
  public List<String> getRequiredIDListContainingNulls() {
    return requiredIDListContainingNulls;
  }
}
