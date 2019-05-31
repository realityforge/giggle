package org.realityforge.giggle.generator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

public abstract class AbstractGeneratorRepository
{
  private final Map<String, Supplier<Generator>> _generators = new HashMap<>();

  public final void generate( @Nonnull final String name, @Nonnull final GeneratorContext context )
    throws NoSuchGeneratorException, GenerateException
  {
    final Generator generator = getGenerator( name );
    try
    {
      generator.generate( context );
    }
    catch ( final Throwable t )
    {
      throw new GenerateException( name, t );
    }
  }

  @Nonnull
  final Generator getGenerator( @Nonnull final String name )
    throws NoSuchGeneratorException
  {
    final Supplier<Generator> factory = _generators.get( name );
    if ( null == factory )
    {
      throw new NoSuchGeneratorException( name );
    }
    return factory.get();
  }

  final void registerGenerator( @Nonnull final String name, @Nonnull final Supplier<Generator> factory )
  {
    _generators.put( name, Objects.requireNonNull( factory ) );
  }
}
