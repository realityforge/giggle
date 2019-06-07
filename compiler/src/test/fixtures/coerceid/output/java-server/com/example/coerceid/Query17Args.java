package com.example.coerceid;

import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class Query17Args {
  @Nullable
  private final List<MyInput> optionalInputList;

  private Query17Args(@Nullable final List<MyInput> optionalInputList) {
    this.optionalInputList = optionalInputList;
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static Query17Args from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final List<Map<String, Object>> $giggle$_optionalInputList = (List<Map<String, Object>>) args.get( "optionalInputList" );
    return new Query17Args(null == $giggle$_optionalInputList ? null : $giggle$_optionalInputList.stream().map( MyInput::maybeFrom ).collect( Collectors.toList() ));
  }

  @Nullable
  public List<MyInput> getOptionalInputList() {
    return optionalInputList;
  }
}
