package org.realityforge.giggle.generator.java;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import javax.annotation.Nonnull;
import org.realityforge.giggle.generator.GeneratorContext;

public final class JavaGenUtil
{
  public static final ClassName NONNULL_CLASSNAME = ClassName.get( "javax.annotation", "Nonnull" );
  public static final ClassName NULLABLE_CLASSNAME = ClassName.get( "javax.annotation", "Nullable" );
  public static final ClassName LIST_CLASSNAME = ClassName.get( "java.util", "List" );

  private JavaGenUtil()
  {
  }

  public static void writeTopLevelType( @Nonnull final GeneratorContext context, @Nonnull final TypeSpec.Builder builder )
    throws IOException
  {
    JavaFile.builder( context.getPackageName(), builder.build() ).
      skipJavaLangImports( true ).
      build().
      writeTo( context.getOutputDirectory() );
  }
}
