package org.acme;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.*;
import org.hibernate.reactive.mutiny.Mutiny;
import org.jboss.logging.Logger;

@ApplicationScoped
public class MyReactiveMessagingApplication {

  @Inject
  Logger logger;

  @Inject
  PersonRepository personRepository;

  @Inject
  Mutiny.SessionFactory sessionFactory;

  @Incoming("wordsin-panache")
  public Uni<Void> queryDb(String message) {
    logger.infof("Processing message - panache - %s", message);
    return Panache.withTransaction(() ->
      personRepository
        .find("id", 1L)
        .firstResult()
        .onItem()
        .invoke(p -> logger.info("query done - panache"))
        .replaceWithVoid()
    );
  }

  @Incoming("wordsin-hibernate")
  public Uni<Void> queryDbReactive(String message) {
    logger.infof("Processing message - hibernate - %s", message);
    return sessionFactory.withTransaction((session, transaction) ->
      session
        .find(Person.class, 1L)
        .onItem()
        .invoke(p -> logger.info("query done - hibernate"))
        .replaceWithVoid()
    );
  }
}
