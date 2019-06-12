package org.realityforge.giggle.generator.java.client;

import graphql.language.Definition;
import graphql.language.FragmentDefinition;
import graphql.language.OperationDefinition;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLType;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.realityforge.giggle.generator.Generator;
import org.realityforge.giggle.generator.GeneratorContext;
import org.realityforge.giggle.generator.java.AbstractJavaGenerator;

@SuppressWarnings( "Duplicates" )
@Generator.MetaData( name = "java-client" )
public class JavaClientGenerator
  extends AbstractJavaGenerator
{
  @Override
  public void generate( @Nonnull final GeneratorContext context )
    throws Exception
  {
    final Map<GraphQLType, String> inputTypeMap = buildTypeMapping( context );
    final Map<GraphQLType, String> generatedTypeMap = new HashMap<>();
    final GraphQLSchema schema = context.getSchema();
    final List<GraphQLType> types =
      schema.getAllTypesAsList()
        .stream()
        .filter( this::isNotIntrospectionType )
        .filter( t -> !inputTypeMap.containsKey( t ) )
        .collect( Collectors.toList() );
    for ( final GraphQLType type : types )
    {
      if ( type instanceof GraphQLEnumType )
      {
        generatedTypeMap.put( type, context.getPackageName() + "." + type.getName() );
      }
    }
    final Map<GraphQLType, String> fullTypeMap = new HashMap<>();
    fullTypeMap.putAll( inputTypeMap );
    fullTypeMap.putAll( generatedTypeMap );
    for ( final GraphQLType type : types )
    {
      if ( type instanceof GraphQLEnumType )
      {
        emitEnum( context, (GraphQLEnumType) type );
      }
    }
    for ( final Definition definition : context.getDocument().getDefinitions() )
    {
      if ( definition instanceof FragmentDefinition )
      {
        emitFragment( context, (FragmentDefinition) definition );
      }
      else
      {
        assert definition instanceof OperationDefinition;
        emitOperation( context, fullTypeMap, (OperationDefinition) definition );
      }
    }
    writeTypeMappingFile( context, generatedTypeMap );
  }

  private void emitFragment( @Nonnull final GeneratorContext context, @Nonnull final FragmentDefinition fragment )
    throws IOException
  {
  }

  private void emitOperation( @Nonnull final GeneratorContext context,
                              @Nonnull final Map<GraphQLType, String> typeMap,
                              @Nonnull final OperationDefinition operation )
    throws IOException
  {
  }
}
