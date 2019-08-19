package org.realityforge.giggle.generator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.Set;
import javax.annotation.Nonnull;

public interface Generator
{
  @Documented
  @Target( ElementType.TYPE )
  @Retention( RetentionPolicy.RUNTIME )
  @interface MetaData
  {
    String name() default "<default>";
  }

  @Nonnull
  default Set<PropertyDef> getSupportedProperties()
  {
    return Collections.emptySet();
  }

  void generate( @Nonnull GeneratorContext context )
    throws Exception;
}
