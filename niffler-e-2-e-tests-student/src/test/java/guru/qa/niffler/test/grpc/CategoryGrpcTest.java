package guru.qa.niffler.test.grpc;

import guru.qa.grpc.niffler.grpc.Category;
import guru.qa.grpc.niffler.grpc.CategoryResponse;
import guru.qa.grpc.niffler.grpc.UserName;
import io.qameta.allure.Allure;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CategoryGrpcTest extends BaseCategoryGrpcTest {

  @Test
  void allCategoriesShouldBeReturnedFromNifflerCategory() {
    final UserName request = UserName.newBuilder()
        .setUsername("bee")
        .build();

    final CategoryResponse response = blockingStub.getAllCategories(request);

    Allure.step("Check categories size", () ->
        Assertions.assertEquals(9, response.getAllCategoriesList().size()
        ));

    final List<Category> categoriesList = response.getAllCategoriesList();
    Category category = categoriesList.get(0);
    Allure.step("Check category name", () ->
        Assertions.assertEquals("Обучение", category.getCategory()
        ));
  }

  @Test
  void addCategoryTest() {
    final Category request = Category.newBuilder()
        .setCategory(UUID.randomUUID().toString())
        .setUsername("duck")
        .build();

    final Category response = blockingStub.addCategory(request);
    Allure.step("Check category name", () ->
        Assertions.assertEquals(request.getCategory(), response.getCategory()
        ));
  }
}
