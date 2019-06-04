package org.realityforge.giggle.generator.java;

import gir.io.FileUtil;
import graphql.introspection.Introspection;
import graphql.language.Document;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLType;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import org.realityforge.giggle.AbstractTest;
import org.realityforge.giggle.generator.GeneratorContext;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class JavaGeneratorTest
  extends AbstractTest
{
  private static class MyTestJavaGenerator
    extends AbstractJavaGenerator
  {
    @Override
    public void generate( @Nonnull final GeneratorContext context )
    {
    }
  }

  @Test
  public void buildTypeMapping()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final HashMap<String, String> typeMapping = new HashMap<>();
      typeMapping.put( "Person", "com.biz.Person" );
      final GeneratorContext context =
        new GeneratorContext( buildGraphQLSchema( "type Person {\n" +
                                                  "  name: String\n" +
                                                  "}\n" +
                                                  "type Tanker {\n" +
                                                  "  name: String\n" +
                                                  "}\n" ),
                              Document.newDocument().build(),
                              typeMapping,
                              Collections.emptyMap(),
                              FileUtil.createLocalTempDir(),
                              "com.example" );
      final Map<GraphQLType, String> mapping = new MyTestJavaGenerator().buildTypeMapping( context );
      assertEquals( mapping.size(), 1 );
      final Map.Entry<GraphQLType, String> entry = mapping.entrySet().iterator().next();
      assertEquals( entry.getKey().getName(), "Person" );
      assertEquals( entry.getValue(), "com.biz.Person" );
    } );
  }

  @Test
  public void isNotIntrospectionType()
  {
    final MyTestJavaGenerator gen = new MyTestJavaGenerator();
    assertTrue( gen.isNotIntrospectionType( GraphQLEnumType.newEnum().name( "Foo" ).build() ) );
    assertFalse( gen.isNotIntrospectionType( Introspection.__Directive ) );
  }

  @Test
  public void writeTypeMappingFile()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final MyTestJavaGenerator gen = new MyTestJavaGenerator();
      final GraphQLSchema schema =
        buildGraphQLSchema( "type Person {\n" +
                            "  name: String\n" +
                            "}\n" +
                            "type Tanker {\n" +
                            "  name: String\n" +
                            "}\n" );
      final Path dir = FileUtil.createLocalTempDir();
      final GeneratorContext context =
        new GeneratorContext( schema,
                              Document.newDocument().build(),
                              Collections.emptyMap(),
                              Collections.emptyMap(),
                              dir,
                              "com.example" );
      final HashMap<GraphQLType, String> typeMap = new HashMap<>();
      typeMap.put( schema.getType( "Person" ), "com.example.Person" );
      typeMap.put( schema.getType( "Tanker" ), "com.example.Tanker" );
      gen.writeTypeMappingFile( context, typeMap );

      final Path file = Paths.get( dir.toString(), "com", "example", "types.mapping" );
      final String actualContent = new String( Files.readAllBytes( file ), StandardCharsets.US_ASCII );
      assertEquals( actualContent,
                    "Person=com.example.Person\n" +
                    "Tanker=com.example.Tanker\n" );
    } );
  }
}
