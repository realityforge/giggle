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
  /**
   * Return true if type does not look like types derived from the
   * {@link graphql.introspection.Introspection#__Schema introspection schema}
   *
   * @param type the type.
   * @return true if it is not an introspection type.
   */
  protected final boolean isNotIntrospectionType( @Nonnull final GraphQLType type )
  {
    return !type.getName().startsWith( "__" );
  }

  @Nonnull
  protected final Map<GraphQLType, String> buildTypeMapping( @Nonnull final GeneratorContext context )
  {
    final Map<GraphQLType, String> typeMap = new HashMap<>();
    context.getTypeMapping().forEach( ( key, value ) -> typeMap.put( context.getSchema().getType( key ), value ) );
    return typeMap;
  }
}
