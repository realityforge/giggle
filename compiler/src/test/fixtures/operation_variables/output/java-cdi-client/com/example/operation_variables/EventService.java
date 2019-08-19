package com.example.operation_variables;

import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("org.realityforge.giggle.Main")
public interface EventService {
  @Nonnull
  EventQuery.Answer event(@Nonnull String id);
}
