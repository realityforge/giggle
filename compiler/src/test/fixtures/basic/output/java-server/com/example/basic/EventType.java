package com.example.basic;

import javax.annotation.Generated;

/**
 * Classification of Event
 */
@Generated("org.realityforge.giggle.Main")
public enum EventType {
  Wildfire,

  /**
   * A wildfire set intentionally for purposes of forest management, farming, prairie restoration or greenhouse gas abatement.
   */
  Burn,

  /**
   * A flood is an overflow of water that submerges land that is usually dry.
   */
  FloodOrStorm,

  /**
   * @deprecated Use `Burn`.
   */
  @Deprecated
  RegenBurn
}
