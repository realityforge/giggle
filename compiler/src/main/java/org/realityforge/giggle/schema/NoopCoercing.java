package org.realityforge.giggle.schema;

import graphql.schema.Coercing;

// We never run the queries so just use a dummy Coercing
final class NoopCoercing
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
