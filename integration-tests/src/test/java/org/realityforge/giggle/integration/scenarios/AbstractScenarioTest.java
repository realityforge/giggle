package org.realityforge.giggle.integration.scenarios;

import gir.Gir;
import gir.Task;
import gir.io.FileUtil;
import gir.io.IoUtil;
import graphql.schema.GraphQLSchema;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.json.bind.JsonbBuilder;
import org.realityforge.giggle.schema.SchemaRepository;
import static org.testng.Assert.*;

public abstract class AbstractScenarioTest
{
  protected final void inIsolatedDirectory( @Nonnull final Task task )
    throws Exception
  {
    Gir.go( () -> FileUtil.inTempDir( task ) );
  }

  @Nonnull
  protected final GraphQLSchema loadSchema()
    throws IOException
  {
    return new SchemaRepository().getSchema( Collections.singletonList( getResourceAsFile( "schema.graphqls" ) ) );
  }

  @Nonnull
  protected final Path getResourceAsFile( @Nonnull final String name )
    throws IOException
  {
    final Path file = Files.createTempFile( FileUtil.createTempDir(), "", name );
    final InputStream inputStream = getResourceAsStream( name );
    IoUtil.copy( inputStream, new FileOutputStream( file.toFile() ) );
    return file;
  }

  @Nonnull
  protected final String getResourceAsString( @Nonnull final String name )
    throws IOException
  {
    final InputStream stream = getResourceAsStream( name );
    final byte[] data = new byte[ stream.available() ];
    assertEquals( stream.read( data ), data.length );
    return new String( data, StandardCharsets.US_ASCII );
  }

  @Nonnull
  protected final InputStream getResourceAsStream( @Nonnull final String name )
  {
    final InputStream inputStream = getClass().getResourceAsStream( name );
    assertNotNull( inputStream, "Missing resource " + name + " (relative to " + getClass().getName() + ")" );
    return inputStream;
  }

  @SuppressWarnings( "unchecked" )
  protected final void assertTypeMapping( @Nonnull final Map<String, String> expectedTypeMapping )
    throws IOException
  {
    final Properties properties = new Properties();
    properties.load( getResourceAsStream( "types.mapping" ) );
    assertEquals( new HashMap<>( (Map) properties ), expectedTypeMapping );
  }

  protected final <T> T fromJson( @Nonnull final String jsonData, @Nonnull final Class<T> type )
  {
    return JsonbBuilder.create().fromJson( jsonData, type );
  }

  protected final <T> T fromJsonResource( @Nonnull final String resourceName, @Nonnull final Class<T> type )
  {
    return JsonbBuilder.create().fromJson( getResourceAsStream( resourceName ), type );
  }

  protected final void assertListFieldType( @Nonnull final Class<?> clazz,
                                            @Nonnull final String name,
                                            @Nonnull final Class<?> componentType )
    throws Exception
  {
    final Field field = clazz.getDeclaredField( name );
    final Type fieldType = field.getGenericType();
    assertTrue( fieldType instanceof ParameterizedType );
    final ParameterizedType genericType = (ParameterizedType) fieldType;
    assertEquals( genericType.getRawType(), List.class );
    assertEquals( genericType.getActualTypeArguments()[ 0 ], componentType );
  }

  protected final void assertFieldType( @Nonnull final Class<?> clazz,
                                        @Nonnull final String name,
                                        @Nonnull final Class<?> type )
    throws Exception
  {
    assertEquals( clazz.getDeclaredField( name ).getType(), type );
  }

  protected final void assertNullable( @Nonnull final Class<?> clazz, @Nonnull final String name )
    throws Exception
  {
    assertNotNull( clazz.getDeclaredField( name ).getAnnotation( Nullable.class ) );
    assertNull( clazz.getDeclaredField( name ).getAnnotation( Nonnull.class ) );
  }

  protected final void assertNonnull( @Nonnull final Class<?> clazz, @Nonnull final String name )
    throws Exception
  {
    assertNotNull( clazz.getDeclaredField( name ).getAnnotation( Nonnull.class ) );
    assertNull( clazz.getDeclaredField( name ).getAnnotation( Nullable.class ) );
  }

  protected final void assertNeitherNullableNorNonnull( @Nonnull final Class<?> clazz, @Nonnull final String name )
    throws Exception
  {
    assertNull( clazz.getDeclaredField( name ).getAnnotation( Nonnull.class ) );
    assertNull( clazz.getDeclaredField( name ).getAnnotation( Nullable.class ) );
  }
}
