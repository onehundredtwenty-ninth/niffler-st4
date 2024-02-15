package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.model.SpendEntity;
import java.util.UUID;

public interface SpendingRepository {

  SpendEntity createSpend(SpendEntity spendEntity);

  void deleteSpendById(UUID id);
}
