package com.example.basic;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class ObservationsInput {
  @Nonnull
  private final String name;

  @Nonnull
  private final List<ObservationInput> type;

  private ObservationsInput(@Nonnull final String name,
      @Nonnull final List<ObservationInput> type) {
    this.name = Objects.requireNonNull( name );
    this.type = Objects.requireNonNull( type );
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static ObservationsInput from(@Nonnull final Map<String, Object> args) {
    final String $giggle$_name = (String) args.get( "name" );
    final List<Map<String, Object>> $giggle$_type = (List<Map<String, Object>>) args.get( "type" );
    return new ObservationsInput($giggle$_name, $giggle$_type.stream().map( ObservationInput::from ).collect( Collectors.toList() ));
  }

  @Nullable
  public static ObservationsInput maybeFrom(@Nullable final Map<String, Object> args) {
    return null == args ? null : from( args );
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
