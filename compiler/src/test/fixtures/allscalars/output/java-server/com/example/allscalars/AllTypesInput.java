package com.example.allscalars;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An input type with most field types
 */
@Generated("org.realityforge.giggle.Main")
public final class AllTypesInput {
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
  private final MyEnum requiredEnum;

  @Nonnull
  private final List<MyEnum> requiredEnumList;

  private final float requiredFloat;

  @Nonnull
  private final List<Float> requiredFloatList;

  @Nonnull
  private final String requiredID;

  @Nonnull
  private final List<String> requiredIDList;

  @Nonnull
  private final MyInput requiredInput;

  @Nonnull
  private final List<MyInput> requiredInputList;

  private final int requiredInt;

  @Nonnull
  private final List<Integer> requiredIntList;

  @Nonnull
  private final String requiredString;

  @Nonnull
  private final List<String> requiredStringList;

  public AllTypesInput(@Nullable final Boolean optionalBoolean,
      @Nullable final List<Boolean> optionalBooleanList, @Nullable final MyEnum optionalEnum,
      @Nullable final List<MyEnum> optionalEnumList, @Nullable final Float optionalFloat,
      @Nullable final List<Float> optionalFloatList, @Nullable final String optionalID,
      @Nullable final List<String> optionalIDList, @Nullable final MyInput optionalInput,
      @Nullable final List<MyInput> optionalInputList, @Nullable final Integer optionalInt,
      @Nullable final List<Integer> optionalIntList, @Nullable final String optionalString,
      @Nullable final List<String> optionalStringList, final boolean requiredBoolean,
      @Nonnull final List<Boolean> requiredBooleanList, @Nonnull final MyEnum requiredEnum,
      @Nonnull final List<MyEnum> requiredEnumList, final float requiredFloat,
      @Nonnull final List<Float> requiredFloatList, @Nonnull final String requiredID,
      @Nonnull final List<String> requiredIDList, @Nonnull final MyInput requiredInput,
      @Nonnull final List<MyInput> requiredInputList, final int requiredInt,
      @Nonnull final List<Integer> requiredIntList, @Nonnull final String requiredString,
      @Nonnull final List<String> requiredStringList) {
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
    this.requiredBoolean = Objects.requireNonNull( requiredBoolean );
    this.requiredBooleanList = Objects.requireNonNull( requiredBooleanList );
    this.requiredEnum = Objects.requireNonNull( requiredEnum );
    this.requiredEnumList = Objects.requireNonNull( requiredEnumList );
    this.requiredFloat = Objects.requireNonNull( requiredFloat );
    this.requiredFloatList = Objects.requireNonNull( requiredFloatList );
    this.requiredID = Objects.requireNonNull( requiredID );
    this.requiredIDList = Objects.requireNonNull( requiredIDList );
    this.requiredInput = Objects.requireNonNull( requiredInput );
    this.requiredInputList = Objects.requireNonNull( requiredInputList );
    this.requiredInt = Objects.requireNonNull( requiredInt );
    this.requiredIntList = Objects.requireNonNull( requiredIntList );
    this.requiredString = Objects.requireNonNull( requiredString );
    this.requiredStringList = Objects.requireNonNull( requiredStringList );
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static AllTypesInput from(@Nonnull final Map<String, Object> args) {
    final Boolean optionalBoolean = (Boolean) args.get( "optionalBoolean" );
    final List<Boolean> optionalBooleanList = (List<Boolean>) args.get( "optionalBooleanList" );
    final MyEnum optionalEnum = (MyEnum) args.get( "optionalEnum" );
    final List<MyEnum> optionalEnumList = (List<MyEnum>) args.get( "optionalEnumList" );
    final Float optionalFloat = (Float) args.get( "optionalFloat" );
    final List<Float> optionalFloatList = (List<Float>) args.get( "optionalFloatList" );
    final String optionalID = (String) args.get( "optionalID" );
    final List<String> optionalIDList = (List<String>) args.get( "optionalIDList" );
    final Map<String, Object> optionalInput = (Map<String, Object>) args.get( "optionalInput" );
    final List<Map<String, Object>> optionalInputList = (List<Map<String, Object>>) args.get( "optionalInputList" );
    final Integer optionalInt = (Integer) args.get( "optionalInt" );
    final List<Integer> optionalIntList = (List<Integer>) args.get( "optionalIntList" );
    final String optionalString = (String) args.get( "optionalString" );
    final List<String> optionalStringList = (List<String>) args.get( "optionalStringList" );
    final boolean requiredBoolean = (Boolean) args.get( "requiredBoolean" );
    final List<Boolean> requiredBooleanList = (List<Boolean>) args.get( "requiredBooleanList" );
    final MyEnum requiredEnum = (MyEnum) args.get( "requiredEnum" );
    final List<MyEnum> requiredEnumList = (List<MyEnum>) args.get( "requiredEnumList" );
    final float requiredFloat = (Float) args.get( "requiredFloat" );
    final List<Float> requiredFloatList = (List<Float>) args.get( "requiredFloatList" );
    final String requiredID = (String) args.get( "requiredID" );
    final List<String> requiredIDList = (List<String>) args.get( "requiredIDList" );
    final Map<String, Object> requiredInput = (Map<String, Object>) args.get( "requiredInput" );
    final List<Map<String, Object>> requiredInputList = (List<Map<String, Object>>) args.get( "requiredInputList" );
    final int requiredInt = (Integer) args.get( "requiredInt" );
    final List<Integer> requiredIntList = (List<Integer>) args.get( "requiredIntList" );
    final String requiredString = (String) args.get( "requiredString" );
    final List<String> requiredStringList = (List<String>) args.get( "requiredStringList" );
    return new AllTypesInput(optionalBoolean, optionalBooleanList, optionalEnum, optionalEnumList, optionalFloat, optionalFloatList, optionalID, optionalIDList, null == optionalInput ? null : MyInput.from( optionalInput ), optionalInputList.stream().map( $element$ -> MyInput.from( $element$ ) ).collect( Collectors.toList() ), optionalInt, optionalIntList, optionalString, optionalStringList, requiredBoolean, requiredBooleanList, requiredEnum, requiredEnumList, requiredFloat, requiredFloatList, requiredID, requiredIDList, MyInput.from( requiredInput ), requiredInputList.stream().map( $element$ -> MyInput.from( $element$ ) ).collect( Collectors.toList() ), requiredInt, requiredIntList, requiredString, requiredStringList);
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
  public MyEnum getRequiredEnum() {
    return requiredEnum;
  }

  @Nonnull
  public List<MyEnum> getRequiredEnumList() {
    return requiredEnumList;
  }

  public float getRequiredFloat() {
    return requiredFloat;
  }

  @Nonnull
  public List<Float> getRequiredFloatList() {
    return requiredFloatList;
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
  public MyInput getRequiredInput() {
    return requiredInput;
  }

  @Nonnull
  public List<MyInput> getRequiredInputList() {
    return requiredInputList;
  }

  public int getRequiredInt() {
    return requiredInt;
  }

  @Nonnull
  public List<Integer> getRequiredIntList() {
    return requiredIntList;
  }

  @Nonnull
  public String getRequiredString() {
    return requiredString;
  }

  @Nonnull
  public List<String> getRequiredStringList() {
    return requiredStringList;
  }
}
