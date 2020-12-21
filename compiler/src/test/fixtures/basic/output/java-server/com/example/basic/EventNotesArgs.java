package com.example.basic;

import graphql.schema.DataFetchingEnvironment;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("org.realityforge.giggle.Main")
public final class EventNotesArgs {
  @Nonnull
  private final String noteType;

  private EventNotesArgs(@Nonnull final String noteType) {
    this.noteType = Objects.requireNonNull( noteType );
  }

  @Nonnull
  public static EventNotesArgs from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final String $giggle$_noteType = (String) args.get( "noteType" );
    return new EventNotesArgs($giggle$_noteType);
  }

  @Nonnull
  public String getNoteType() {
    return noteType;
  }
}
