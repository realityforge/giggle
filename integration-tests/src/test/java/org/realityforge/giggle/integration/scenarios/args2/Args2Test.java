package org.realityforge.giggle.integration.scenarios.args2;

import graphql.ErrorType;
import graphql.execution.MergedField;
import graphql.language.Field;
import graphql.language.SourceLocation;
import graphql.schema.CoercingParseValueException;
import graphql.schema.DataFetchingEnvironmentImpl;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import org.realityforge.giggle.integration.scenarios.AbstractScenarioTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class Args2Test
  extends AbstractScenarioTest
{
  @Test
  public void structure()
    throws Exception
  {
    assertTrue( Modifier.isPublic( CreateObservationArgs.class.getModifiers() ) );
    assertFieldType( CreateObservationArgs.class, "oid", int.class );
    assertFieldType( CreateObservationArgs.class, "type", String.class );
    assertFieldType( CreateObservationArgs.class, "value", int.class );
    assertFieldType( CreateObservationArgs.class, "error", ErrorRateInput.class );

    assertNeitherNullableNorNonnull( CreateObservationArgs.class, "oid" );
    assertNonnull( CreateObservationArgs.class, "type" );
    assertNeitherNullableNorNonnull( CreateObservationArgs.class, "value" );
    assertNullable( CreateObservationArgs.class, "error" );
  }

  @Test
  public void from()
  {
    final HashMap<String, Object> args = new HashMap<>();
    args.put( "oid", "1" );
    args.put( "type", "Wind Speed" );
    args.put( "value", 67 );

    final CreateObservationArgs createObservationArgs =
      CreateObservationArgs.from( DataFetchingEnvironmentImpl.newDataFetchingEnvironment().arguments( args ).build() );

    assertEquals( createObservationArgs.getOid(), 1 );
    assertEquals( createObservationArgs.getType(), "Wind Speed" );
    assertEquals( createObservationArgs.getValue(), 67 );
  }

  @Test
  public void from_with_arg_invalidID()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final HashMap<String, Object> args = new HashMap<>();
      args.put( "oid", "abc" );
      args.put( "type", "Wind Speed" );
      args.put( "value", 67 );

      final SourceLocation sourceLocation = new SourceLocation( 1, 2 );
      final Field field = Field.newField( "createObservation" ).sourceLocation( sourceLocation ).build();

      final CoercingParseValueException exception =
        expectThrows( CoercingParseValueException.class,
                      () -> CreateObservationArgs.from( DataFetchingEnvironmentImpl.newDataFetchingEnvironment()
                                                          .arguments( args )
                                                          .mergedField( MergedField.newMergedField()
                                                                          .addField( field )
                                                                          .build() )
                                                          .build() ) );
      assertEquals( exception.getMessage(),
                    "Failed to parse argument 'oid' that was expected to be a numeric ID type. Actual value = 'abc'" );
      assertEquals( exception.getLocations(), Collections.singletonList( sourceLocation ) );
      assertEquals( exception.getErrorType(), ErrorType.ValidationError );
    } );
  }

  @Test
  public void from_with_input_invalidID()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final HashMap<String, Object> errorArgs = new HashMap<>();
      errorArgs.put( "oid", "x" );
      errorArgs.put( "value", 2.3F );

      final HashMap<String, Object> args = new HashMap<>();
      args.put( "oid", "77" );
      args.put( "type", "Wind Speed" );
      args.put( "value", 67 );
      args.put( "error", errorArgs );

      final SourceLocation sourceLocation = new SourceLocation( 1, 2 );
      final Field field = Field.newField( "createObservation" ).sourceLocation( sourceLocation ).build();

      final CoercingParseValueException exception =
        expectThrows( CoercingParseValueException.class,
                      () -> CreateObservationArgs.from( DataFetchingEnvironmentImpl.newDataFetchingEnvironment()
                                                          .arguments( args )
                                                          .mergedField( MergedField.newMergedField()
                                                                          .addField( field )
                                                                          .build() )
                                                          .build() ) );
      assertEquals( exception.getMessage(),
                    "Failed to parse input field 'oid' that was expected to be a numeric ID type. Actual value = 'x'" );
      assertEquals( exception.getLocations(), Collections.singletonList( sourceLocation ) );
      assertEquals( exception.getErrorType(), ErrorType.ValidationError );
    } );
  }
}
