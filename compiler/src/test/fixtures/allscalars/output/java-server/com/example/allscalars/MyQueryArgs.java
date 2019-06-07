package com.example.allscalars;

import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class MyQueryArgs {
  @Nullable
  private final Boolean optionalBoolean;

  @Nullable
  private final List<Boolean> optionalBooleanList;

  @Nullable
  private final MyEnum optionalEnum;

  @Nullable
  private final List<MyEnum> optionalEnumList;

  @Nullable
  private final Float optionalFloat;

  @Nullable
  private final List<Float> optionalFloatList;

  @Nullable
  private final String optionalID;

  @Nullable
  private final List<String> optionalIDList;

  @Nullable
  private final MyInput optionalInput;

  @Nullable
  private final List<MyInput> optionalInputList;

  @Nullable
  private final Integer optionalInt;

  @Nullable
  private final List<Integer> optionalIntList;

  @Nullable
  private final String optionalString;

  @Nullable
  private final List<String> optionalStringList;

  /**
   * A boolean that is required ... duh!
   */
  private final boolean requiredBoolean;

  @Nonnull
  private final List<Boolean> requiredBooleanList;

  @Nonnull
  private final List<Boolean> requiredBooleanListContainingNulls;

  @Nonnull
  private final MyEnum requiredEnum;

  @Nonnull
  private final List<MyEnum> requiredEnumList;

  @Nonnull
  private final List<MyEnum> requiredEnumListContainingNulls;

  private final float requiredFloat;

  @Nonnull
  private final List<Float> requiredFloatList;

  @Nonnull
  private final List<Float> requiredFloatListContainingNulls;

  @Nonnull
  private final String requiredID;

  @Nonnull
  private final List<String> requiredIDList;

  @Nonnull
  private final List<String> requiredIDListContainingNulls;

  @Nonnull
  private final MyInput requiredInput;

  @Nonnull
  private final List<MyInput> requiredInputList;

  @Nonnull
  private final List<MyInput> requiredInputListContainingNulls;

  private final int requiredInt;

  @Nonnull
  private final List<Integer> requiredIntList;

  @Nonnull
  private final List<Integer> requiredIntListContainingNulls;

  @Nonnull
  private final String requiredString;

  @Nonnull
  private final List<String> requiredStringList;

  @Nonnull
  private final List<String> requiredStringListContainingNulls;

  private MyQueryArgs(@Nullable final Boolean optionalBoolean,
      @Nullable final List<Boolean> optionalBooleanList, @Nullable final MyEnum optionalEnum,
      @Nullable final List<MyEnum> optionalEnumList, @Nullable final Float optionalFloat,
      @Nullable final List<Float> optionalFloatList, @Nullable final String optionalID,
      @Nullable final List<String> optionalIDList, @Nullable final MyInput optionalInput,
      @Nullable final List<MyInput> optionalInputList, @Nullable final Integer optionalInt,
      @Nullable final List<Integer> optionalIntList, @Nullable final String optionalString,
      @Nullable final List<String> optionalStringList, final boolean requiredBoolean,
      @Nonnull final List<Boolean> requiredBooleanList,
      @Nonnull final List<Boolean> requiredBooleanListContainingNulls,
      @Nonnull final MyEnum requiredEnum, @Nonnull final List<MyEnum> requiredEnumList,
      @Nonnull final List<MyEnum> requiredEnumListContainingNulls, final float requiredFloat,
      @Nonnull final List<Float> requiredFloatList,
      @Nonnull final List<Float> requiredFloatListContainingNulls, @Nonnull final String requiredID,
      @Nonnull final List<String> requiredIDList,
      @Nonnull final List<String> requiredIDListContainingNulls,
      @Nonnull final MyInput requiredInput, @Nonnull final List<MyInput> requiredInputList,
      @Nonnull final List<MyInput> requiredInputListContainingNulls, final int requiredInt,
      @Nonnull final List<Integer> requiredIntList,
      @Nonnull final List<Integer> requiredIntListContainingNulls,
      @Nonnull final String requiredString, @Nonnull final List<String> requiredStringList,
      @Nonnull final List<String> requiredStringListContainingNulls) {
    this.optionalBoolean = optionalBoolean;
    this.optionalBooleanList = optionalBooleanList;
    this.optionalEnum = optionalEnum;
    this.optionalEnumList = optionalEnumList;
    this.optionalFloat = optionalFloat;
    this.optionalFloatList = optionalFloatList;
    this.optionalID = optionalID;
    this.optionalIDList = optionalIDList;
    this.optionalInput = optionalInput;
    this.optionalInputList = optionalInputList;
    this.optionalInt = optionalInt;
    this.optionalIntList = optionalIntList;
    this.optionalString = optionalString;
    this.optionalStringList = optionalStringList;
    this.requiredBoolean = requiredBoolean;
    this.requiredBooleanList = Objects.requireNonNull( requiredBooleanList );
    this.requiredBooleanListContainingNulls = Objects.requireNonNull( requiredBooleanListContainingNulls );
    this.requiredEnum = Objects.requireNonNull( requiredEnum );
    this.requiredEnumList = Objects.requireNonNull( requiredEnumList );
    this.requiredEnumListContainingNulls = Objects.requireNonNull( requiredEnumListContainingNulls );
    this.requiredFloat = requiredFloat;
    this.requiredFloatList = Objects.requireNonNull( requiredFloatList );
    this.requiredFloatListContainingNulls = Objects.requireNonNull( requiredFloatListContainingNulls );
    this.requiredID = Objects.requireNonNull( requiredID );
    this.requiredIDList = Objects.requireNonNull( requiredIDList );
    this.requiredIDListContainingNulls = Objects.requireNonNull( requiredIDListContainingNulls );
    this.requiredInput = Objects.requireNonNull( requiredInput );
    this.requiredInputList = Objects.requireNonNull( requiredInputList );
    this.requiredInputListContainingNulls = Objects.requireNonNull( requiredInputListContainingNulls );
    this.requiredInt = requiredInt;
    this.requiredIntList = Objects.requireNonNull( requiredIntList );
    this.requiredIntListContainingNulls = Objects.requireNonNull( requiredIntListContainingNulls );
    this.requiredString = Objects.requireNonNull( requiredString );
    this.requiredStringList = Objects.requireNonNull( requiredStringList );
    this.requiredStringListContainingNulls = Objects.requireNonNull( requiredStringListContainingNulls );
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static MyQueryArgs from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final Boolean $giggle$_optionalBoolean = (Boolean) args.get( "optionalBoolean" );
    final List<Boolean> $giggle$_optionalBooleanList = (List<Boolean>) args.get( "optionalBooleanList" );
    final MyEnum $giggle$_optionalEnum = (MyEnum) args.get( "optionalEnum" );
    final List<MyEnum> $giggle$_optionalEnumList = (List<MyEnum>) args.get( "optionalEnumList" );
    final Float $giggle$_optionalFloat = (Float) args.get( "optionalFloat" );
    final List<Float> $giggle$_optionalFloatList = (List<Float>) args.get( "optionalFloatList" );
    final String $giggle$_optionalID = (String) args.get( "optionalID" );
    final List<String> $giggle$_optionalIDList = (List<String>) args.get( "optionalIDList" );
    final Map<String, Object> $giggle$_optionalInput = (Map<String, Object>) args.get( "optionalInput" );
    final List<Map<String, Object>> $giggle$_optionalInputList = (List<Map<String, Object>>) args.get( "optionalInputList" );
    final Integer $giggle$_optionalInt = (Integer) args.get( "optionalInt" );
    final List<Integer> $giggle$_optionalIntList = (List<Integer>) args.get( "optionalIntList" );
    final String $giggle$_optionalString = (String) args.get( "optionalString" );
    final List<String> $giggle$_optionalStringList = (List<String>) args.get( "optionalStringList" );
    final boolean $giggle$_requiredBoolean = (Boolean) args.get( "requiredBoolean" );
    final List<Boolean> $giggle$_requiredBooleanList = (List<Boolean>) args.get( "requiredBooleanList" );
    final List<Boolean> $giggle$_requiredBooleanListContainingNulls = (List<Boolean>) args.get( "requiredBooleanListContainingNulls" );
    final MyEnum $giggle$_requiredEnum = (MyEnum) args.get( "requiredEnum" );
    final List<MyEnum> $giggle$_requiredEnumList = (List<MyEnum>) args.get( "requiredEnumList" );
    final List<MyEnum> $giggle$_requiredEnumListContainingNulls = (List<MyEnum>) args.get( "requiredEnumListContainingNulls" );
    final float $giggle$_requiredFloat = (Float) args.get( "requiredFloat" );
    final List<Float> $giggle$_requiredFloatList = (List<Float>) args.get( "requiredFloatList" );
    final List<Float> $giggle$_requiredFloatListContainingNulls = (List<Float>) args.get( "requiredFloatListContainingNulls" );
    final String $giggle$_requiredID = (String) args.get( "requiredID" );
    final List<String> $giggle$_requiredIDList = (List<String>) args.get( "requiredIDList" );
    final List<String> $giggle$_requiredIDListContainingNulls = (List<String>) args.get( "requiredIDListContainingNulls" );
    final Map<String, Object> $giggle$_requiredInput = (Map<String, Object>) args.get( "requiredInput" );
    final List<Map<String, Object>> $giggle$_requiredInputList = (List<Map<String, Object>>) args.get( "requiredInputList" );
    final List<Map<String, Object>> $giggle$_requiredInputListContainingNulls = (List<Map<String, Object>>) args.get( "requiredInputListContainingNulls" );
    final int $giggle$_requiredInt = (Integer) args.get( "requiredInt" );
    final List<Integer> $giggle$_requiredIntList = (List<Integer>) args.get( "requiredIntList" );
    final List<Integer> $giggle$_requiredIntListContainingNulls = (List<Integer>) args.get( "requiredIntListContainingNulls" );
    final String $giggle$_requiredString = (String) args.get( "requiredString" );
    final List<String> $giggle$_requiredStringList = (List<String>) args.get( "requiredStringList" );
    final List<String> $giggle$_requiredStringListContainingNulls = (List<String>) args.get( "requiredStringListContainingNulls" );
    return new MyQueryArgs($giggle$_optionalBoolean, $giggle$_optionalBooleanList, $giggle$_optionalEnum, $giggle$_optionalEnumList, $giggle$_optionalFloat, $giggle$_optionalFloatList, $giggle$_optionalID, $giggle$_optionalIDList, MyInput.maybeFrom( $giggle$_optionalInput ), null == $giggle$_optionalInputList ? null : $giggle$_optionalInputList.stream().map( MyInput::maybeFrom ).collect( Collectors.toList() ), $giggle$_optionalInt, $giggle$_optionalIntList, $giggle$_optionalString, $giggle$_optionalStringList, $giggle$_requiredBoolean, $giggle$_requiredBooleanList, $giggle$_requiredBooleanListContainingNulls, $giggle$_requiredEnum, $giggle$_requiredEnumList, $giggle$_requiredEnumListContainingNulls, $giggle$_requiredFloat, $giggle$_requiredFloatList, $giggle$_requiredFloatListContainingNulls, $giggle$_requiredID, $giggle$_requiredIDList, $giggle$_requiredIDListContainingNulls, MyInput.from( $giggle$_requiredInput ), $giggle$_requiredInputList.stream().map( MyInput::from ).collect( Collectors.toList() ), $giggle$_requiredInputListContainingNulls.stream().map( MyInput::maybeFrom ).collect( Collectors.toList() ), $giggle$_requiredInt, $giggle$_requiredIntList, $giggle$_requiredIntListContainingNulls, $giggle$_requiredString, $giggle$_requiredStringList, $giggle$_requiredStringListContainingNulls);
  }

  @Nullable
  public Boolean getOptionalBoolean() {
    return optionalBoolean;
  }

  @Nullable
  public List<Boolean> getOptionalBooleanList() {
    return optionalBooleanList;
  }

  @Nullable
  public MyEnum getOptionalEnum() {
    return optionalEnum;
  }

  @Nullable
  public List<MyEnum> getOptionalEnumList() {
    return optionalEnumList;
  }

  @Nullable
  public Float getOptionalFloat() {
    return optionalFloat;
  }

  @Nullable
  public List<Float> getOptionalFloatList() {
    return optionalFloatList;
  }

  @Nullable
  public String getOptionalID() {
    return optionalID;
  }

  @Nullable
  public List<String> getOptionalIDList() {
    return optionalIDList;
  }

  @Nullable
  public MyInput getOptionalInput() {
    return optionalInput;
  }

  @Nullable
  public List<MyInput> getOptionalInputList() {
    return optionalInputList;
  }

  @Nullable
  public Integer getOptionalInt() {
    return optionalInt;
  }

  @Nullable
  public List<Integer> getOptionalIntList() {
    return optionalIntList;
  }

  @Nullable
  public String getOptionalString() {
    return optionalString;
  }

  @Nullable
  public List<String> getOptionalStringList() {
    return optionalStringList;
  }

  /**
   * A boolean that is required ... duh!
   */
  public boolean getRequiredBoolean() {
    return requiredBoolean;
  }

  @Nonnull
  public List<Boolean> getRequiredBooleanList() {
    return requiredBooleanList;
  }

  @Nonnull
  public List<Boolean> getRequiredBooleanListContainingNulls() {
    return requiredBooleanListContainingNulls;
  }

  @Nonnull
  public MyEnum getRequiredEnum() {
    return requiredEnum;
  }

  @Nonnull
  public List<MyEnum> getRequiredEnumList() {
    return requiredEnumList;
  }

  @Nonnull
  public List<MyEnum> getRequiredEnumListContainingNulls() {
    return requiredEnumListContainingNulls;
  }

  public float getRequiredFloat() {
    return requiredFloat;
  }

  @Nonnull
  public List<Float> getRequiredFloatList() {
    return requiredFloatList;
  }

  @Nonnull
  public List<Float> getRequiredFloatListContainingNulls() {
    return requiredFloatListContainingNulls;
  }

  @Nonnull
  public String getRequiredID() {
    return requiredID;
  }

  @Nonnull
  public List<String> getRequiredIDList() {
    return requiredIDList;
  }

  @Nonnull
  public List<String> getRequiredIDListContainingNulls() {
    return requiredIDListContainingNulls;
  }

  @Nonnull
  public MyInput getRequiredInput() {
    return requiredInput;
  }

  @Nonnull
  public List<MyInput> getRequiredInputList() {
    return requiredInputList;
  }

  @Nonnull
  public List<MyInput> getRequiredInputListContainingNulls() {
    return requiredInputListContainingNulls;
  }

  public int getRequiredInt() {
    return requiredInt;
  }

  @Nonnull
  public List<Integer> getRequiredIntList() {
    return requiredIntList;
  }

  @Nonnull
  public List<Integer> getRequiredIntListContainingNulls() {
    return requiredIntListContainingNulls;
  }

  @Nonnull
  public String getRequiredString() {
    return requiredString;
  }

  @Nonnull
  public List<String> getRequiredStringList() {
    return requiredStringList;
  }

  @Nonnull
  public List<String> getRequiredStringListContainingNulls() {
    return requiredStringListContainingNulls;
  }
}
