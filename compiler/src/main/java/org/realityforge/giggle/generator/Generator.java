package org.realityforge.giggle.generator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
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

  void generate( @Nonnull GeneratorContext context )
    throws Exception;
}
