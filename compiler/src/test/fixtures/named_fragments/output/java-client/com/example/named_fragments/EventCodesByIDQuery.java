package com.example.named_fragments;

import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class EventCodesByIDQuery {
  @Nonnull
  private static final String QUERY = "query eventCodesByID($id:ID!) {event(id:$id) {code name}}";

  private EventCodesByIDQuery() {
  }

  public static final class Variables {
    @Nonnull
    private final String id;

    private Variables(@Nonnull final String id) {
      this.id = Objects.requireNonNull( id );
    }

    @Nonnull
    public String getId() {
      return id;
    }
  }

  public static final class Question {
    @Nonnull
    private final Variables variables;

    public Question(@Nonnull final String id) {
      this.variables = new Variables( id );
    }

    @Nonnull
    public Variables getVariables() {
      return variables;
    }

    @Nonnull
    public String getQuery() {
      return QUERY;
    }
  }

  public static final class Answer {
    @Nullable
    private EventCodesByIDResponse data;

    @Nullable
    private GraphQLError[] errors;

    public boolean hasData() {
      return null != data;
    }

    @Nonnull
    public EventCodesByIDResponse getData() {
      return Objects.requireNonNull( data );
    }

    public void setData(@Nullable final EventCodesByIDResponse data) {
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
