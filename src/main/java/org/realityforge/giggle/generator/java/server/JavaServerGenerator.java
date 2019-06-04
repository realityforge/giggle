package org.realityforge.giggle.generator.java.server;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeSpec;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLEnumValueDefinition;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLType;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.realityforge.giggle.generator.Generator;
import org.realityforge.giggle.generator.GeneratorContext;
import org.realityforge.giggle.generator.java.AbstractJavaGenerator;
import org.realityforge.giggle.generator.java.JavaGenUtil;

@Generator.MetaData( name = "java-server" )
public class JavaServerGenerator
  extends AbstractJavaGenerator
{
  @Override
  public void generate( @Nonnull final GeneratorContext context )
    throws Exception
  {
    final Map<GraphQLType, String> typeMap = buildTypeMapping( context );
    final GraphQLSchema schema = context.getSchema();
    final List<GraphQLType> types =
      schema.getAllTypesAsList()
        .stream()
        .filter( this::isNotIntrospectionType )
        .filter( t -> !typeMap.containsKey( t ) )
        .collect( Collectors.toList() );
    for ( final GraphQLType type : types )
    {
      if ( type instanceof GraphQLEnumType )
      {
        emitEnum( context, (GraphQLEnumType) type );
        typeMap.put( type, context.getPackageName() + "." + type.getName() );
      }
    }
    writeTypeMappingFile( context, typeMap );
  }

  private void emitEnum( @Nonnull final GeneratorContext context, @Nonnull final GraphQLEnumType enumType )
    throws IOException
  {
    final TypeSpec.Builder builder = TypeSpec.enumBuilder( enumType.getName() );
    enumType.getValues().forEach( value -> emitEnumValue( builder, value ) );
    JavaGenUtil.writeTopLevelType( context, builder );
  }

  private void emitEnumValue( @Nonnull final TypeSpec.Builder enumBuilder,
                              @Nonnull final GraphQLEnumValueDefinition value )
  {
    final TypeSpec.Builder builder = TypeSpec.anonymousClassBuilder( "" );
    final String description = value.getDescription();
    if ( null != description )
    {
      builder.addJavadoc( description.trim() + "\n" );
    }
    final String deprecationReason = value.getDeprecationReason();
    if ( null != deprecationReason )
    {
      builder.addJavadoc( "@deprecated " + deprecationReason + "\n" );
      builder.addAnnotation( AnnotationSpec.builder( Deprecated.class ).build() );
    }
    enumBuilder.addEnumConstant( value.getName(), builder.build() );
  }
}
