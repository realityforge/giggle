package org.realityforge.giggle.integration.scenarios.args.server;

import graphql.schema.DataFetchingEnvironmentImpl;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import org.realityforge.giggle.integration.scenarios.AbstractScenarioTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ArgsTest
  extends AbstractScenarioTest
{
  @Test
  public void structure()
  {
    assertTrue( Modifier.isPublic( MyQueryArgs.class.getModifiers() ) );
  }

  @Test
  public void fieldTypes()
    throws Exception
  {
    assertFieldType( MyQueryArgs.class, "requiredBoolean", boolean.class );
    assertFieldType( MyQueryArgs.class, "optionalBoolean", Boolean.class );
    assertListFieldType( MyQueryArgs.class, "requiredBooleanList", Boolean.class );
    assertListFieldType( MyQueryArgs.class, "requiredBooleanListContainingNulls", Boolean.class );
    assertListFieldType( MyQueryArgs.class, "optionalBooleanList", Boolean.class );
    assertFieldType( MyQueryArgs.class, "requiredInt", int.class );
    assertFieldType( MyQueryArgs.class, "optionalInt", Integer.class );
    assertListFieldType( MyQueryArgs.class, "requiredIntList", Integer.class );
    assertListFieldType( MyQueryArgs.class, "requiredIntListContainingNulls", Integer.class );
    assertListFieldType( MyQueryArgs.class, "optionalIntList", Integer.class );
    assertFieldType( MyQueryArgs.class, "requiredFloat", float.class );
    assertFieldType( MyQueryArgs.class, "optionalFloat", Float.class );
    assertListFieldType( MyQueryArgs.class, "requiredFloatList", Float.class );
    assertListFieldType( MyQueryArgs.class, "requiredFloatListContainingNulls", Float.class );
    assertListFieldType( MyQueryArgs.class, "optionalFloatList", Float.class );
    assertFieldType( MyQueryArgs.class, "requiredString", String.class );
    assertFieldType( MyQueryArgs.class, "optionalString", String.class );
    assertListFieldType( MyQueryArgs.class, "requiredStringList", String.class );
    assertListFieldType( MyQueryArgs.class, "requiredStringListContainingNulls", String.class );
    assertListFieldType( MyQueryArgs.class, "optionalStringList", String.class );
    assertFieldType( MyQueryArgs.class, "requiredID", String.class );
    assertFieldType( MyQueryArgs.class, "optionalID", String.class );
    assertListFieldType( MyQueryArgs.class, "requiredIDList", String.class );
    assertListFieldType( MyQueryArgs.class, "requiredIDListContainingNulls", String.class );
    assertListFieldType( MyQueryArgs.class, "optionalIDList", String.class );
    assertFieldType( MyQueryArgs.class, "requiredEnum", MyEnum.class );
    assertFieldType( MyQueryArgs.class, "optionalEnum", MyEnum.class );
    assertListFieldType( MyQueryArgs.class, "requiredEnumList", MyEnum.class );
    assertListFieldType( MyQueryArgs.class, "requiredEnumListContainingNulls", MyEnum.class );
    assertListFieldType( MyQueryArgs.class, "optionalEnumList", MyEnum.class );
    assertFieldType( MyQueryArgs.class, "requiredInput", MyInput.class );
    assertFieldType( MyQueryArgs.class, "optionalInput", MyInput.class );
    assertListFieldType( MyQueryArgs.class, "requiredInputList", MyInput.class );
    assertListFieldType( MyQueryArgs.class, "requiredInputListContainingNulls", MyInput.class );
    assertListFieldType( MyQueryArgs.class, "optionalInputList", MyInput.class );
  }

  @Test
  public void nullability()
    throws Exception
  {
    assertNeitherNullableNorNonnull( MyQueryArgs.class, "requiredBoolean" );
    assertNullable( MyQueryArgs.class, "optionalBoolean" );
    assertNonnull( MyQueryArgs.class, "requiredBooleanList" );
    assertNonnull( MyQueryArgs.class, "requiredBooleanListContainingNulls" );
    assertNullable( MyQueryArgs.class, "optionalBooleanList" );
    assertNeitherNullableNorNonnull( MyQueryArgs.class, "requiredInt" );
    assertNullable( MyQueryArgs.class, "optionalInt" );
    assertNonnull( MyQueryArgs.class, "requiredIntList" );
    assertNonnull( MyQueryArgs.class, "requiredIntListContainingNulls" );
    assertNullable( MyQueryArgs.class, "optionalIntList" );
    assertNeitherNullableNorNonnull( MyQueryArgs.class, "requiredFloat" );
    assertNullable( MyQueryArgs.class, "optionalFloat" );
    assertNonnull( MyQueryArgs.class, "requiredFloatList" );
    assertNonnull( MyQueryArgs.class, "requiredFloatListContainingNulls" );
    assertNullable( MyQueryArgs.class, "optionalFloatList" );
    assertNonnull( MyQueryArgs.class, "requiredString" );
    assertNullable( MyQueryArgs.class, "optionalString" );
    assertNonnull( MyQueryArgs.class, "requiredStringList" );
    assertNonnull( MyQueryArgs.class, "requiredStringListContainingNulls" );
    assertNullable( MyQueryArgs.class, "optionalStringList" );
    assertNonnull( MyQueryArgs.class, "requiredID" );
    assertNullable( MyQueryArgs.class, "optionalID" );
    assertNonnull( MyQueryArgs.class, "requiredIDList" );
    assertNonnull( MyQueryArgs.class, "requiredIDListContainingNulls" );
    assertNullable( MyQueryArgs.class, "optionalIDList" );
    assertNonnull( MyQueryArgs.class, "requiredEnum" );
    assertNullable( MyQueryArgs.class, "optionalEnum" );
    assertNonnull( MyQueryArgs.class, "requiredEnumList" );
    assertNonnull( MyQueryArgs.class, "requiredEnumListContainingNulls" );
    assertNullable( MyQueryArgs.class, "optionalEnumList" );
    assertNonnull( MyQueryArgs.class, "requiredInput" );
    assertNullable( MyQueryArgs.class, "optionalInput" );
    assertNonnull( MyQueryArgs.class, "requiredInputList" );
    assertNonnull( MyQueryArgs.class, "requiredInputListContainingNulls" );
    assertNullable( MyQueryArgs.class, "optionalInputList" );
  }

  @Test
  public void from()
  {
    final HashMap<String, Object> inputArgs = new HashMap<>();
    inputArgs.put( "v", "Blah" );

    final HashMap<String, Object> args = new HashMap<>();
    args.put( "requiredBoolean", true );
    args.put( "requiredInt", 1 );
    args.put( "requiredFloat", 2.5F );
    args.put( "requiredString", "MyString" );
    args.put( "requiredID", "MyID" );
    args.put( "requiredEnum", MyEnum.A_VALUE );
    args.put( "requiredInput", inputArgs );
    args.put( "requiredBooleanList", Collections.singletonList( true ) );
    args.put( "requiredBooleanListContainingNulls", Collections.singletonList( null ) );
    args.put( "requiredIntList", Collections.singletonList( 1 ) );
    args.put( "requiredIntListContainingNulls", Collections.singletonList( null ) );
    args.put( "requiredFloatList", Collections.singletonList( 2.5F ) );
    args.put( "requiredFloatListContainingNulls", Collections.singletonList( null ) );
    args.put( "requiredStringList", Collections.singletonList( "MyString" ) );
    args.put( "requiredStringListContainingNulls", Collections.singletonList( null ) );
    args.put( "requiredIDList", Collections.singletonList( "MyID" ) );
    args.put( "requiredIDListContainingNulls", Collections.singletonList( null ) );
    args.put( "requiredEnumList", Collections.singletonList( MyEnum.A_VALUE ) );
    args.put( "requiredEnumListContainingNulls", Collections.singletonList( null ) );
    args.put( "requiredInputList", Collections.singletonList( inputArgs ) );
    args.put( "requiredInputListContainingNulls", Collections.singletonList( null ) );

    final MyQueryArgs input =
      MyQueryArgs.from( DataFetchingEnvironmentImpl.newDataFetchingEnvironment().arguments( args ).build() );

    assertNull( input.getOptionalBoolean() );
    assertNull( input.getOptionalInt() );
    assertNull( input.getOptionalFloat() );
    assertNull( input.getOptionalString() );
    assertNull( input.getOptionalID() );
    assertNull( input.getOptionalEnum() );
    assertNull( input.getOptionalInput() );
    assertNull( input.getOptionalBooleanList() );
    assertNull( input.getOptionalIntList() );
    assertNull( input.getOptionalFloatList() );
    assertNull( input.getOptionalStringList() );
    assertNull( input.getOptionalIDList() );
    assertNull( input.getOptionalEnumList() );
    assertNull( input.getOptionalInputList() );

    assertTrue( input.getRequiredBoolean() );
    assertEquals( input.getRequiredInt(), 1 );
    assertEquals( input.getRequiredFloat(), 2.5F );
    assertEquals( input.getRequiredString(), "MyString" );
    assertEquals( input.getRequiredID(), "MyID" );
    assertEquals( input.getRequiredEnum(), MyEnum.A_VALUE );
    assertEquals( input.getRequiredInput().getV(), "Blah" );
    assertEquals( input.getRequiredBooleanList(), Collections.singletonList( true ) );
    assertEquals( input.getRequiredIntList(), Collections.singletonList( 1 ) );
    assertEquals( input.getRequiredFloatList(), Collections.singletonList( 2.5F ) );
    assertEquals( input.getRequiredStringList(), Collections.singletonList( "MyString" ) );
    assertEquals( input.getRequiredIDList(), Collections.singletonList( "MyID" ) );
    assertEquals( input.getRequiredEnumList(), Collections.singletonList( MyEnum.A_VALUE ) );
    assertEquals( input.getRequiredInputList().size(), 1 );
    assertEquals( input.getRequiredInputList().get( 0 ).getV(), "Blah" );
  }
}
