package org.realityforge.giggle.generator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;

public abstract class AbstractGeneratorRepository
{
  private final Map<String, GeneratorEntry> _generators = new HashMap<>();

  @Nonnull
  public final GeneratorEntry getGenerator( @Nonnull final String name )
    throws NoSuchGeneratorException
  {
    final GeneratorEntry generator = _generators.get( name );
    if ( null == generator )
    {
      throw new NoSuchGeneratorException( name );
    }
    return generator;
  }

  @Nonnull
  public final Set<String> getGeneratorNames()
  {
    return _generators.keySet();
  }

  public final void registerGenerator( @Nonnull final Generator generator )
  {
    final GeneratorEntry entry = new GeneratorEntry( generator );
    _generators.put( entry.getName(), entry );
  }
}
