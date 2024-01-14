package guru.qa.niffler.jupiter;

import guru.qa.niffler.api.CategoryApi;
import guru.qa.niffler.api.SpendClient;
import guru.qa.niffler.model.CategoryJson;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
  private final CategoryApi categoryApi = new SpendClient().getDefault().create(CategoryApi.class);

  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {
    Optional<GenerateCategory> category = AnnotationSupport.findAnnotation(
        extensionContext.getRequiredTestMethod(),
        GenerateCategory.class
    );

    if (category.isPresent()) {
      var categoryData = category.get();
      var categoryJson = new CategoryJson(
          null,
          categoryData.description(),
          categoryData.username()
      );

      var existedUserCategories = categoryApi.getCategories(categoryData.username()).execute().body();
      var existedUserCategory = Objects.requireNonNull(existedUserCategories).stream()
          .filter(s -> s.category().equals(categoryData.description()))
          .findAny();

      var categoryForTest = existedUserCategory.isPresent()
          ? existedUserCategory.get()
          : categoryApi.addCategory(categoryJson).execute().body();
      extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), categoryForTest);
    }
  }
}
