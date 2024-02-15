package guru.qa.niffler.jupiter;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

public abstract class SpendExtension implements BeforeEachCallback {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendExtension.class);

  public abstract SpendJson create(SpendJson spend);

  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {
    Optional<GenerateSpend> spend = AnnotationSupport.findAnnotation(
        extensionContext.getRequiredTestMethod(),
        GenerateSpend.class
    );

    if (spend.isPresent()) {
      GenerateSpend spendData = spend.get();
      var category = spendData.category().isBlank()
          ? ((CategoryJson) extensionContext.getStore(CategoryExtension.NAMESPACE).get(extensionContext.getUniqueId())).category()
          : spendData.category();

      SpendJson spendJson = new SpendJson(
          null,
          new Date(),
          category,
          spendData.currency(),
          spendData.amount(),
          spendData.description(),
          spendData.username()
      );

      SpendJson created = create(spendJson);
      extensionContext.getStore(NAMESPACE)
          .put("spend", created);
    }
  }
}
