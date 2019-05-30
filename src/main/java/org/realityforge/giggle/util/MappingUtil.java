package org.realityforge.giggle.util;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Nonnull;

public final class MappingUtil
{
  private MappingUtil()
  {
  }

  /**
   * Return a mapping between types from the supplied files.
   * The files are expected to be in properties file format and map a symbol
   * in graphql to a symbol in the target language.
   *
   * @param components the file components that are used to construct the mapping.
   * @return the mappings.
   * @throws IOException if there is an error reading a file component.
   */
  @Nonnull
  public static Map<String, String> getMapping( @Nonnull final List<Path> components )
    throws IOException
  {
    assert !components.isEmpty();
    final HashMap<String, String> mapping = new HashMap<>();
    final ArrayList<Properties> loaded = new ArrayList<>();
    for ( final Path component : components )
    {
      final Properties properties = new Properties();
      properties.load( new FileReader( component.toFile() ) );
      loaded.add( properties );
      for ( final String propertyName : properties.stringPropertyNames() )
      {
        if ( mapping.containsKey( propertyName ) )
        {
          for ( int i = 0; i < loaded.size(); i++ )
          {
            if ( loaded.get( i ).containsKey( propertyName ) )
            {
              //TODO: Change this exception
              throw new IOException( "Mapping key '" + propertyName + "' appears in multiple files: " +
                                     Arrays.asList( components.get( i ), component ) );
            }
          }
        }
        else
        {
          mapping.put( propertyName, properties.getProperty( propertyName ) );
        }
      }
    }
    return mapping;
  }
}
