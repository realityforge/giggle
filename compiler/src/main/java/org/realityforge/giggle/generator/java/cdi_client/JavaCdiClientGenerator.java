package org.realityforge.giggle.generator.java.cdi_client;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import graphql.language.Definition;
import graphql.language.NonNullType;
import graphql.language.OperationDefinition;
import graphql.language.VariableDefinition;
import graphql.schema.GraphQLType;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.Modifier;
import org.realityforge.giggle.generator.Generator;
import org.realityforge.giggle.generator.GeneratorContext;
import org.realityforge.giggle.generator.PropertyDef;
import org.realityforge.giggle.generator.java.AbstractJavaGenerator;
import org.realityforge.giggle.generator.java.JavaGenUtil;
import org.realityforge.giggle.generator.java.NamingUtil;

@Generator.MetaData( name = "java-cdi-client" )
public class JavaCdiClientGenerator
  extends AbstractJavaGenerator
{
  private static final String SERVICE_NAME_KEY = "cdi.service.name";
  private static final String BASE_URL_KEY = "cdi.base_url.jndi_name";
  private static final String URL_SUFFIX_KEY = "cdi.url.suffix";
  private static final String KEYCLOAK_CLIENT_NAME_KEY = "cdi.keycloak.client.name";
  private static final ClassName RESOURCE_TYPE = ClassName.get( "javax.annotation", "Resource" );
  private static final ClassName APPLICATION_SCOPED_TYPE =
    ClassName.get( "javax.enterprise.context", "ApplicationScoped" );
  private static final ClassName TRANSACTIONAL_TYPE = ClassName.get( "javax.transaction", "Transactional" );
  private static final ClassName INJECT_TYPE = ClassName.get( "javax.inject", "Inject" );
  private static final ClassName NAMED_TYPE = ClassName.get( "javax.inject", "Named" );
  private static final ClassName TYPED_TYPE = ClassName.get( "javax.enterprise.inject", "Typed" );
  private static final ClassName RESPONSE_TYPE = ClassName.get( "javax.ws.rs.core", "Response" );
  private static final ClassName MEDIA_TYPE_TYPE = ClassName.get( "javax.ws.rs.core", "MediaType" );
  private static final ClassName CLIENT_BUILDER_TYPE = ClassName.get( "javax.ws.rs.client", "ClientBuilder" );
  private static final ClassName ENTITY_TYPE = ClassName.get( "javax.ws.rs.client", "Entity" );
  private static final ClassName KEYCLOAK_TYPE =
    ClassName.get( "org.realityforge.keycloak.client.authfilter", "Keycloak" );

  @Nonnull
  @Override
  public List<PropertyDef> getSupportedProperties()
  {
    final List<PropertyDef> properties = new ArrayList<>();
    properties.add( new PropertyDef( SERVICE_NAME_KEY, true, "The name of the generated service class" ) );
    properties.add( new PropertyDef( BASE_URL_KEY,
                                     true,
                                     "The name of the JNDI resource that contains the base url for the endpoint" ) );
    properties.add( new PropertyDef( URL_SUFFIX_KEY,
                                     false,
                                     "The path added to the setting retrieved from the base_url.config.key to construct the url. If unspecified then no suffix is added" ) );
    properties.add( new PropertyDef( KEYCLOAK_CLIENT_NAME_KEY,
                                     false,
                                     "The name of the keycloak client used to authenticate the client. If unspecified then it is assumed no authentication step" ) );
    return properties;
  }

  @Override
  public void generate( @Nonnull final GeneratorContext context )
    throws Exception
  {
    final Map<GraphQLType, String> typeMap = buildTypeMapping( context );
    final List<GraphQLType> types = extractTypesToGenerate( context.getSchema(), typeMap );

    typeMap.putAll( extractGeneratedDataTypes( context, types ) );

    final String serviceName = context.getRequiredProperty( SERVICE_NAME_KEY );

    JavaGenUtil.writeTopLevelType( context, emitServiceInterface( context, serviceName, typeMap ) );
    JavaGenUtil.writeTopLevelType( context, emitServiceImplementation( context, serviceName, typeMap ) );
  }

  @Nonnull
  private TypeSpec.Builder emitServiceImplementation( @Nonnull final GeneratorContext context,
                                                      @Nonnull final String serviceName,
                                                      @Nonnull final Map<GraphQLType, String> typeMap )
  {
    final String baseUrlKey = context.getRequiredProperty( BASE_URL_KEY );

    final ClassName self = ClassName.get( context.getPackageName(), serviceName + "Impl" );
    final TypeSpec.Builder builder = TypeSpec.classBuilder( self );
    final ClassName serviceType = ClassName.get( context.getPackageName(), serviceName );
    builder.addSuperinterface( serviceType );
    builder.addModifiers( Modifier.PUBLIC );
    builder.addAnnotation( APPLICATION_SCOPED_TYPE );
    builder.addAnnotation( AnnotationSpec.builder( TRANSACTIONAL_TYPE )
                             .addMember( "value", "$T.TxType.NOT_SUPPORTED", TRANSACTIONAL_TYPE )
                             .build() );
    builder.addAnnotation( AnnotationSpec.builder( TYPED_TYPE ).addMember( "value", "$T.class", serviceType ).build() );

    for ( final Definition definition : context.getDocument().getDefinitions() )
    {
      if ( definition instanceof OperationDefinition )
      {
        final OperationDefinition operation = (OperationDefinition) definition;
        if ( OperationDefinition.Operation.SUBSCRIPTION != operation.getOperation() )
        {
          builder.addMethod( buildOperationMethodImplementation( context, typeMap, operation ) );
        }
      }
    }

    // Build baseUrl field
    builder.addField( FieldSpec
                        .builder( String.class, "baseUrl", Modifier.PRIVATE )
                        .addAnnotation( Nonnull.class )
                        .addAnnotation( AnnotationSpec.builder( RESOURCE_TYPE )
                                          .addMember( "lookup", "$S", baseUrlKey )
                                          .build() )
                        .build() );

    final String keycloakClientName = context.getProperty( KEYCLOAK_CLIENT_NAME_KEY );
    if ( null != keycloakClientName )
    {
      builder.addField( FieldSpec
                          .builder( KEYCLOAK_TYPE, "keycloak", Modifier.PRIVATE )
                          .addAnnotation( Nonnull.class )
                          .addAnnotation( AnnotationSpec.builder( NAMED_TYPE )
                                            .addMember( "value", "$S", keycloakClientName )
                                            .build() )
                          .addAnnotation( INJECT_TYPE )
                          .build() );
    }

    builder.addMethod( buildCallMethod( context ) );

    return builder;
  }

  @Nonnull
  private MethodSpec buildCallMethod( @Nonnull final GeneratorContext context )
  {
    final TypeVariableName typeVariable = TypeVariableName.get( "T" );
    final MethodSpec.Builder method = MethodSpec
      .methodBuilder( GEN_PREFIX + "call" )
      .addTypeVariable( typeVariable )
      .addAnnotation( Nonnull.class )
      .addParameter( ParameterSpec
                       .builder( typeVariable, "entity", Modifier.FINAL )
                       .addAnnotation( Nonnull.class )
                       .build() )
      .returns( RESPONSE_TYPE );

    final StringBuilder sb = new StringBuilder();
    final ArrayList<Object> params = new ArrayList<>();
    sb.append( "return $T.newClient().target( $T.create( this.baseUrl " );
    params.add( CLIENT_BUILDER_TYPE );
    params.add( URI.class );

    final String urlSuffix = context.getProperty( URL_SUFFIX_KEY );
    if ( null != urlSuffix )
    {
      sb.append( "+ $S " );
      params.add( urlSuffix );
    }
    sb.append( ") ).request( $T.APPLICATION_JSON_TYPE )" );
    params.add( MEDIA_TYPE_TYPE );

    final boolean keycloakEnabled = null != context.getProperty( KEYCLOAK_CLIENT_NAME_KEY );
    if ( keycloakEnabled )
    {
      sb.append( ".header( \"Authorization\", " +
                 "\"bearer \" + $T.requireNonNull( this.keycloak.getAccessToken() ).getAccessToken() )" );
      params.add( Objects.class );
    }
    sb.append( ".post( $T.json( entity ) )" );
    params.add( ENTITY_TYPE );

    method.addStatement( sb.toString(), params.toArray() );

    return method.build();
  }

  @Nonnull
  private MethodSpec buildOperationMethodImplementation( @Nonnull final GeneratorContext context,
                                                         @Nonnull final Map<GraphQLType, String> typeMap,
                                                         @Nonnull final OperationDefinition operation )
  {
    final Map<VariableDefinition, TypeName> variableTypes = buildVariableMap( operation, typeMap );

    final String typeName = toOperationClassName( operation );
    final String name = operation.getName();
    assert null != name;
    final MethodSpec.Builder method =
      MethodSpec.methodBuilder( NamingUtil.lowercaseFirstCharacter( name ) )
        .addModifiers( Modifier.PUBLIC )
        .addAnnotation( Override.class )
        .addAnnotation( Nonnull.class )
        .returns( ClassName.get( context.getPackageName(), typeName, "Answer" ) );

    final StringBuilder sb = new StringBuilder();
    final ArrayList<Object> params = new ArrayList<>();
    sb.append( "try ( $T response = $N( new $T(" );
    params.add( RESPONSE_TYPE );
    params.add( GEN_PREFIX + "call" );
    params.add( ClassName.get( context.getPackageName(), typeName, "Question" ) );

    boolean first = true;

    for ( final VariableDefinition variable : operation.getVariableDefinitions() )
    {
      final TypeName javaType = variableTypes.get( variable );
      final ParameterSpec.Builder parameter = ParameterSpec.builder( javaType, variable.getName(), Modifier.FINAL );
      final boolean isNonnull = variable.getType() instanceof NonNullType;
      if ( !javaType.isPrimitive() )
      {
        parameter.addAnnotation( isNonnull ? Nonnull.class : Nullable.class );
      }
      method.addParameter( parameter.build() );
      if ( first )
      {
        sb.append( " " );
        first = false;
      }
      else
      {
        sb.append( ", " );
      }
      sb.append( "$N" );
      params.add( variable.getName() );
    }
    if ( !first )
    {
      sb.append( " " );
    }

    sb.append( ") ) )" );

    final CodeBlock.Builder code = CodeBlock.builder();
    code.beginControlFlow( sb.toString(), params.toArray() );
    code.addStatement( "return response.readEntity( $T.class )",
                       ClassName.get( context.getPackageName(), typeName, "Answer" ) );
    code.endControlFlow();
    method.addCode( code.build() );
    return method.build();
  }

  @Nonnull
  private TypeSpec.Builder emitServiceInterface( @Nonnull final GeneratorContext context,
                                                 @Nonnull final String serviceName,
                                                 @Nonnull final Map<GraphQLType, String> typeMap )
  {
    final ClassName self = ClassName.get( context.getPackageName(), serviceName );
    final TypeSpec.Builder builder = TypeSpec.interfaceBuilder( self );
    builder.addModifiers( Modifier.PUBLIC );

    for ( final Definition definition : context.getDocument().getDefinitions() )
    {
      if ( definition instanceof OperationDefinition )
      {
        final OperationDefinition operation = (OperationDefinition) definition;
        if ( OperationDefinition.Operation.SUBSCRIPTION != operation.getOperation() )
        {
          builder.addMethod( buildOperationMethod( context, typeMap, operation ) );
        }
      }
    }
    return builder;
  }

  @Nonnull
  private MethodSpec buildOperationMethod( @Nonnull final GeneratorContext context,
                                           @Nonnull final Map<GraphQLType, String> typeMap,
                                           @Nonnull final OperationDefinition operation )
  {
    final Map<VariableDefinition, TypeName> variableTypes = buildVariableMap( operation, typeMap );

    final String typeName = toOperationClassName( operation );
    final String name = operation.getName();
    assert null != name;
    final MethodSpec.Builder method =
      MethodSpec.methodBuilder( NamingUtil.lowercaseFirstCharacter( name ) )
        .addModifiers( Modifier.PUBLIC, Modifier.ABSTRACT )
        .addAnnotation( Nonnull.class )
        .returns( ClassName.get( context.getPackageName(), typeName, "Answer" ) );
    for ( final VariableDefinition variable : operation.getVariableDefinitions() )
    {
      final TypeName javaType = variableTypes.get( variable );
      final ParameterSpec.Builder parameter = ParameterSpec.builder( javaType, variable.getName() );
      final boolean isNonnull = variable.getType() instanceof NonNullType;
      if ( !javaType.isPrimitive() )
      {
        parameter.addAnnotation( isNonnull ? Nonnull.class : Nullable.class );
      }
      method.addParameter( parameter.build() );
    }
    return method.build();
  }

  @Nonnull
  private Map<VariableDefinition, TypeName> buildVariableMap( @Nonnull final OperationDefinition operation,
                                                              @Nonnull final Map<GraphQLType, String> typeMap )
  {
    final Map<VariableDefinition, TypeName> variableTypes = new HashMap<>();
    for ( final VariableDefinition variable : operation.getVariableDefinitions() )
    {
      variableTypes.put( variable, JavaGenUtil.getJavaType( typeMap, variable.getType() ) );
    }
    return variableTypes;
  }

  @Nonnull
  private String toOperationClassName( @Nonnull final OperationDefinition operation )
  {
    final String name = operation.getName();
    assert null != name;
    final OperationDefinition.Operation operationType = operation.getOperation();
    return NamingUtil.uppercaseFirstCharacter( name ) +
           (
             OperationDefinition.Operation.QUERY == operationType ? "Query" :
             OperationDefinition.Operation.MUTATION == operationType ? "Mutation" :
             "Subscription"
           );
  }
}
