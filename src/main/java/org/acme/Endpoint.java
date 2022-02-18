package org.acme;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.jboss.logging.Logger;

@Path("/")
public class Endpoint {

  @Inject
  Logger logger;

  @Inject
  PersonRepository personRepository;

  @GET
  public Uni<Void> queryDb() {
    logger.infof("Processing api - panache");
    return Panache.withTransaction(() ->
      personRepository
        .find("id", 1L)
        .firstResult()
        .onItem()
        .invoke(p -> logger.info("query done - panache"))
        .replaceWithVoid()
    );
  }
}
