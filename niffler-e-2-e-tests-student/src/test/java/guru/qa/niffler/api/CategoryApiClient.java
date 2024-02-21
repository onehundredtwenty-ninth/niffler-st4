package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import java.util.List;
import lombok.SneakyThrows;

public class CategoryApiClient extends RestClient {

  private final CategoryApi categoryApi;

  public CategoryApiClient() {
    super("http://127.0.0.1:8093");
    categoryApi = retrofit.create(CategoryApi.class);
  }

  @SneakyThrows
  public CategoryJson addCategory(CategoryJson category) {
    return categoryApi.addCategory(category).execute().body();
  }

  @SneakyThrows
  public List<CategoryJson> getCategories(String username) {
    return categoryApi.getCategories(username).execute().body();
  }
}
