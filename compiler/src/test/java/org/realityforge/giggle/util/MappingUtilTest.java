package org.realityforge.giggle.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.realityforge.giggle.AbstractTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class MappingUtilTest
  extends AbstractTest
{
  @Test
  public void getMapping_singleFile()
    throws Exception
  {
    final Path file =
      writeContent( "mapping.properties",
                    "MyType=com.example.model.MyType\n" +
                    "MyOtherType=com.example.model.MyOtherType\n" );

    final Map<String, String> mapping = MappingUtil.getMapping( Collections.singletonList( file ) );
    assertEquals( mapping.size(), 2 );
    assertEquals( mapping.get( "MyType" ), "com.example.model.MyType" );
    assertEquals( mapping.get( "MyOtherType" ), "com.example.model.MyOtherType" );
  }

  @Test
  public void getMapping_multipleFile()
    throws Exception
  {
    final Path file1 =
      writeContent( "mapping1.properties", "MyType=com.example.model.MyType\n" );
    final Path file2 =
      writeContent( "mapping2.properties", "MyOtherType=com.example.model.MyOtherType\n" );

    final Map<String, String> mapping = MappingUtil.getMapping( Arrays.asList( file1, file2 ) );
    assertEquals( mapping.size(), 2 );
    assertEquals( mapping.get( "MyType" ), "com.example.model.MyType" );
    assertEquals( mapping.get( "MyOtherType" ), "com.example.model.MyOtherType" );
  }

  @Test
  public void getMapping_multipleMappingsInDifferentFiles()
    throws Exception
  {
    final Path file1 =
      writeContent( "mapping1.properties", "MyType=com.example.model.MyType\n" );
    final Path file2 =
      writeContent( "mapping2.properties", "MyType=com.example.model.MyType2\n" );

    final IOException exception =
      expectThrows( IOException.class, () -> MappingUtil.getMapping( Arrays.asList( file1, file2 ) ) );
    assertEquals( exception.getMessage(),
                  "Mapping key 'MyType' appears in multiple files: " + Arrays.asList( file1, file2 ) );
  }
}
