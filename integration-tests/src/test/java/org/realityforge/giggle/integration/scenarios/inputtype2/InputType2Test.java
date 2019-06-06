package org.realityforge.giggle.integration.scenarios.inputtype2;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
    assertFieldType( "requiredBoolean", boolean.class );
    assertFieldType( "optionalBoolean", Boolean.class );
    assertListFieldType( "requiredBooleanList", Boolean.class );
    assertListFieldType( "requiredBooleanListContainingNulls", Boolean.class );
    assertListFieldType( "optionalBooleanList", Boolean.class );
    assertFieldType( "requiredInt", int.class );
    assertFieldType( "optionalInt", Integer.class );
    assertListFieldType( "requiredIntList", Integer.class );
    assertListFieldType( "requiredIntListContainingNulls", Integer.class );
    assertListFieldType( "optionalIntList", Integer.class );
    assertFieldType( "requiredFloat", float.class );
    assertFieldType( "optionalFloat", Float.class );
    assertListFieldType( "requiredFloatList", Float.class );
    assertListFieldType( "requiredFloatListContainingNulls", Float.class );
    assertListFieldType( "optionalFloatList", Float.class );
    assertFieldType( "requiredString", String.class );
    assertFieldType( "optionalString", String.class );
    assertListFieldType( "requiredStringList", String.class );
    assertListFieldType( "requiredStringListContainingNulls", String.class );
    assertListFieldType( "optionalStringList", String.class );
    assertFieldType( "requiredID", String.class );
    assertFieldType( "optionalID", String.class );
    assertListFieldType( "requiredIDList", String.class );
    assertListFieldType( "requiredIDListContainingNulls", String.class );
    assertListFieldType( "optionalIDList", String.class );
    assertFieldType( "requiredEnum", MyEnum.class );
    assertFieldType( "optionalEnum", MyEnum.class );
    assertListFieldType( "requiredEnumList", MyEnum.class );
    assertListFieldType( "requiredEnumListContainingNulls", MyEnum.class );
    assertListFieldType( "optionalEnumList", MyEnum.class );
    assertFieldType( "requiredInput", MyInput.class );
    assertFieldType( "optionalInput", MyInput.class );
    assertListFieldType( "requiredInputList", MyInput.class );
    assertListFieldType( "requiredInputListContainingNulls", MyInput.class );
    assertListFieldType( "optionalInputList", MyInput.class );
  }

  @Test
  public void nullability()
    throws Exception
  {
    assertNeitherNullableNorNonnull( "requiredBoolean" );
    assertNullable( "optionalBoolean" );
    assertNonnull( "requiredBooleanList" );
    assertNonnull( "requiredBooleanListContainingNulls" );
    assertNullable( "optionalBooleanList" );
    assertNeitherNullableNorNonnull( "requiredInt" );
    assertNullable( "optionalInt" );
    assertNonnull( "requiredIntList" );
    assertNonnull( "requiredIntListContainingNulls" );
    assertNullable( "optionalIntList" );
    assertNeitherNullableNorNonnull( "requiredFloat" );
    assertNullable( "optionalFloat" );
    assertNonnull( "requiredFloatList" );
    assertNonnull( "requiredFloatListContainingNulls" );
    assertNullable( "optionalFloatList" );
    assertNonnull( "requiredString" );
    assertNullable( "optionalString" );
    assertNonnull( "requiredStringList" );
    assertNonnull( "requiredStringListContainingNulls" );
    assertNullable( "optionalStringList" );
    assertNonnull( "requiredID" );
    assertNullable( "optionalID" );
    assertNonnull( "requiredIDList" );
    assertNonnull( "requiredIDListContainingNulls" );
    assertNullable( "optionalIDList" );
    assertNonnull( "requiredEnum" );
    assertNullable( "optionalEnum" );
    assertNonnull( "requiredEnumList" );
    assertNonnull( "requiredEnumListContainingNulls" );
    assertNullable( "optionalEnumList" );
    assertNonnull( "requiredInput" );
    assertNullable( "optionalInput" );
    assertNonnull( "requiredInputList" );
    assertNonnull( "requiredInputListContainingNulls" );
    assertNullable( "optionalInputList" );
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

  private void assertListFieldType( @Nonnull final String name, @Nonnull final Class<?> type )
    throws Exception
  {
    final Field field = AllTypesInput.class.getDeclaredField( name );
    final Type fieldType = field.getGenericType();
    assertTrue( fieldType instanceof ParameterizedType );
    final ParameterizedType genericType = (ParameterizedType) fieldType;
    assertEquals( genericType.getRawType(), List.class );
    assertEquals( genericType.getActualTypeArguments()[ 0 ], type );
  }

  private void assertFieldType( @Nonnull final String name, @Nonnull final Class<?> type )
    throws Exception
  {
    assertEquals( AllTypesInput.class.getDeclaredField( name ).getType(), type );
  }

  private void assertNullable( @Nonnull final String name )
    throws Exception
  {
    assertNotNull( AllTypesInput.class.getDeclaredField( name ).getAnnotation( Nullable.class ) );
    assertNull( AllTypesInput.class.getDeclaredField( name ).getAnnotation( Nonnull.class ) );
  }

  private void assertNonnull( @Nonnull final String name )
    throws Exception
  {
    assertNotNull( AllTypesInput.class.getDeclaredField( name ).getAnnotation( Nonnull.class ) );
    assertNull( AllTypesInput.class.getDeclaredField( name ).getAnnotation( Nullable.class ) );
  }

  private void assertNeitherNullableNorNonnull( @Nonnull final String name )
    throws Exception
  {
    assertNull( AllTypesInput.class.getDeclaredField( name ).getAnnotation( Nonnull.class ) );
    assertNull( AllTypesInput.class.getDeclaredField( name ).getAnnotation( Nullable.class ) );
  }
}
