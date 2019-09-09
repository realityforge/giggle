package com.example.cdi_client_custom_properties;

import java.net.URI;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Typed;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.realityforge.keycloak.client.authfilter.Keycloak;

@ApplicationScoped
@Transactional(Transactional.TxType.NOT_SUPPORTED)
@Typed(EventService.class)
@Generated("org.realityforge.giggle.Main")
public class EventServiceImpl implements EventService {
  @Nonnull
  @Resource(
      lookup = "myapp/env/other_url"
  )
  private String baseUrl;

  @Nonnull
  @Named("Rose.Keycloak")
  @Inject
  private Keycloak keycloak;

  @Override
  @Nonnull
  public EventQuery.Answer event(@Nonnull final String id) {
    try ( Response response = $giggle$_call( new EventQuery.Question( id ) ) ) {
      return response.readEntity( EventQuery.Answer.class );
    }
  }

  @Nonnull
  <T> Response $giggle$_call(@Nonnull final T entity) {
    final URI uri = URI.create( this.baseUrl + "/graphql" );
    Invocation.Builder request = ClientBuilder.newClient().target( uri ).request();
    request = request.accept( MediaType.APPLICATION_JSON_TYPE );
    request = request.header( "Authorization", "bearer " + $giggle$_getBearerToken() );
    return request.post( Entity.json( entity ) );
  }

  @Nonnull
  String $giggle$_getBearerToken() {
    final String token = this.keycloak.getAccessToken();
    if( null == token ) {
      throw new GraphQLException( "Bearer token unavailable from Keycloak" );
    }
    return token;
  }
}
