package org.realityforge.giggle.generator;

import javax.annotation.Nonnull;

public interface Generator
{
  void generate( @Nonnull GeneratorContext context )
    throws Exception;
}
