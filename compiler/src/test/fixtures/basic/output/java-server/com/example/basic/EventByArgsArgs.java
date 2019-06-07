package com.example.basic;

import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("org.realityforge.giggle.Main")
public final class EventByArgsArgs {
  @Nonnull
  private final List<String> args;

  private EventByArgsArgs(@Nonnull final List<String> args) {
    this.args = Objects.requireNonNull( args );
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static EventByArgsArgs from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final List<String> $giggle$_args = (List<String>) args.get( "args" );
    return new EventByArgsArgs($giggle$_args);
  }

  @Nonnull
  public List<String> getArgs() {
    return args;
  }
}
