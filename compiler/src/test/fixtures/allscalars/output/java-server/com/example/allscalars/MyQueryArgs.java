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
  /**
   * A boolean that is required ... duh!
   */
  private final boolean requiredBoolean;

  @Nullable
  private final Boolean optionalBoolean;

  private final int requiredInt;

  @Nullable
  private final Integer optionalInt;

  private final float requiredFloat;

  @Nullable
  private final Float optionalFloat;

  @Nonnull
  private final String requiredString;

  @Nullable
  private final String optionalString;

  @Nonnull
  private final String requiredID;

  @Nullable
  private final String optionalID;

  @Nonnull
  private final MyEnum requiredEnum;

  @Nullable
  private final MyEnum optionalEnum;

  @Nonnull
  private final MyInput requiredInput;

  @Nullable
  private final MyInput optionalInput;

  @Nonnull
  private final List<Boolean> requiredBooleanList;

  @Nonnull
  private final List<Boolean> requiredBooleanListContainingNulls;

  @Nullable
  private final List<Boolean> optionalBooleanList;

  @Nonnull
  private final List<Integer> requiredIntList;

  @Nonnull
  private final List<Integer> requiredIntListContainingNulls;

  @Nullable
  private final List<Integer> optionalIntList;

  @Nonnull
  private final List<Float> requiredFloatList;

  @Nonnull
  private final List<Float> requiredFloatListContainingNulls;

  @Nullable
  private final List<Float> optionalFloatList;

  @Nonnull
  private final List<String> requiredStringList;

  @Nonnull
  private final List<String> requiredStringListContainingNulls;

  @Nullable
  private final List<String> optionalStringList;

  @Nonnull
  private final List<String> requiredIDList;

  @Nonnull
  private final List<String> requiredIDListContainingNulls;

  @Nullable
  private final List<String> optionalIDList;

  @Nonnull
  private final List<MyEnum> requiredEnumList;

  @Nonnull
  private final List<MyEnum> requiredEnumListContainingNulls;

  @Nullable
  private final List<MyEnum> optionalEnumList;

  @Nonnull
  private final List<MyInput> requiredInputList;

  @Nonnull
  private final List<MyInput> requiredInputListContainingNulls;

  @Nullable
  private final List<MyInput> optionalInputList;

  private MyQueryArgs(final boolean requiredBoolean, @Nullable final Boolean optionalBoolean,
      final int requiredInt, @Nullable final Integer optionalInt, final float requiredFloat,
      @Nullable final Float optionalFloat, @Nonnull final String requiredString,
      @Nullable final String optionalString, @Nonnull final String requiredID,
      @Nullable final String optionalID, @Nonnull final MyEnum requiredEnum,
      @Nullable final MyEnum optionalEnum, @Nonnull final MyInput requiredInput,
      @Nullable final MyInput optionalInput, @Nonnull final List<Boolean> requiredBooleanList,
      @Nonnull final List<Boolean> requiredBooleanListContainingNulls,
      @Nullable final List<Boolean> optionalBooleanList,
      @Nonnull final List<Integer> requiredIntList,
      @Nonnull final List<Integer> requiredIntListContainingNulls,
      @Nullable final List<Integer> optionalIntList, @Nonnull final List<Float> requiredFloatList,
      @Nonnull final List<Float> requiredFloatListContainingNulls,
      @Nullable final List<Float> optionalFloatList, @Nonnull final List<String> requiredStringList,
      @Nonnull final List<String> requiredStringListContainingNulls,
      @Nullable final List<String> optionalStringList, @Nonnull final List<String> requiredIDList,
      @Nonnull final List<String> requiredIDListContainingNulls,
      @Nullable final List<String> optionalIDList, @Nonnull final List<MyEnum> requiredEnumList,
      @Nonnull final List<MyEnum> requiredEnumListContainingNulls,
      @Nullable final List<MyEnum> optionalEnumList, @Nonnull final List<MyInput> requiredInputList,
      @Nonnull final List<MyInput> requiredInputListContainingNulls,
      @Nullable final List<MyInput> optionalInputList) {
    this.requiredBoolean = requiredBoolean;
    this.optionalBoolean = optionalBoolean;
    this.requiredInt = requiredInt;
    this.optionalInt = optionalInt;
    this.requiredFloat = requiredFloat;
    this.optionalFloat = optionalFloat;
    this.requiredString = Objects.requireNonNull( requiredString );
    this.optionalString = optionalString;
    this.requiredID = Objects.requireNonNull( requiredID );
    this.optionalID = optionalID;
    this.requiredEnum = Objects.requireNonNull( requiredEnum );
    this.optionalEnum = optionalEnum;
    this.requiredInput = Objects.requireNonNull( requiredInput );
    this.optionalInput = optionalInput;
    this.requiredBooleanList = Objects.requireNonNull( requiredBooleanList );
    this.requiredBooleanListContainingNulls = Objects.requireNonNull( requiredBooleanListContainingNulls );
    this.optionalBooleanList = optionalBooleanList;
    this.requiredIntList = Objects.requireNonNull( requiredIntList );
    this.requiredIntListContainingNulls = Objects.requireNonNull( requiredIntListContainingNulls );
    this.optionalIntList = optionalIntList;
    this.requiredFloatList = Objects.requireNonNull( requiredFloatList );
    this.requiredFloatListContainingNulls = Objects.requireNonNull( requiredFloatListContainingNulls );
    this.optionalFloatList = optionalFloatList;
    this.requiredStringList = Objects.requireNonNull( requiredStringList );
    this.requiredStringListContainingNulls = Objects.requireNonNull( requiredStringListContainingNulls );
    this.optionalStringList = optionalStringList;
    this.requiredIDList = Objects.requireNonNull( requiredIDList );
    this.requiredIDListContainingNulls = Objects.requireNonNull( requiredIDListContainingNulls );
    this.optionalIDList = optionalIDList;
    this.requiredEnumList = Objects.requireNonNull( requiredEnumList );
    this.requiredEnumListContainingNulls = Objects.requireNonNull( requiredEnumListContainingNulls );
    this.optionalEnumList = optionalEnumList;
    this.requiredInputList = Objects.requireNonNull( requiredInputList );
    this.requiredInputListContainingNulls = Objects.requireNonNull( requiredInputListContainingNulls );
    this.optionalInputList = optionalInputList;
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public static MyQueryArgs from(@Nonnull final DataFetchingEnvironment environment) {
    final Map<String, Object> args = environment.getArguments();
    final boolean $giggle$_requiredBoolean = (Boolean) args.get( "requiredBoolean" );
    final Boolean $giggle$_optionalBoolean = (Boolean) args.get( "optionalBoolean" );
    final int $giggle$_requiredInt = (Integer) args.get( "requiredInt" );
    final Integer $giggle$_optionalInt = (Integer) args.get( "optionalInt" );
    final float $giggle$_requiredFloat = (Float) args.get( "requiredFloat" );
    final Float $giggle$_optionalFloat = (Float) args.get( "optionalFloat" );
    final String $giggle$_requiredString = (String) args.get( "requiredString" );
    final String $giggle$_optionalString = (String) args.get( "optionalString" );
    final String $giggle$_requiredID = (String) args.get( "requiredID" );
    final String $giggle$_optionalID = (String) args.get( "optionalID" );
    final MyEnum $giggle$_requiredEnum = (MyEnum) args.get( "requiredEnum" );
    final MyEnum $giggle$_optionalEnum = (MyEnum) args.get( "optionalEnum" );
    final Map<String, Object> $giggle$_requiredInput = (Map<String, Object>) args.get( "requiredInput" );
    final Map<String, Object> $giggle$_optionalInput = (Map<String, Object>) args.get( "optionalInput" );
    final List<Boolean> $giggle$_requiredBooleanList = (List<Boolean>) args.get( "requiredBooleanList" );
    final List<Boolean> $giggle$_requiredBooleanListContainingNulls = (List<Boolean>) args.get( "requiredBooleanListContainingNulls" );
    final List<Boolean> $giggle$_optionalBooleanList = (List<Boolean>) args.get( "optionalBooleanList" );
    final List<Integer> $giggle$_requiredIntList = (List<Integer>) args.get( "requiredIntList" );
    final List<Integer> $giggle$_requiredIntListContainingNulls = (List<Integer>) args.get( "requiredIntListContainingNulls" );
    final List<Integer> $giggle$_optionalIntList = (List<Integer>) args.get( "optionalIntList" );
    final List<Float> $giggle$_requiredFloatList = (List<Float>) args.get( "requiredFloatList" );
    final List<Float> $giggle$_requiredFloatListContainingNulls = (List<Float>) args.get( "requiredFloatListContainingNulls" );
    final List<Float> $giggle$_optionalFloatList = (List<Float>) args.get( "optionalFloatList" );
    final List<String> $giggle$_requiredStringList = (List<String>) args.get( "requiredStringList" );
    final List<String> $giggle$_requiredStringListContainingNulls = (List<String>) args.get( "requiredStringListContainingNulls" );
    final List<String> $giggle$_optionalStringList = (List<String>) args.get( "optionalStringList" );
    final List<String> $giggle$_requiredIDList = (List<String>) args.get( "requiredIDList" );
    final List<String> $giggle$_requiredIDListContainingNulls = (List<String>) args.get( "requiredIDListContainingNulls" );
    final List<String> $giggle$_optionalIDList = (List<String>) args.get( "optionalIDList" );
    final List<MyEnum> $giggle$_requiredEnumList = (List<MyEnum>) args.get( "requiredEnumList" );
    final List<MyEnum> $giggle$_requiredEnumListContainingNulls = (List<MyEnum>) args.get( "requiredEnumListContainingNulls" );
    final List<MyEnum> $giggle$_optionalEnumList = (List<MyEnum>) args.get( "optionalEnumList" );
    final List<Map<String, Object>> $giggle$_requiredInputList = (List<Map<String, Object>>) args.get( "requiredInputList" );
    final List<Map<String, Object>> $giggle$_requiredInputListContainingNulls = (List<Map<String, Object>>) args.get( "requiredInputListContainingNulls" );
    final List<Map<String, Object>> $giggle$_optionalInputList = (List<Map<String, Object>>) args.get( "optionalInputList" );
    return new MyQueryArgs($giggle$_requiredBoolean, $giggle$_optionalBoolean, $giggle$_requiredInt, $giggle$_optionalInt, $giggle$_requiredFloat, $giggle$_optionalFloat, $giggle$_requiredString, $giggle$_optionalString, $giggle$_requiredID, $giggle$_optionalID, $giggle$_requiredEnum, $giggle$_optionalEnum, MyInput.from( $giggle$_requiredInput ), MyInput.maybeFrom( $giggle$_optionalInput ), $giggle$_requiredBooleanList, $giggle$_requiredBooleanListContainingNulls, $giggle$_optionalBooleanList, $giggle$_requiredIntList, $giggle$_requiredIntListContainingNulls, $giggle$_optionalIntList, $giggle$_requiredFloatList, $giggle$_requiredFloatListContainingNulls, $giggle$_optionalFloatList, $giggle$_requiredStringList, $giggle$_requiredStringListContainingNulls, $giggle$_optionalStringList, $giggle$_requiredIDList, $giggle$_requiredIDListContainingNulls, $giggle$_optionalIDList, $giggle$_requiredEnumList, $giggle$_requiredEnumListContainingNulls, $giggle$_optionalEnumList, $giggle$_requiredInputList.stream().map( MyInput::from ).collect( Collectors.toList() ), $giggle$_requiredInputListContainingNulls.stream().map( MyInput::maybeFrom ).collect( Collectors.toList() ), null == $giggle$_optionalInputList ? null : $giggle$_optionalInputList.stream().map( MyInput::maybeFrom ).collect( Collectors.toList() ));
  }

  /**
   * A boolean that is required ... duh!
   */
  public boolean getRequiredBoolean() {
    return requiredBoolean;
  }

  @Nullable
  public Boolean getOptionalBoolean() {
    return optionalBoolean;
  }

  public int getRequiredInt() {
    return requiredInt;
  }

  @Nullable
  public Integer getOptionalInt() {
    return optionalInt;
  }

  public float getRequiredFloat() {
    return requiredFloat;
  }

  @Nullable
  public Float getOptionalFloat() {
    return optionalFloat;
  }

  @Nonnull
  public String getRequiredString() {
    return requiredString;
  }

  @Nullable
  public String getOptionalString() {
    return optionalString;
  }

  @Nonnull
  public String getRequiredID() {
    return requiredID;
  }

  @Nullable
  public String getOptionalID() {
    return optionalID;
  }

  @Nonnull
  public MyEnum getRequiredEnum() {
    return requiredEnum;
  }

  @Nullable
  public MyEnum getOptionalEnum() {
    return optionalEnum;
  }

  @Nonnull
  public MyInput getRequiredInput() {
    return requiredInput;
  }

  @Nullable
  public MyInput getOptionalInput() {
    return optionalInput;
  }

  @Nonnull
  public List<Boolean> getRequiredBooleanList() {
    return requiredBooleanList;
  }

  @Nonnull
  public List<Boolean> getRequiredBooleanListContainingNulls() {
    return requiredBooleanListContainingNulls;
  }

  @Nullable
  public List<Boolean> getOptionalBooleanList() {
    return optionalBooleanList;
  }

  @Nonnull
  public List<Integer> getRequiredIntList() {
    return requiredIntList;
  }

  @Nonnull
  public List<Integer> getRequiredIntListContainingNulls() {
    return requiredIntListContainingNulls;
  }

  @Nullable
  public List<Integer> getOptionalIntList() {
    return optionalIntList;
  }

  @Nonnull
  public List<Float> getRequiredFloatList() {
    return requiredFloatList;
  }

  @Nonnull
  public List<Float> getRequiredFloatListContainingNulls() {
    return requiredFloatListContainingNulls;
  }

  @Nullable
  public List<Float> getOptionalFloatList() {
    return optionalFloatList;
  }

  @Nonnull
  public List<String> getRequiredStringList() {
    return requiredStringList;
  }

  @Nonnull
  public List<String> getRequiredStringListContainingNulls() {
    return requiredStringListContainingNulls;
  }

  @Nullable
  public List<String> getOptionalStringList() {
    return optionalStringList;
  }

  @Nonnull
  public List<String> getRequiredIDList() {
    return requiredIDList;
  }

  @Nonnull
  public List<String> getRequiredIDListContainingNulls() {
    return requiredIDListContainingNulls;
  }

  @Nullable
  public List<String> getOptionalIDList() {
    return optionalIDList;
  }

  @Nonnull
  public List<MyEnum> getRequiredEnumList() {
    return requiredEnumList;
  }

  @Nonnull
  public List<MyEnum> getRequiredEnumListContainingNulls() {
    return requiredEnumListContainingNulls;
  }

  @Nullable
  public List<MyEnum> getOptionalEnumList() {
    return optionalEnumList;
  }

  @Nonnull
  public List<MyInput> getRequiredInputList() {
    return requiredInputList;
  }

  @Nonnull
  public List<MyInput> getRequiredInputListContainingNulls() {
    return requiredInputListContainingNulls;
  }

  @Nullable
  public List<MyInput> getOptionalInputList() {
    return optionalInputList;
  }
}
