# TODO

This document is essentially a list of shorthand notes describing work yet to be completed.
Unfortunately it is not complete enough for other people to pick work off the list and
complete as there is too much un-said.

* Convert between plural values in response and singular type. Maybe should name it with the underlying types name
  unless an alias is present? Probably we use the type if there is only one field of that type, otherwise we prefix
  type with name and/or alias or some other combination.

* Add support for operations that returns
  - a scalar
  - a list of scalars
  - a list of enums
  - a list of objectTypes

* Expose supported properties via annotations on type rather than a method on an instances.

* Replace `@numeric` with an `integers.txt` that just lists the fully qualified names of `ID`
  `Variables`/`Fields`/`InputFields` that should be numeric. The tool should detect any unused entries. The `@numeric`
  directive should be removed from domgen.

* Generate interfaces for non-inline fragments and ensure generated containers implement interfaces. If the container
  only contains a fragment then the getters that return container instances should be typed with the interface.

* If the project gets established consider attempting to acquire the abandoned `giggle`
  Github account: https://github.com/giggle

* The tool should be built so that it is command driven to make it compatible with Bazel's
  [Persistent Worker](https://medium.com/@mmorearty/how-to-create-a-persistent-worker-for-bazel-7738bba2cabb)
  infrastructure.

  Each generation cycle is triggered by a command that includes the input files, the sha256 of
  the input files and any parameters controlling the generation. The generation should be deterministic.

* Add configuration files to control shape of output files.

* Optionally generate server-side entities as one of the following forms. The form is probably driven by
  either a directive in the schema or a configuration/mapping file.
  - `Map<String,Object>`
  - Resolver interface such as;
```java
interface PhysicalUnit {
  DataFetcher<Integer> id();
  DataFetcher<PhysicalUnitType> type();
  DataFetcher<String> name();
  DataFetcher<String> globalId();
  DataFetcher<Iterable<Attribute>> attributes();
}
```
  - `"Bean"` interface such as;
```java
interface PhysicalUnit {
  int getId();
  PhysicalUnitType getType();
  String getName();
  String getGlobalId();
  List<Attribute> getAttributes();
}
```
  - `"Bean"` class such as;
```java
public final class PhysicalUnit {
  public int getId() { return id; }
  public PhysicalUnitType getType() { return type; }
  public String getName() { return name; }
  public String getGlobalId() { return globalId; }
  public List<Attribute> getAttributes() { return attributes; }
}
```

* A good place to look for how to structure our services is:
  - [AWS Amplify](https://aws-amplify.github.io/docs/cli/graphql)

* Another code generator that has not been investigated is [graphql-apigen](https://github.com/Distelli/graphql-apigen).
