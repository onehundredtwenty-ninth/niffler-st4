package guru.qa.niffler.db.repository;


import static guru.qa.niffler.db.JdbcUrl.SPEND;

import guru.qa.niffler.db.EmfProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.SpendEntity;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SpendingRepositoryHibernate extends JpaService implements SpendingRepository {

  private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  public SpendingRepositoryHibernate() {
    super(
        Map.of(
            SPEND, EmfProvider.INSTANCE.emf(SPEND).createEntityManager()
        )
    );
  }

  @Override
  public SpendEntity createSpend(SpendEntity spend) {
    persist(SPEND, spend);
    return spend;
  }

  @Override
  public void deleteSpendById(UUID id) {
    var spend = Optional.of(entityManager(SPEND).find(SpendEntity.class, id)).get();
    remove(SPEND, spend);
  }
}
