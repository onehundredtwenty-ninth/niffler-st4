package guru.qa.niffler.db.jpa;

import guru.qa.niffler.db.JdbcUrl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class JpaService {

  private final Map<JdbcUrl, EntityManager> emStore;

  public JpaService(JdbcUrl database, EntityManager em) {
    this.emStore = new HashMap<>();
    this.emStore.put(database, em);
  }

  public JpaService(Map<JdbcUrl, EntityManager> ems) {
    this.emStore = ems;
  }

  protected EntityManager entityManager(JdbcUrl database) {
    return emStore.get(database);
  }

  protected <T> void persist(JdbcUrl database, T entity) {
    tx(database, em -> em.persist(entity));
  }

  protected <T> void remove(JdbcUrl database, T entity) {
    tx(database, em -> em.remove(entity));
  }

  protected <T> T merge(JdbcUrl database, T entity) {
    return txWithResult(database, em -> em.merge(entity));
  }

  protected <T> T txWithResult(JdbcUrl database, Function<EntityManager, T> function) {
    EntityTransaction transaction = entityManager(database).getTransaction();
    transaction.begin();
    try {
      T result = function.apply(entityManager(database));
      transaction.commit();
      return result;
    } catch (Exception e) {
      transaction.rollback();
      throw e;
    }
  }

  protected void tx(JdbcUrl database, Consumer<EntityManager> consumer) {
    EntityTransaction transaction = entityManager(database).getTransaction();
    transaction.begin();
    try {
      consumer.accept(entityManager(database));
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      throw e;
    }
  }
}
