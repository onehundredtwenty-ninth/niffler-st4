package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.CategoryApiClient;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.model.CategoryJson;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
  private final CategoryApiClient categoryApiClient = new CategoryApiClient();

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

      var existedUserCategories = categoryApiClient.getCategories(categoryData.username());
      var existedUserCategory = Objects.requireNonNull(existedUserCategories).stream()
          .filter(s -> s.category().equals(categoryData.description()))
          .findAny();

      var categoryForTest = existedUserCategory.orElseGet(() -> categoryApiClient.addCategory(categoryJson));
      extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), categoryForTest);
    }
  }
}
