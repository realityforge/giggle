package org.realityforge.giggle.generator.java;

import gir.io.FileUtil;
import graphql.introspection.Introspection;
import graphql.language.Document;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLNamedType;
import graphql.schema.GraphQLSchema;
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
import org.realityforge.giggle.generator.GeneratorEntry;
import org.realityforge.giggle.generator.GlobalGeneratorContext;
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
    final HashMap<String, String> typeMapping = new HashMap<>();
    typeMapping.put( "Person", "com.biz.Person" );
    final GlobalGeneratorContext globalContext =
      new GlobalGeneratorContext( buildGraphQLSchema( "type Person {\n" +
                                                      "  name: String\n" +
                                                      "}\n" +
                                                      "type Tanker {\n" +
                                                      "  name: String\n" +
                                                      "}\n" ),
                                  Document.newDocument().build(),
                                  typeMapping,
                                  Collections.emptyMap(),
                                  Collections.emptyMap(),
                                  FileUtil.createLocalTempDir(),
                                  "com.example" );
    final MyTestJavaGenerator gen = new MyTestJavaGenerator();
    final GeneratorContext context =
      new GeneratorContext( new GeneratorEntry( gen ), globalContext );
    final Map<GraphQLNamedType, String> mapping = gen.buildTypeMapping( context );
    assertEquals( mapping.size(), 1 );
    final Map.Entry<GraphQLNamedType, String> entry = mapping.entrySet().iterator().next();
    assertEquals( entry.getKey().getName(), "Person" );
    assertEquals( entry.getValue(), "com.biz.Person" );
  }

  @Test
  public void isNotIntrospectionType()
  {
    final MyTestJavaGenerator gen = new MyTestJavaGenerator();
    assertTrue( gen.isNotIntrospectionType( GraphQLEnumType.newEnum().name( "Foo" ).build() ) );
    assertFalse( gen.isNotIntrospectionType( Introspection.__Directive ) );
  }

  @Test
  public void asJavadoc()
  {
    final MyTestJavaGenerator gen = new MyTestJavaGenerator();
    assertEquals( gen.asJavadoc( "\n\n\nHello" ), "Hello\n" );
    assertEquals( gen.asJavadoc( "\n\n\nHello\n\n\n" ), "Hello\n" );
    assertEquals( gen.asJavadoc( "\n\n\nHello\n\nWorld\n" ), "Hello\n\nWorld\n" );
  }

  @Test
  public void writeTypeMappingFile()
    throws Exception
  {
    final GraphQLSchema schema =
      buildGraphQLSchema( "type Person {\n" +
                          "  name: String\n" +
                          "}\n" +
                          "type Tanker {\n" +
                          "  name: String\n" +
                          "}\n" );
    final Path dir = FileUtil.createLocalTempDir();
    final GlobalGeneratorContext globalContext =
      new GlobalGeneratorContext( schema,
                                  Document.newDocument().build(),
                                  Collections.emptyMap(),
                                  Collections.emptyMap(),
                                  Collections.emptyMap(),
                                  dir,
                                  "com.example" );
    final MyTestJavaGenerator gen = new MyTestJavaGenerator();
    final GeneratorContext context =
      new GeneratorContext( new GeneratorEntry( gen ), globalContext );
    final HashMap<GraphQLNamedType, String> typeMap = new HashMap<>();
    typeMap.put( (GraphQLNamedType) schema.getType( "Person" ), "com.example.Person" );
    typeMap.put( (GraphQLNamedType) schema.getType( "Tanker" ), "com.example.Tanker" );
    gen.writeTypeMappingFile( context, typeMap );

    final Path file = Paths.get( dir.toString(), "com", "example", "types.mapping" );
    final String actualContent = new String( Files.readAllBytes( file ), StandardCharsets.US_ASCII );
    assertEquals( actualContent,
                  "Person=com.example.Person\n" +
                  "Tanker=com.example.Tanker\n" );
  }
}
