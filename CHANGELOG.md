# Change Log

### Unreleased

* Remove reflection based integration tests (i.e. `EnumTest`) and related infrastructure (i.e. `AbstractIntegrationTest` and `CompileResults`) as fixture based tests combined with the codegen integration tests have proved to be a more effective mechanisms for testing output.

### [v0.03](https://github.com/realityforge/giggle/tree/v0.03) (2019-06-13)
[Full Changelog](https://github.com/realityforge/giggle/compare/v0.02...v0.03)

* Refactor the test infrastructure to support multiple generators and to process graphql documents as well as graphql schemas.

#### java-client generator

* Initial implementation of the `java-client` to generate the java native infrastructure for invoking GraphQL operations. The first phase generates:
  - the enum types for every enum in the schema.
  - a compact, minimized graphql document for each GraphQL operation that only contains the operation and any referenced fragments.
  - a java bean representing the response data for each GraphQL operation. The object form is designed to be compatible with JSON-B and Jackson json serialization formats. The response object contains fields for each selection in the operation and static inner classes for all complex sub-selections. The response object is named using the pattern `[OperationName][OperationType]Response`. i.e. The response object for the `event` query is represented by a bean named `EventQueryResponse` while the `updateEvent` mutation is named `UpdateEventMutationResponse`.

### [v0.02](https://github.com/realityforge/giggle/tree/v0.02) (2019-06-11)
[Full Changelog](https://github.com/realityforge/giggle/compare/v0.01...v0.02)

#### java-server generator

* Remove final qualifier on `toString()`, `hashCode()` and `equals(Object)` methods in generated input classes. The final qualifier is unnecessary as the class is final.
* Fix bug where the private method `maybeCoerceID(...)` was generated on `*Args` type but was not used. This bug presented when when the type was `[ID!]! @numeric`.

### [v0.01](https://github.com/realityforge/giggle/tree/v0.01) (2019-06-11)
[Full Changelog](https://github.com/realityforge/giggle/compare/f99bb23e8e6ddeeeb17b79e337f84fdd539308a0...v0.01)

 ‎🎉	Initial super-alpha release ‎🎉.
