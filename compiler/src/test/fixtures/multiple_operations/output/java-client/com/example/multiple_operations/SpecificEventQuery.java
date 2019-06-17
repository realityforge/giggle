package com.example.multiple_operations;

import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class SpecificEventQuery {
  @Nonnull
  public static final String QUERY = "fragment EventName1 on Event {name} query specificEvent {event(id:1) {id ...EventName1}}";

  private SpecificEventQuery() {
  }

  public static final class Answer {
    @Nullable
    private SpecificEventResponse data;

    @Nullable
    private GraphQLError[] errors;

    public boolean hasData() {
      return null != data;
    }

    @Nonnull
    public SpecificEventResponse getData() {
      return Objects.requireNonNull( data );
    }

    public void setData(@Nullable final SpecificEventResponse data) {
      this.data = data;
    }

    public boolean hasErrors() {
      return null != errors;
    }

    @Nonnull
    public GraphQLError[] getErrors() {
      return Objects.requireNonNull( errors );
    }

    public void setErrors(@Nullable final GraphQLError[] errors) {
      this.errors = errors;
    }
  }
}
