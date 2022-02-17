package org.acme;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.*;
import org.jboss.logging.Logger;

@ApplicationScoped
public class MyReactiveMessagingApplication {

  @Inject
  Logger logger;

  @Inject
  PersonRepository personRepository;

  @Incoming("wordsin")
  public Uni<Void> queryDb(String object) {
    logger.infof("Processing message");
    return Panache.withTransaction(() ->
      personRepository
        .findByName("name")
        .onItem()
        .invoke(p -> logger.info("query done"))
        .replaceWithVoid()
    );
  }
}
