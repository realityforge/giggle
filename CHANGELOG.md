# Change Log

### Unreleased

### [v0.02](https://github.com/realityforge/giggle/tree/v0.02) (2019-06-11)
[Full Changelog](https://github.com/realityforge/giggle/compare/v0.01...v0.02)

#### java-server generator

* Remove final qualifier on `toString()`, `hashCode()` and `equals(Object)` methods in generated input classes.
  The final qualifier is unnecessary as the class is final.
* Fix bug where the private method `maybeCoerceID(...)` was generated on `*Args` type but was not used. This
  bug presented when when the type was `[ID!]! @numeric`.

### [v0.01](https://github.com/realityforge/giggle/tree/v0.01) (2019-06-11)
[Full Changelog](https://github.com/realityforge/giggle/compare/f99bb23e8e6ddeeeb17b79e337f84fdd539308a0...v0.01)

 ‎🎉	Initial super-alpha release ‎🎉.
