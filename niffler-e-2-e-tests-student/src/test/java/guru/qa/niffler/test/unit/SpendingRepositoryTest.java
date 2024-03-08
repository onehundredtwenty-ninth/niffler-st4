package guru.qa.niffler.test.unit;

import guru.qa.niffler.db.model.CategoryEntity;
import guru.qa.niffler.db.model.SpendEntity;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.repository.SpendingRepositoryHibernate;
import guru.qa.niffler.db.repository.SpendingRepository;
import guru.qa.niffler.jupiter.annotation.CreateUser;
import guru.qa.niffler.model.CurrencyValues;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SpendingRepositoryTest {

  private final SpendingRepository spendingRepository = new SpendingRepositoryHibernate();
  private SpendEntity spend;

  @CreateUser
  @Test
  void selectUserFromAuthTest(UserAuthEntity userAuth) {
    spend = new SpendEntity();
    spend.setUsername(userAuth.getUsername());
    spend.setSpendDate(Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC)));
    spend.setCurrency(CurrencyValues.RUB);
    spend.setAmount(10000D);
    spend.setDescription(UUID.randomUUID().toString());

    var category = new CategoryEntity();
    category.setId(UUID.fromString("a22fff34-2153-4f3b-8a27-5491abeb576d"));
    spend.setCategory(category);

    spendingRepository.createSpend(spend);

    Assertions.assertAll(
        () -> Assertions.assertNotNull(spend.getId())
    );
  }

  @AfterEach
  void deleteSpend() {
    spendingRepository.deleteSpendById(spend.getId());
  }
}
