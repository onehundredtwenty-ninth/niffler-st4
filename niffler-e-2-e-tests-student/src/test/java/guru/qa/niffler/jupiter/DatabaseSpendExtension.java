package guru.qa.niffler.jupiter;

import guru.qa.niffler.db.model.SpendEntity;
import guru.qa.niffler.db.repository.CategoryRepository;
import guru.qa.niffler.db.repository.CategoryRepositorySJdbc;
import guru.qa.niffler.db.repository.SpendingRepository;
import guru.qa.niffler.db.repository.SpendingRepositorySJdbc;
import guru.qa.niffler.model.SpendJson;

public class DatabaseSpendExtension extends SpendExtension {

  private final SpendingRepository spendingRepository = new SpendingRepositorySJdbc();
  private final CategoryRepository categoryRepository = new CategoryRepositorySJdbc();

  @Override
  public SpendJson create(SpendJson spend) {
    var spendEntity = new SpendEntity();
    spendEntity.setUsername(spend.username());
    spendEntity.setSpendDate(spend.spendDate());
    spendEntity.setCurrency(spend.currency());
    spendEntity.setAmount(spend.amount());
    spendEntity.setDescription(spend.description());

    var category = categoryRepository.findCategoryByName(spend.category()).get();
    spendEntity.setCategory(category);

    spendingRepository.createSpend(spendEntity);
    return new SpendJson(
        spendEntity.getId(),
        spendEntity.getSpendDate(),
        spend.category(),
        spendEntity.getCurrency(),
        spendEntity.getAmount(),
        spendEntity.getDescription(),
        spendEntity.getUsername()
    );
  }
}
