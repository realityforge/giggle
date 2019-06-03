package org.realityforge.giggle.generator.java;

import graphql.schema.GraphQLType;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import org.realityforge.giggle.generator.Generator;
import org.realityforge.giggle.generator.GeneratorContext;

public abstract class AbstractJavaGenerator
  implements Generator
{
  @Nonnull
  protected final Map<GraphQLType, String> buildTypeMapping( @Nonnull final GeneratorContext context )
  {
    final Map<GraphQLType, String> typeMap = new HashMap<>();
    context.getTypeMapping().forEach( ( key, value ) -> typeMap.put( context.getSchema().getType( key ), value ) );
    return typeMap;
  }
}
