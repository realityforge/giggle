package com.example.cdi_client_custom_properties;

import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("org.realityforge.giggle.Main")
public interface EventService {
  @Nonnull
  EventQuery.Answer event(@Nonnull String id);
}
