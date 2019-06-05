package org.realityforge.giggle.generator;

import javax.annotation.Nonnull;
import org.realityforge.giggle.AbstractTest;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class GeneratorRepositoryTest
  extends AbstractTest
{
  private static class TestGeneratorRepository
    extends AbstractGeneratorRepository
  {
  }

  @Generator.MetaData( name = "test" )
  private static class TestGenerator
    implements Generator
  {
    int _generateCount;

    @Override
    public void generate( @Nonnull final GeneratorContext context )
    {
      _generateCount++;
    }
  }

  private static class DefaultNamedGenerator
    implements Generator
  {
    @Override
    public void generate( @Nonnull final GeneratorContext context )
    {
    }
  }

  @Generator.MetaData( name = "fail" )
  private static class FailingGenerator
    implements Generator
  {
    @Override
    public void generate( @Nonnull final GeneratorContext context )
    {
      throw new NumberFormatException();
    }
  }

  @Test
  public void getGenerator()
  {
    final TestGeneratorRepository repository = new TestGeneratorRepository();
    final TestGenerator generator = new TestGenerator();
    repository.registerGenerator( generator );

    final Generator result = repository.getGenerator( "test" );
    assertEquals( result, generator );
  }

  @Test
  public void getGenerator_noExplicitName()
  {
    final TestGeneratorRepository repository = new TestGeneratorRepository();
    final DefaultNamedGenerator generator = new DefaultNamedGenerator();
    repository.registerGenerator( generator );

    final Generator result = repository.getGenerator( "DefaultNamed" );
    assertEquals( result, generator );
  }

  @Test
  public void getGenerator_missing()
  {
    final TestGeneratorRepository repository = new TestGeneratorRepository();
    final String name = ValueUtil.randomString();

    final NoSuchGeneratorException exception =
      expectThrows( NoSuchGeneratorException.class, () -> repository.getGenerator( name ) );
    assertEquals( exception.getName(), name );
  }

  @Test
  public void generate_error()
  {
    final TestGeneratorRepository repository = new TestGeneratorRepository();
    final String name = "fail";
    final FailingGenerator generator = new FailingGenerator();
    repository.registerGenerator( generator );

    //noinspection ConstantConditions
    final GenerateException exception =
      expectThrows( GenerateException.class, () -> repository.generate( name, null ) );

    assertEquals( exception.getName(), name );
    assertTrue( exception.getCause() instanceof NumberFormatException );
  }

  @Test
  public void generate()
  {
    final TestGeneratorRepository repository = new TestGeneratorRepository();
    final TestGenerator generator = new TestGenerator();
    repository.registerGenerator( generator );

    assertEquals( generator._generateCount, 0 );
    //noinspection ConstantConditions
    repository.generate( "test", null );
    assertEquals( generator._generateCount, 1 );
  }
}
