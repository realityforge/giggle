package com.example.basic;

import javax.annotation.Generated;

@Generated("org.realityforge.giggle.Main")
public class GraphQLException extends RuntimeException {
  public GraphQLException() {
  }

  public GraphQLException(final String message) {
    super( message );
  }

  public GraphQLException(final String message, final Throwable cause) {
    super( message, cause );
  }

  public GraphQLException(final Throwable cause) {
    super( cause );
  }
}
