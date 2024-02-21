package guru.qa.niffler.test.unit;

import guru.qa.niffler.api.CategoryApiClient;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
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
  void editSpendTest() {
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

    var updatedSpendJson = new SpendJson(
        createdSpend.id(),
        new Date(),
        "Обучение",
        CurrencyValues.USD,
        40000D,
        UUID.randomUUID().toString(),
        "bee"
    );
    var updatedSpend = spendApiClient.editSpend(updatedSpendJson);
    Assertions.assertAll(
        () -> Assertions.assertNotNull(updatedSpend.id()),
        () -> Assertions.assertEquals(40000D, updatedSpend.amount()),
        () -> Assertions.assertNotEquals(createdSpend.amount(), updatedSpend.amount())
    );
  }

  @Test
  void deleteSpendTest() {
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

    spendApiClient.deleteSpends(spendJson.username(), List.of(createdSpend.id().toString()));
  }

  @Test
  void spendsTest() {
    var spends = spendApiClient.spends("bee",
        null,
        LocalDate.now().minusMonths(6),
        LocalDate.now()
    );
    Assertions.assertAll(
        () -> Assertions.assertTrue(spends.size() > 0)
    );
  }

  @Test
  void statisticTest() {
    var spends = spendApiClient.statistic("bee",
        CurrencyValues.USD,
        null,
        LocalDate.now().minusMonths(6),
        LocalDate.now()
    );
    Assertions.assertAll(
        () -> Assertions.assertTrue(spends.size() > 0)
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
