# TODO

This document is essentially a list of shorthand notes describing work yet to completed.
Unfortunately it is not complete enough for other people to pick work off the list and
complete as there is too much un-said.

* Add additional validation that operations must not be anonymous. This makes it easy to
  generate the appropriate code and is considered a "best" practice, even if it adds extra
  weight to the network call.

* Generate server-side client APIs as a combination of jackson annotated beans and fragment
  interfaces combined with jaxrs client endpoints.

* Generate interfaces for fragments or at least those fragments that have types (some fragments
  only have directives).

* If the project gets established consider attempting to acquire the abandoned `giggle`
  Github account: https://github.com/giggle

* Constraints to take into consideration during code generation:
  - Operations in a document should have unique names or there should be a single anonymous operation
  - Names within a schema are unique
  - Operations, Fragments (from the executable document) and Types can have the same name

* The tool should be built so that it is command driven to make it compatible with Bazel's
  [Persistent Worker](https://medium.com/@mmorearty/how-to-create-a-persistent-worker-for-bazel-7738bba2cabb)
  infrastructure.

  Each generation cycle is triggered by a command that includes the input files, the sha256 of
  the input files and any parameters controlling the generation. The generation should be deterministic.

* Add configuration files to control shape of output files.

* A good place to look for how to structure our services is:
  - [AWS Amplify](https://aws-amplify.github.io/docs/cli/graphql)

### Additional Notes

The following notes were extracted from the original issue that motivated the creation of this project.
While the issue was purely about generation of client-side artifacts, this project expanded to also
include server-side artifacts.

> Currently mapping the request and response objects for GraphQL operations is particularly laborious and error prone. For a single call the following steps typically need to be taken:
>
> * Write the query
> * Verify the query is valid in the context of the schema (which is a result of aggregating all the graphqls files)
> * Map the query to java and/or javascript classes (depending on whether the operation is being called by the browser or another server)
> * Potentially setup local caching, polling etc. of responses.
> * Setup infrastructure to invoke the operation.
>
> A simple query and the associated representation for a server based implementation using the American Express [nodes](https://github.com/americanexpress/nodes) library can be seen in ....
>
> A better approach is to read the queries and generate the required infrastructure code automagically. The first step is to generate the representation of requests and responses which is likely where the most manual, error prone work is present.
>
> Other tools on the market do similar things for different languages and contexts. We would also be sitting on top of parsing, validating and processing infrastructure provided by [graphql-java](https://www.graphql-java.com). Ideally we would also prepare for Bazel integration by supporting a [Persistent Worker](https://medium.com/@mmorearty/how-to-create-a-persistent-worker-for-bazel-7738bba2cabb) style interface.
>
> * [graphqlgen](https://github.com/prisma/graphqlgen) - schema first approach that generates resolvers (i.e. server side component) in `TypeScript`, `Flow` and `Reason` (soon ).
> * [gqlgen](https://github.com/99designs/gqlgen) is the Golang equivalent of graphqlgen.
> * [graphql-code-generator](https://github.com/dotansimha/graphql-code-generator) generates server-side resolvers, react client-side components, angular/apollo components, and more (TM)
> * [graphql_java_gen](https://github.com/Shopify/graphql_java_gen) generates client infrastructure in java for querying GraphQL endpoints programatically. Written using ruby/erb templates.
> * [apollo-android](https://github.com/apollographql/apollo-android) has support for generating some java classes and it integrates into their whole apollo infrastructure (i.e. caching, pipelining, calling etc).
