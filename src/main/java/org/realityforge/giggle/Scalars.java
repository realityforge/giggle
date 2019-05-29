package org.realityforge.giggle;

import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

/**
 * Contains scalars required when building schemas.
 * Th scalar types have noop coercings as they never expect to be executed.
 */
final class Scalars
{
  static GraphQLScalarType DATE_TIME =
    GraphQLScalarType.newScalar()
      .name( "DateTime" )
      .description( "DataTime scalar" )
      .coercing( new NoopCoercing() )
      .build();
  static GraphQLScalarType DATE =
    GraphQLScalarType.newScalar()
      .name( "Date" )
      .description( "Date scalar" )
      .coercing( new NoopCoercing() )
      .build();

  // We never run the queries so just use a dummy Coercing
  private static class NoopCoercing
    implements Coercing<String, String>
  {
    @Override
    public String serialize( final Object input )
    {
      return null;
    }

    @Override
    public String parseValue( final Object input )
    {
      return null;
    }

    @Override
    public String parseLiteral( final Object input )
    {
      return null;
    }
  }
}
