package org.realityforge.giggle.schema;

import graphql.language.ScalarTypeDefinition;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.ScalarInfo;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.validation.SchemaValidator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.realityforge.giggle.util.HashUtil;

/**
 * Container responsible for building and caching instances of GraphQLSchema.
 */
public final class SchemaRepository
{
  @Nonnull
  private final SchemaParser _schemaParser = new SchemaParser();
  @Nonnull
  private final SchemaGenerator _schemaGenerator = new SchemaGenerator();
  @Nonnull
  private final Map<String, WeakReference<GraphQLSchema>> _schemas = new HashMap<>();
  private final SchemaValidator _validator = new SchemaValidator();

  /**
   * Return a GraphQLSchema instance constructed from the supplied files.
   * If the contents of the files and the order of the files has not changed
   * between calls then a cached instance may be returned.
   *
   * @param components the file components that are used to construct the schema.
   * @return a GraphQLSchema instance.
   */
  @Nonnull
  public GraphQLSchema getSchema( @Nonnull final List<Path> components )
  {

    final List<byte[]> schemaBytes = loadFiles( components );
    final String id = deriveId( schemaBytes );

    final WeakReference<GraphQLSchema> reference = _schemas.get( id );
    if ( null != reference )
    {
      final GraphQLSchema schema = reference.get();
      if ( null != schema )
      {
        return schema;
      }
    }
    final TypeDefinitionRegistry typeRegistry = buildTypeDefinitionRegistry( schemaBytes );

    final RuntimeWiring runtimeWiring = buildRuntimeWiring( typeRegistry );

    final GraphQLSchema schema = _schemaGenerator.makeExecutableSchema( typeRegistry, runtimeWiring );
    _validator.validateSchema( schema );
    _schemas.put( id, new WeakReference<>( schema ) );
    return schema;
  }

  @Nonnull
  private RuntimeWiring buildRuntimeWiring( @Nonnull final TypeDefinitionRegistry typeRegistry )
  {
    final RuntimeWiring.Builder builder = RuntimeWiring.newRuntimeWiring();
    for ( final ScalarTypeDefinition scalar : typeRegistry.scalars().values() )
    {
      if ( !ScalarInfo.isStandardScalar( scalar.getName() ) )
      {
        builder.scalar( GraphQLScalarType.newScalar()
                          .name( scalar.getName() )
                          .description( scalar.getName() )
                          .coercing( new NoopCoercing() )
                          .build() );
      }
    }

    return builder.build();
  }

  @Nonnull
  private TypeDefinitionRegistry buildTypeDefinitionRegistry( @Nonnull final List<byte[]> schemaBytes )
  {
    final TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
    for ( final byte[] data : schemaBytes )
    {
      final ByteArrayInputStream input = new ByteArrayInputStream( data );
      final InputStreamReader reader = new InputStreamReader( input, StandardCharsets.UTF_8 );
      typeRegistry.merge( _schemaParser.parse( reader ) );
    }
    return typeRegistry;
  }

  @Nonnull
  private String deriveId( @Nonnull final List<byte[]> schemaBytes )
  {
    // Produce a hash that combines hashes of all files
    return HashUtil.sha256( schemaBytes.stream()
                              .map( HashUtil::sha256 )
                              .collect( Collectors.joining() )
                              .getBytes( StandardCharsets.US_ASCII ) );
  }

  @Nonnull
  private List<byte[]> loadFiles( @Nonnull final List<Path> components )
  {
    final ArrayList<byte[]> schemaBytes = new ArrayList<>();
    for ( final Path omponent : components )
    {
      try
      {
        schemaBytes.add( Files.readAllBytes( omponent ) );
      }
      catch ( final IOException ioe )
      {
        throw new IllegalStateException( "Error reading schema file " + omponent, ioe );
      }
    }
    return schemaBytes;
  }
}
