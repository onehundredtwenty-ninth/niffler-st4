package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.CategoryApiClient;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import java.util.Objects;
import lombok.SneakyThrows;

public class RestSpendExtension extends SpendExtension {

  private final SpendApiClient spendApiClient = new SpendApiClient();
  private final CategoryApiClient categoryApiClient = new CategoryApiClient();

  @SneakyThrows
  @Override
  public SpendJson create(SpendJson spend) {
    var existedUserCategories = categoryApiClient.getCategories(spend.username());
    var existedUserCategory = Objects.requireNonNull(existedUserCategories).stream()
        .filter(s -> s.category().equals(spend.category()))
        .findAny();

    if (existedUserCategory.isEmpty()) {
      var categoryJson = new CategoryJson(
          null,
          spend.category(),
          spend.username()
      );
      categoryApiClient.addCategory(categoryJson);
    }

    return spendApiClient.addSpend(spend);
  }
}
