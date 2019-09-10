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
import javax.ws.rs.client.Invocation;
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
      if( Response.Status.Family.SUCCESSFUL == response.getStatusInfo().getFamily() ) {
        return response.readEntity( EventQuery.Answer.class );
      } else {
        throw new GraphQLException( "Error invoking GraphQL endpoint. HTTP Status: " + response.getStatusInfo() );
      }
    }
  }

  @Nonnull
  <T> Response $giggle$_call(@Nonnull final T entity) {
    final URI uri = URI.create( this.baseUrl );
    Invocation.Builder request = ClientBuilder.newClient().target( uri ).request();
    request = request.accept( MediaType.APPLICATION_JSON_TYPE );
    request = request.property( "jersey.config.client.connectTimeout", 10000 );
    request = request.property( "jersey.config.client.readTimeout", 10000 );
    return request.post( Entity.json( entity ) );
  }
}
