package org.realityforge.giggle.generator.java.client;

import graphql.language.Definition;
import graphql.language.FragmentDefinition;
import graphql.language.OperationDefinition;
import java.io.IOException;
import javax.annotation.Nonnull;
import org.realityforge.giggle.generator.Generator;
import org.realityforge.giggle.generator.GeneratorContext;
import org.realityforge.giggle.generator.java.AbstractJavaGenerator;

@Generator.MetaData( name = "java-client" )
public class JavaClientGenerator
  extends AbstractJavaGenerator
{
  @Override
  public void generate( @Nonnull final GeneratorContext context )
    throws Exception
  {
    for ( final Definition definition : context.getDocument().getDefinitions() )
    {
      if ( definition instanceof FragmentDefinition )
      {
        emitFragment( context, (FragmentDefinition) definition );
      }
      else
      {
        assert definition instanceof OperationDefinition;
        emitOperation( context, (OperationDefinition) definition );
      }
    }
  }

  private void emitFragment( @Nonnull final GeneratorContext context, @Nonnull final FragmentDefinition fragment )
    throws IOException
  {
  }

  private void emitOperation( @Nonnull final GeneratorContext context, @Nonnull final OperationDefinition operation )
    throws IOException
  {
  }
}
