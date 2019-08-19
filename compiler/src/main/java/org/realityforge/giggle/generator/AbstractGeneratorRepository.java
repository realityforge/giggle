package org.realityforge.giggle.generator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;

public abstract class AbstractGeneratorRepository
{
  private final Map<String, Generator> _generators = new HashMap<>();

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
  public final Generator getGenerator( @Nonnull final String name )
    throws NoSuchGeneratorException
  {
    final Generator generator = _generators.get( name );
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
    final Class<? extends Generator> type = generator.getClass();
    final Generator.MetaData metaData = type.getAnnotation( Generator.MetaData.class );
    final String name = null == metaData ? "<default>" : metaData.name();
    final String actualName =
      "<default>".equals( name ) ? type.getSimpleName().replaceAll( "Generator$", "" ) : name;

    _generators.put( actualName, generator );
  }
}
