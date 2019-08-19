package com.example.cdi_client_custom_properties;

import java.net.URI;
import java.util.Objects;
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
    return ClientBuilder.newClient().target( URI.create( this.baseUrl + "/graphql" ) ).request( MediaType.APPLICATION_JSON_TYPE ).header( "Authorization", "bearer " + Objects.requireNonNull( this.keycloak.getAccessToken() ).getToken() ).post( Entity.json( entity ) );
  }
}
