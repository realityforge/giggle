package com.example.coerceid;

import graphql.schema.DataFetchingEnvironment;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("org.realityforge.giggle.Main")
public final class Query11Args {
  @Nonnull
  private final MyInput requiredInput;

  private Query11Args(@Nonnull final MyInput requiredInput) {
    this.requiredInput = Objects.requireNonNull( requiredInput );
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static Query11Args from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final Map<String, Object> $giggle$_requiredInput = (Map<String, Object>) args.get( "requiredInput" );
    return new Query11Args(MyInput.from( $giggle$_requiredInput ));
  }

  @Nonnull
  public MyInput getRequiredInput() {
    return requiredInput;
  }
}
