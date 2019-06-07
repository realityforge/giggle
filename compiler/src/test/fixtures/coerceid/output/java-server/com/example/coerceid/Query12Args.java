package com.example.coerceid;

import graphql.schema.DataFetchingEnvironment;
import java.util.Map;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class Query12Args {
  @Nullable
  private final MyInput optionalInput;

  private Query12Args(@Nullable final MyInput optionalInput) {
    this.optionalInput = optionalInput;
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static Query12Args from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final Map<String, Object> $giggle$_optionalInput = (Map<String, Object>) args.get( "optionalInput" );
    return new Query12Args(MyInput.maybeFrom( $giggle$_optionalInput ));
  }

  @Nullable
  public MyInput getOptionalInput() {
    return optionalInput;
  }
}
