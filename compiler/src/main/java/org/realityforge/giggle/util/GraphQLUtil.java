package org.realityforge.giggle.util;

import graphql.language.OperationDefinition;
import javax.annotation.Nonnull;

public final class GraphQLUtil
{
  private GraphQLUtil()
  {
  }

  @Nonnull
  public static String getTopLevelFieldName( @Nonnull final OperationDefinition.Operation operationType )
  {
    return OperationDefinition.Operation.QUERY == operationType ? "Query" :
           OperationDefinition.Operation.MUTATION == operationType ? "Mutation" :
           "Subscription";
  }
}
