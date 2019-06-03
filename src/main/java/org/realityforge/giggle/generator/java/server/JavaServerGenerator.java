package org.realityforge.giggle.generator.java.server;

import com.google.auto.service.AutoService;
import javax.annotation.Nonnull;
import org.realityforge.giggle.generator.Generator;
import org.realityforge.giggle.generator.GeneratorContext;

@AutoService( Generator.class )
@Generator.MetaData( name = "java-server" )
public class JavaServerGenerator
  implements Generator
{
  @Override
  public void generate( @Nonnull final GeneratorContext context )
    throws Exception
  {
  }
}
