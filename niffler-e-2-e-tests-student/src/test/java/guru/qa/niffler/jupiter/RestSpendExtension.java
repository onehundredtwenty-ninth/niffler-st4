package guru.qa.niffler.jupiter;

import guru.qa.niffler.api.CategoryApi;
import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.api.SpendClient;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import java.util.Objects;
import lombok.SneakyThrows;

public class RestSpendExtension extends SpendExtension {

  private final SpendApi spendApi = new SpendClient().getDefault().create(SpendApi.class);
  private final CategoryApi categoryApi = new SpendClient().getDefault().create(CategoryApi.class);

  @SneakyThrows
  @Override
  public SpendJson create(SpendJson spend) {
    var existedUserCategories = categoryApi.getCategories(spend.username()).execute().body();
    var existedUserCategory = Objects.requireNonNull(existedUserCategories).stream()
        .filter(s -> s.category().equals(spend.category()))
        .findAny();

    if (existedUserCategory.isEmpty()) {
      var categoryJson = new CategoryJson(
          null,
          spend.category(),
          spend.username()
      );
      categoryApi.addCategory(categoryJson);
    }

    return spendApi.addSpend(spend).execute().body();
  }
}
