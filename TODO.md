# TODO

This document is essentially a list of shorthand notes describing work yet to completed.
Unfortunately it is not complete enough for other people to pick work off the list and
complete as there is too much un-said.

* Generate beans representing complete response. It should look something like;

```java
public class DispatchAnalysisDetailsQueryResponse
{
  public Data data;
  public GraphQLError[] errors;
}
```
  The `GraphQLError` should be generated and registered as a type. `Data` is what we currently represent
  as the response.

* Convert between plural values in response and singular type. Maybe should name it with the underlying types name
  unless an alias is present?

* Generate the jaxrs infrastructure for issuing a request. This would probably generate a `javax.ws.rs.client.Entity`
  based on the request and any supplied variables.

* Generate `input` types for jaxrs clients and a mechanism for serializing them.

* Generate interfaces for non-inline fragments and ensure generated containers implement interfaces.

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
