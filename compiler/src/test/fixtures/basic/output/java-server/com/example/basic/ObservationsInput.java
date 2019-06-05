package com.example.basic;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("org.realityforge.giggle.Main")
public final class ObservationsInput {
  @Nonnull
  private final String name;

  @Nonnull
  private final List<ObservationInput> type;

  public ObservationsInput(@Nonnull final String name, @Nonnull final List<ObservationInput> type) {
    this.name = Objects.requireNonNull( name );
    this.type = Objects.requireNonNull( type );
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static ObservationsInput from(@Nonnull final Map<String, Object> args) {
    final String name = (String) args.get( "name" );
    final List<Map<String, Object>> type = (List<Map<String, Object>>) args.get( "type" );
    return new ObservationsInput(name, type.stream().map( $element$ -> ObservationInput.from( $element$ ) ).collect( Collectors.toList() ));
  }

  @Nonnull
  public String getName() {
    return name;
  }

  @Nonnull
  public List<ObservationInput> getType() {
    return type;
  }
}
