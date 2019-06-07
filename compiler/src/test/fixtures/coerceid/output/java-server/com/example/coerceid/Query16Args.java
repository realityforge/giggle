package com.example.coerceid;

import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("org.realityforge.giggle.Main")
public final class Query16Args {
  @Nonnull
  private final List<MyInput> requiredInputListContainingNulls;

  private Query16Args(@Nonnull final List<MyInput> requiredInputListContainingNulls) {
    this.requiredInputListContainingNulls = Objects.requireNonNull( requiredInputListContainingNulls );
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static Query16Args from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final List<Map<String, Object>> $giggle$_requiredInputListContainingNulls = (List<Map<String, Object>>) args.get( "requiredInputListContainingNulls" );
    return new Query16Args($giggle$_requiredInputListContainingNulls.stream().map( MyInput::maybeFrom ).collect( Collectors.toList() ));
  }

  @Nonnull
  public List<MyInput> getRequiredInputListContainingNulls() {
    return requiredInputListContainingNulls;
  }
}
