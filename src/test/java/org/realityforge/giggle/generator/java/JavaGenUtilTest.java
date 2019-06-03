package org.realityforge.giggle.generator.java;

import com.squareup.javapoet.TypeSpec;
import gir.io.FileUtil;
import graphql.language.Document;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import org.realityforge.giggle.AbstractTest;
import org.realityforge.giggle.generator.GeneratorContext;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class JavaGenUtilTest
  extends AbstractTest
{
  @Test
  public void writeTopLevelType()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final Path dir = FileUtil.getCurrentDirectory();
      final GeneratorContext context =
        new GeneratorContext( buildGraphQLSchema( "" ),
                              Document.newDocument().build(),
                              Collections.emptyMap(),
                              Collections.emptyMap(),
                              dir,
                              "com.example" );
      final Path file = dir.resolve( "com/example/MyEnum.java" );

      assertFalse( file.toFile().exists() );

      JavaGenUtil.writeTopLevelType( context, TypeSpec.enumBuilder( "MyEnum" ).addEnumConstant( "Foo" ) );

      assertTrue( file.toFile().exists() );
      assertEquals( new String( Files.readAllBytes( file ), StandardCharsets.US_ASCII ),
                    "package com.example;\n" +
                    "\n" +
                    "enum MyEnum {\n" +
                    "  Foo\n" +
                    "}\n" );
    } );
  }
}
