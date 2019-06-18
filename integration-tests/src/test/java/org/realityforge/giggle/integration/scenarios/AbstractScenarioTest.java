package org.realityforge.giggle.integration.scenarios;

import gir.Gir;
import gir.Task;
import gir.io.FileUtil;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.json.bind.JsonbBuilder;
import static org.testng.Assert.*;

public abstract class AbstractScenarioTest
{
  protected final void inIsolatedDirectory( @Nonnull final Task task )
    throws Exception
  {
    Gir.go( () -> FileUtil.inTempDir( task ) );
  }

  @SuppressWarnings( "SameParameterValue" )
  @Nonnull
  private InputStream getResourceAsStream( @Nonnull final String name )
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
