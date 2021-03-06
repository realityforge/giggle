package com.example.multiple_operations;

import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class OtherEventQuery {
  @Nonnull
  private static final String QUERY = "query {event(id:2) {id name type}}";

  private OtherEventQuery() {
  }

  public static final class Question {
    @Nonnull
    public String getQuery() {
      return QUERY;
    }
  }

  public static final class Answer {
    @Nullable
    private OtherEventResponse data;

    @Nullable
    private GraphQLError[] errors;

    public boolean hasData() {
      return null != data;
    }

    @Nonnull
    public OtherEventResponse getData() {
      return Objects.requireNonNull( data );
    }

    public void setData(@Nullable final OtherEventResponse data) {
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
