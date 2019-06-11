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
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
    final Path schemaFile = FileUtil.getCurrentDirectory().resolve( "schema.graphqls" );
    final String resourceName =
      getClass().getName().replaceAll( "\\.[^.]+$", "" ).replace( ".", "/" ) + "/schema.graphqls";
    final InputStream inputStream = getClass().getResourceAsStream( "/" + resourceName );
    assertNotNull( inputStream, "Missing resource " + resourceName );
    IoUtil.copy( inputStream, new FileOutputStream( schemaFile.toFile() ) );
    return new SchemaRepository().getSchema( Collections.singletonList( schemaFile ) );
  }

  @SuppressWarnings( "unchecked" )
  protected final void assertTypeMapping( final Map<String, String> expectedTypeMapping )
    throws IOException
  {
    final InputStream types = getClass().getResourceAsStream( "types.mapping" );
    assertNotNull( types );
    final Properties properties = new Properties();
    properties.load( types );
    assertEquals( new HashMap<>( (Map) properties ), expectedTypeMapping );
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
