package com.example.coerceid;

import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("org.realityforge.giggle.Main")
public final class Query15Args {
  @Nonnull
  private final List<MyInput> requiredInputList;

  private Query15Args(@Nonnull final List<MyInput> requiredInputList) {
    this.requiredInputList = Objects.requireNonNull( requiredInputList );
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static Query15Args from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final List<Map<String, Object>> $giggle$_requiredInputList = (List<Map<String, Object>>) args.get( "requiredInputList" );
    return new Query15Args($giggle$_requiredInputList.stream().map( MyInput::from ).collect( Collectors.toList() ));
  }

  @Nonnull
  public List<MyInput> getRequiredInputList() {
    return requiredInputList;
  }
}
