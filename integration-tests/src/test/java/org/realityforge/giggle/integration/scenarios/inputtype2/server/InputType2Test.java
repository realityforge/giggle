package org.realityforge.giggle.integration.scenarios.inputtype2.server;

import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    assertFieldType( AllTypesInput.class, "requiredDate", LocalDate.class );
    assertFieldType( AllTypesInput.class, "optionalDate", LocalDate.class );
    assertListFieldType( AllTypesInput.class, "requiredDateList", LocalDate.class );
    assertListFieldType( AllTypesInput.class, "requiredDateListContainingNulls", LocalDate.class );
    assertListFieldType( AllTypesInput.class, "optionalDateList", LocalDate.class );
    assertFieldType( AllTypesInput.class, "requiredDateTime", LocalDateTime.class );
    assertFieldType( AllTypesInput.class, "optionalDateTime", LocalDateTime.class );
    assertListFieldType( AllTypesInput.class, "requiredDateTimeList", LocalDateTime.class );
    assertListFieldType( AllTypesInput.class, "requiredDateTimeListContainingNulls", LocalDateTime.class );
    assertListFieldType( AllTypesInput.class, "optionalDateTimeList", LocalDateTime.class );
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
    assertNonnull( AllTypesInput.class, "requiredDate" );
    assertNullable( AllTypesInput.class, "optionalDate" );
    assertNonnull( AllTypesInput.class, "requiredDateList" );
    assertNonnull( AllTypesInput.class, "requiredDateListContainingNulls" );
    assertNullable( AllTypesInput.class, "optionalDateList" );
    assertNonnull( AllTypesInput.class, "requiredDateTime" );
    assertNullable( AllTypesInput.class, "optionalDateTime" );
    assertNonnull( AllTypesInput.class, "requiredDateTimeList" );
    assertNonnull( AllTypesInput.class, "requiredDateTimeListContainingNulls" );
    assertNullable( AllTypesInput.class, "optionalDateTimeList" );
  }

  @Test
  public void from()
  {
    final HashMap<String, Object> inputArgs = new HashMap<>();
    inputArgs.put( "v", "Blah" );

    final LocalDate date = LocalDate.of( 2001, 10, 20 );
    final LocalDateTime dateTime = LocalDateTime.of( 2001, 10, 20, 16, 30 );

    final HashMap<String, Object> args = new HashMap<>();
    args.put( "requiredBoolean", true );
    args.put( "requiredInt", 1 );
    args.put( "requiredFloat", 2.5F );
    args.put( "requiredString", "MyString" );
    args.put( "requiredID", "MyID" );
    args.put( "requiredEnum", MyEnum.A_VALUE );
    args.put( "requiredInput", inputArgs );
    args.put( "requiredDate", date );
    args.put( "requiredDateTime", dateTime );
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
    args.put( "requiredDateList", Collections.singletonList( date ) );
    args.put( "requiredDateListContainingNulls", Collections.singletonList( null ) );
    args.put( "requiredDateTimeList", Collections.singletonList( dateTime ) );
    args.put( "requiredDateTimeListContainingNulls", Collections.singletonList( null ) );
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
    assertEquals( input.getRequiredDate(), date );
    assertEquals( input.getRequiredDateTime(), dateTime );
    assertEquals( input.getRequiredBooleanList(), Collections.singletonList( true ) );
    assertEquals( input.getRequiredIntList(), Collections.singletonList( 1 ) );
    assertEquals( input.getRequiredFloatList(), Collections.singletonList( 2.5F ) );
    assertEquals( input.getRequiredStringList(), Collections.singletonList( "MyString" ) );
    assertEquals( input.getRequiredIDList(), Collections.singletonList( "MyID" ) );
    assertEquals( input.getRequiredEnumList(), Collections.singletonList( MyEnum.A_VALUE ) );
    assertEquals( input.getRequiredDateList(), Collections.singletonList( date ) );
    assertEquals( input.getRequiredDateTimeList(), Collections.singletonList( dateTime ) );
    assertEquals( input.getRequiredInputList().size(), 1 );
    assertEquals( input.getRequiredInputList().get( 0 ).getV(), "Blah" );

    assertEquals( input.toString(),
                  "AllTypesInput[requiredBoolean=true, optionalBoolean=null, requiredInt=1, optionalInt=null, requiredFloat=2.5, optionalFloat=null, requiredString=MyString, optionalString=null, requiredID=MyID, optionalID=null, requiredEnum=A_VALUE, optionalEnum=null, requiredInput=MyInput[v=Blah], optionalInput=null, requiredDate=2001-10-20, optionalDate=null, requiredDateTime=2001-10-20T16:30, optionalDateTime=null, requiredBooleanList=[true], requiredBooleanListContainingNulls=[null], optionalBooleanList=null, requiredIntList=[1], requiredIntListContainingNulls=[null], optionalIntList=null, requiredFloatList=[2.5], requiredFloatListContainingNulls=[null], optionalFloatList=null, requiredStringList=[MyString], requiredStringListContainingNulls=[null], optionalStringList=null, requiredIDList=[MyID], requiredIDListContainingNulls=[null], optionalIDList=null, requiredEnumList=[A_VALUE], requiredEnumListContainingNulls=[null], optionalEnumList=null, requiredInputList=[MyInput[v=Blah]], requiredInputListContainingNulls=[null], optionalInputList=null, requiredDateList=[2001-10-20], requiredDateListContainingNulls=[null], optionalDateList=null, requiredDateTimeList=[2001-10-20T16:30], requiredDateTimeListContainingNulls=[null], optionalDateTimeList=null]" );

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
