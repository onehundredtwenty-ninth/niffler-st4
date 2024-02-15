package guru.qa.niffler.test.unit;

import guru.qa.niffler.api.CategoryApiClient;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import java.util.Date;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SpendApiClientTest {

  private final SpendApiClient spendApiClient = new SpendApiClient();
  private final CategoryApiClient categoryApiClient = new CategoryApiClient();

  @Test
  void addSpendTest() {
    var spendJson = new SpendJson(
        null,
        new Date(),
        "Обучение",
        CurrencyValues.USD,
        20000D,
        UUID.randomUUID().toString(),
        "bee"
    );

    var createdSpend = spendApiClient.addSpend(spendJson);
    Assertions.assertAll(
        () -> Assertions.assertNotNull(createdSpend.id())
    );
  }

  @Test
  void getAllCategoriesTest() {
    var categories = categoryApiClient.getCategories("bee");
    Assertions.assertAll(
        () -> Assertions.assertTrue(categories.size() > 0)
    );
  }

  @Test
  void createCategoryTest() {
    var categoryJson = new CategoryJson(
        null,
        "Обучение " + UUID.randomUUID(),
        "bee"
    );
    var category = categoryApiClient.addCategory(categoryJson);
    Assertions.assertAll(
        () -> Assertions.assertNotNull(category.id())
    );
  }
}
