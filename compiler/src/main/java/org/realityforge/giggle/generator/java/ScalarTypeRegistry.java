package org.realityforge.giggle.generator.java;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import graphql.Scalars;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import javax.annotation.Nonnull;

final class ScalarTypeRegistry
{
  @Nonnull
  private final Map<String, TypeName> _typeMap = new HashMap<>();

  @Nonnull
  static ScalarTypeRegistry createDefault()
  {
    final ScalarTypeRegistry registry = new ScalarTypeRegistry();
    registry.registerType( Scalars.GraphQLBoolean.getName(), TypeName.BOOLEAN );
    registry.registerType( Scalars.GraphQLByte.getName(), TypeName.BYTE );
    registry.registerType( Scalars.GraphQLShort.getName(), TypeName.SHORT );
    registry.registerType( Scalars.GraphQLInt.getName(), TypeName.INT );
    registry.registerType( Scalars.GraphQLLong.getName(), TypeName.LONG );
    registry.registerType( Scalars.GraphQLChar.getName(), TypeName.CHAR );
    registry.registerType( Scalars.GraphQLFloat.getName(), TypeName.FLOAT );
    registry.registerType( Scalars.GraphQLID.getName(), ClassName.get( String.class ) );
    registry.registerType( Scalars.GraphQLString.getName(), ClassName.get( String.class ) );
    registry.registerType( Scalars.GraphQLBigDecimal.getName(), ClassName.get( BigDecimal.class ) );
    registry.registerType( Scalars.GraphQLBigInteger.getName(), ClassName.get( BigInteger.class ) );

    // The following times are added as they match the encoding for jsonb which is what we use when
    // serializing jaxrs clients

    // Emitted as NormalizedCustomID as specified in java.util.TimeZone
    registry.registerType( "TimeZone", ClassName.get( TimeZone.class ) );
    // ISO_INSTANT
    registry.registerType( "Instant", ClassName.get( Instant.class ) );
    // Duration is the ISO 8601 seconds based representation
    registry.registerType( "Duration", ClassName.get( Duration.class ) );
    // ISO 8661 Period representation
    registry.registerType( "Period", ClassName.get( Period.class ) );
    // ISO_LOCAL_DATE
    registry.registerType( "LocalDate", ClassName.get( LocalDate.class ) );
    // ISO_LOCAL_TIME
    registry.registerType( "LocalTime", ClassName.get( LocalTime.class ) );
    // ISO_LOCAL_DATE_TIME
    registry.registerType( "LocalDateTime", ClassName.get( LocalDateTime.class ) );
    // ISO_ZONED_DATE_TIME
    registry.registerType( "ZonedDateTime", ClassName.get( ZonedDateTime.class ) );
    // ZoneID as specified in java.time.ZoneId
    registry.registerType( "ZoneId", ClassName.get( ZoneId.class ) );
    // Zone offset as specified in java.tim.ZoneOffset
    registry.registerType( "ZoneOffset", ClassName.get( ZoneOffset.class ) );
    // ISO_OFFSET_DATE_TIME
    registry.registerType( "OffsetDateTime", ClassName.get( OffsetDateTime.class ) );
    // ISO_OFFSET_TIME
    registry.registerType( "OffsetTime", ClassName.get( OffsetTime.class ) );
    return registry;
  }

  ScalarTypeRegistry()
  {
  }

  boolean isRegistered( @Nonnull final String name )
  {
    return _typeMap.containsKey( name );
  }

  void registerType( @Nonnull final String name, @Nonnull final TypeName type )
  {
    _typeMap.put( name, type );
  }

  @Nonnull
  TypeName getType( @Nonnull final String name )
  {
    final TypeName typeName = _typeMap.get( name );
    if ( null == typeName )
    {
      throw new IllegalStateException( "Unknown scalar type named '" + name + "'" );
    }
    return typeName;
  }
}
