package org.realityforge.giggle.generator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;

public abstract class Generator
{
  @Documented
  @Target( ElementType.TYPE )
  @Retention( RetentionPolicy.RUNTIME )
  public @interface MetaData
  {
    String name() default "<default>";
  }

  @Nonnull
  public List<PropertyDef> getSupportedProperties()
  {
    return Collections.emptyList();
  }

  public abstract void generate( @Nonnull GeneratorContext context )
    throws Exception;
}
