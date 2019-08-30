package org.realityforge.giggle.util;

import graphql.language.OperationDefinition;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class GraphQLUtilTest
{
  @Test
  public void getTopLevelFieldName()
  {
    assertEquals( GraphQLUtil.getTopLevelFieldName( OperationDefinition.Operation.QUERY ), "Query" );
    assertEquals( GraphQLUtil.getTopLevelFieldName( OperationDefinition.Operation.MUTATION ), "Mutation" );
    assertEquals( GraphQLUtil.getTopLevelFieldName( OperationDefinition.Operation.SUBSCRIPTION ), "Subscription" );
  }
}
