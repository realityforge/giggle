package org.realityforge.giggle.generator;

import java.util.ServiceLoader;

public final class GeneratorRepository
  extends AbstractGeneratorRepository
{
  public GeneratorRepository()
  {
    ServiceLoader.load( Generator.class ).forEach( this::registerGenerator );
  }
}
