package org.realityforge.giggle.generator;

import gir.io.FileUtil;
import graphql.language.Document;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.giggle.AbstractTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class GeneratorContextTest
  extends AbstractTest
{
  private static class Test1Generator
    implements Generator
  {
    @Override
    public void generate( @Nonnull final GeneratorContext context )
    {
      context.getProperty( "Foo" );
    }
  }

  private static class Test2Generator
    implements Generator
  {
    @Nonnull
    @Override
    public List<PropertyDef> getSupportedProperties()
    {
      return Collections.singletonList( new PropertyDef( "Foo", false, "A test prop" ) );
    }

    @Override
    public void generate( @Nonnull final GeneratorContext context )
    {
      context.getRequiredProperty( "Foo" );
    }
  }

  private static class Test3Generator
    implements Generator
  {
    @Nullable
    private final String _expectedValue;

    private Test3Generator( @Nullable final String expectedValue )
    {
      _expectedValue = expectedValue;
    }

    @Nonnull
    @Override
    public List<PropertyDef> getSupportedProperties()
    {
      return Collections.singletonList( new PropertyDef( "Foo", false, "A test prop" ) );
    }

    @Override
    public void generate( @Nonnull final GeneratorContext context )
    {
      assertEquals( context.getProperty( "Foo" ), _expectedValue );
    }
  }

  private static class Test4Generator
    implements Generator
  {
    @Nullable
    private final String _expectedValue;

    private Test4Generator( @Nullable final String expectedValue )
    {
      _expectedValue = expectedValue;
    }

    @Nonnull
    @Override
    public List<PropertyDef> getSupportedProperties()
    {
      return Collections.singletonList( new PropertyDef( "Foo", true, "A test prop" ) );
    }

    @Override
    public void generate( @Nonnull final GeneratorContext context )
    {
      assertEquals( context.getRequiredProperty( "Foo" ), _expectedValue );
    }
  }

  @Test
  public void getProperty_propertyNotDeclared()
    throws Exception
  {
    final TestGeneratorRepository repository = new TestGeneratorRepository();
    repository.registerGenerator( new Test1Generator() );

    final GlobalGeneratorContext context = newContext( FileUtil.createLocalTempDir() );
    try
    {
      repository.getGenerator( "Test1" ).generate( context );
      fail( "Able to generate without exception!" );
    }
    catch ( final GenerateException ge )
    {
      assertEquals( ge.getMessage(),
                    "Generator named 'Test1' attempted to access property named 'Foo' but did not declare property" );
    }
  }

  @Test
  public void getRequiredProperty_propertyNotRequired()
    throws Exception
  {
    final TestGeneratorRepository repository = new TestGeneratorRepository();
    repository.registerGenerator( new Test2Generator() );

    final GlobalGeneratorContext context = newContext( FileUtil.createLocalTempDir() );
    try
    {
      repository.getGenerator( "Test2" ).generate( context );
      fail( "Able to generate without exception!" );
    }
    catch ( final GenerateException ge )
    {
      assertEquals( ge.getMessage(),
                    "Generator named 'Test2' attempted to access property named 'Foo' as if it was required but declared the property as optional" );
    }
  }

  @Test
  public void getProperty_propertyDeclaredButNotDefined()
    throws Exception
  {
    final TestGeneratorRepository repository = new TestGeneratorRepository();
    repository.registerGenerator( new Test3Generator( null ) );

    final GlobalGeneratorContext context = newContext( FileUtil.createLocalTempDir() );
    repository.getGenerator( "Test3" ).generate( context );
  }

  @Test
  public void getRequiredProperty_propertyDeclaredButNotDefined()
    throws Exception
  {
    final TestGeneratorRepository repository = new TestGeneratorRepository();
    repository.registerGenerator( new Test4Generator( null ) );

    final GlobalGeneratorContext context = newContext( FileUtil.createLocalTempDir() );
    try
    {
      repository.getGenerator( "Test4" ).generate( context );
      fail( "Able to generate without exception!" );
    }
    catch ( final GenerateException ge )
    {
      assertEquals( ge.getMessage(),
                    "Generator named 'Test4' accessed required property named 'Foo' but no such property was defined" );
    }
  }

  @Test
  public void getRequiredProperty()
    throws Exception
  {
    final TestGeneratorRepository repository = new TestGeneratorRepository();
    repository.registerGenerator( new Test4Generator( "MyValue" ) );

    final HashMap<String, String> defines = new HashMap<>();
    defines.put( "Foo", "MyValue" );
    final GlobalGeneratorContext context = new GlobalGeneratorContext( buildGraphQLSchema( "" ),
                                                                       Document.newDocument().build(),
                                                                       Collections.emptyMap(),
                                                                       Collections.emptyMap(),
                                                                       defines,
                                                                       FileUtil.createLocalTempDir(),
                                                                       "com.example" );
    repository.getGenerator( "Test4" ).generate( context );
  }


  @Test
  public void getProperty()
    throws Exception
  {
    final TestGeneratorRepository repository = new TestGeneratorRepository();
    repository.registerGenerator( new Test3Generator( "MyValue" ) );

    final HashMap<String, String> defines = new HashMap<>();
    defines.put( "Foo", "MyValue" );
    final GlobalGeneratorContext context = new GlobalGeneratorContext( buildGraphQLSchema( "" ),
                                                                       Document.newDocument().build(),
                                                                       Collections.emptyMap(),
                                                                       Collections.emptyMap(),
                                                                       defines,
                                                                       FileUtil.createLocalTempDir(),
                                                                       "com.example" );
    repository.getGenerator( "Test3" ).generate( context );
  }
}
