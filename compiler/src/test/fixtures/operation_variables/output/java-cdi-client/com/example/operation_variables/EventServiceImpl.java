package com.example.operation_variables;

import java.net.URI;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Typed;
import javax.transaction.Transactional;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

  @Override
  @Nonnull
  public EventQuery.Answer event(@Nonnull final String id) {
    try ( Response response = $giggle$_call( new EventQuery.Question( id ) ) ) {
      return response.readEntity( EventQuery.Answer.class );
    }
  }

  @Nonnull
  <T> Response $giggle$_call(@Nonnull final T entity) {
    return ClientBuilder.newClient().target( URI.create( this.baseUrl ) ).request( MediaType.APPLICATION_JSON_TYPE ).post( Entity.json( entity ) );
  }
}