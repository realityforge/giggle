package org.realityforge.giggle.integration.scenarios.inputtype2;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import org.realityforge.giggle.integration.scenarios.AbstractScenarioTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class InputType2Test
  extends AbstractScenarioTest
{
  @Test
  public void structure()
  {
    assertTrue( Modifier.isPublic( AllTypesInput.class.getModifiers() ) );
  }

  @Test
  public void fieldTypes()
    throws Exception
  {
    assertFieldType( AllTypesInput.class, "requiredBoolean", boolean.class );
    assertFieldType( AllTypesInput.class, "optionalBoolean", Boolean.class );
    assertListFieldType( AllTypesInput.class, "requiredBooleanList", Boolean.class );
    assertListFieldType( AllTypesInput.class, "requiredBooleanListContainingNulls", Boolean.class );
    assertListFieldType( AllTypesInput.class, "optionalBooleanList", Boolean.class );
    assertFieldType( AllTypesInput.class, "requiredInt", int.class );
    assertFieldType( AllTypesInput.class, "optionalInt", Integer.class );
    assertListFieldType( AllTypesInput.class, "requiredIntList", Integer.class );
    assertListFieldType( AllTypesInput.class, "requiredIntListContainingNulls", Integer.class );
    assertListFieldType( AllTypesInput.class, "optionalIntList", Integer.class );
    assertFieldType( AllTypesInput.class, "requiredFloat", float.class );
    assertFieldType( AllTypesInput.class, "optionalFloat", Float.class );
    assertListFieldType( AllTypesInput.class, "requiredFloatList", Float.class );
    assertListFieldType( AllTypesInput.class, "requiredFloatListContainingNulls", Float.class );
    assertListFieldType( AllTypesInput.class, "optionalFloatList", Float.class );
    assertFieldType( AllTypesInput.class, "requiredString", String.class );
    assertFieldType( AllTypesInput.class, "optionalString", String.class );
    assertListFieldType( AllTypesInput.class, "requiredStringList", String.class );
    assertListFieldType( AllTypesInput.class, "requiredStringListContainingNulls", String.class );
    assertListFieldType( AllTypesInput.class, "optionalStringList", String.class );
    assertFieldType( AllTypesInput.class, "requiredID", String.class );
    assertFieldType( AllTypesInput.class, "optionalID", String.class );
    assertListFieldType( AllTypesInput.class, "requiredIDList", String.class );
    assertListFieldType( AllTypesInput.class, "requiredIDListContainingNulls", String.class );
    assertListFieldType( AllTypesInput.class, "optionalIDList", String.class );
    assertFieldType( AllTypesInput.class, "requiredEnum", MyEnum.class );
    assertFieldType( AllTypesInput.class, "optionalEnum", MyEnum.class );
    assertListFieldType( AllTypesInput.class, "requiredEnumList", MyEnum.class );
    assertListFieldType( AllTypesInput.class, "requiredEnumListContainingNulls", MyEnum.class );
    assertListFieldType( AllTypesInput.class, "optionalEnumList", MyEnum.class );
    assertFieldType( AllTypesInput.class, "requiredInput", MyInput.class );
    assertFieldType( AllTypesInput.class, "optionalInput", MyInput.class );
    assertListFieldType( AllTypesInput.class, "requiredInputList", MyInput.class );
    assertListFieldType( AllTypesInput.class, "requiredInputListContainingNulls", MyInput.class );
    assertListFieldType( AllTypesInput.class, "optionalInputList", MyInput.class );
  }

  @Test
  public void nullability()
    throws Exception
  {
    assertNeitherNullableNorNonnull( AllTypesInput.class, "requiredBoolean" );
    assertNullable( AllTypesInput.class, "optionalBoolean" );
    assertNonnull( AllTypesInput.class, "requiredBooleanList" );
    assertNonnull( AllTypesInput.class, "requiredBooleanListContainingNulls" );
    assertNullable( AllTypesInput.class, "optionalBooleanList" );
    assertNeitherNullableNorNonnull( AllTypesInput.class, "requiredInt" );
    assertNullable( AllTypesInput.class, "optionalInt" );
    assertNonnull( AllTypesInput.class, "requiredIntList" );
    assertNonnull( AllTypesInput.class, "requiredIntListContainingNulls" );
    assertNullable( AllTypesInput.class, "optionalIntList" );
    assertNeitherNullableNorNonnull( AllTypesInput.class, "requiredFloat" );
    assertNullable( AllTypesInput.class, "optionalFloat" );
    assertNonnull( AllTypesInput.class, "requiredFloatList" );
    assertNonnull( AllTypesInput.class, "requiredFloatListContainingNulls" );
    assertNullable( AllTypesInput.class, "optionalFloatList" );
    assertNonnull( AllTypesInput.class, "requiredString" );
    assertNullable( AllTypesInput.class, "optionalString" );
    assertNonnull( AllTypesInput.class, "requiredStringList" );
    assertNonnull( AllTypesInput.class, "requiredStringListContainingNulls" );
    assertNullable( AllTypesInput.class, "optionalStringList" );
    assertNonnull( AllTypesInput.class, "requiredID" );
    assertNullable( AllTypesInput.class, "optionalID" );
    assertNonnull( AllTypesInput.class, "requiredIDList" );
    assertNonnull( AllTypesInput.class, "requiredIDListContainingNulls" );
    assertNullable( AllTypesInput.class, "optionalIDList" );
    assertNonnull( AllTypesInput.class, "requiredEnum" );
    assertNullable( AllTypesInput.class, "optionalEnum" );
    assertNonnull( AllTypesInput.class, "requiredEnumList" );
    assertNonnull( AllTypesInput.class, "requiredEnumListContainingNulls" );
    assertNullable( AllTypesInput.class, "optionalEnumList" );
    assertNonnull( AllTypesInput.class, "requiredInput" );
    assertNullable( AllTypesInput.class, "optionalInput" );
    assertNonnull( AllTypesInput.class, "requiredInputList" );
    assertNonnull( AllTypesInput.class, "requiredInputListContainingNulls" );
    assertNullable( AllTypesInput.class, "optionalInputList" );
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
    final AllTypesInput input = AllTypesInput.from( args );

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

    assertEquals( input.toString(),
                  "AllTypesInput[optionalBoolean=null, optionalBooleanList=null, optionalEnum=null, optionalEnumList=null, optionalFloat=null, optionalFloatList=null, optionalID=null, optionalIDList=null, optionalInput=null, optionalInputList=null, optionalInt=null, optionalIntList=null, optionalString=null, optionalStringList=null, requiredBoolean=true, requiredBooleanList=[true], requiredBooleanListContainingNulls=[null], requiredEnum=A_VALUE, requiredEnumList=[A_VALUE], requiredEnumListContainingNulls=[null], requiredFloat=2.5, requiredFloatList=[2.5], requiredFloatListContainingNulls=[null], requiredID=MyID, requiredIDList=[MyID], requiredIDListContainingNulls=[null], requiredInput=MyInput[v=Blah], requiredInputList=[MyInput[v=Blah]], requiredInputListContainingNulls=[null], requiredInt=1, requiredIntList=[1], requiredIntListContainingNulls=[null], requiredString=MyString, requiredStringList=[MyString], requiredStringListContainingNulls=[null]]" );

    final AllTypesInput input2 = AllTypesInput.from( args );
    args.put( "optionalID", "XXXX" );
    final AllTypesInput input3 = AllTypesInput.from( args );

    assertEquals( input, input );
    assertEquals( input, input2 );
    assertNotEquals( input, input3 );
    assertEquals( input.hashCode(), input2.hashCode() );
    assertNotEquals( input.hashCode(), input3.hashCode() );
  }
}
