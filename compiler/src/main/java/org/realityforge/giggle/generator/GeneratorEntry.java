package org.realityforge.giggle.generator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

public final class GeneratorEntry
{
  @Nonnull
  private final String _name;
  @Nonnull
  private final Map<String, PropertyDef> _properties;
  @Nonnull
  private final Generator _generator;

  public GeneratorEntry( @Nonnull final Generator generator )
  {
    final Class<? extends Generator> type = generator.getClass();
    final Generator.MetaData metaData = type.getAnnotation( Generator.MetaData.class );
    final String name = null == metaData ? "<default>" : metaData.name();

    _name = "<default>".equals( name ) ? type.getSimpleName().replaceAll( "Generator$", "" ) : name;
    _generator = Objects.requireNonNull( generator );
    final HashMap<String, PropertyDef> map = new HashMap<>();
    for ( final PropertyDef propertyDef : generator.getSupportedProperties() )
    {
      map.put( propertyDef.getKey(), propertyDef );
    }
    _properties = Collections.unmodifiableMap( map );
  }

  @Nonnull
  public String getName()
  {
    return _name;
  }

  @Nonnull
  public Map<String, PropertyDef> getSupportedProperties()
  {
    return _properties;
  }

  @Nonnull
  public Generator getGenerator()
  {
    return _generator;
  }

  public void generate( @Nonnull final GlobalGeneratorContext context )
    throws GenerateException
  {
    try
    {
      getGenerator().generate( new GeneratorContext( this, context ) );
    }
    catch ( final Throwable t )
    {
      throw new GenerateException( getName(), t.getMessage(), t );
    }
  }

  @Nonnull
  public PropertyDef getProperty( @Nonnull final String name )
  {
    final PropertyDef propertyDef = _properties.get( name );
    if ( null == propertyDef )
    {
      final String message =
        "Generator named '" + getName() + "' attempted to access property named '" + name +
        "' but did not declare property";
      throw new IllegalStateException( message );
    }
    return propertyDef;
  }
}
