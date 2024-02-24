package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.utils.DateTimeUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SpendCollectionConditionStudent {

  public static CollectionCondition spends(SpendJson... expectedSpends) {
    return new CollectionCondition() {

      @Nonnull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        if (elements.size() != expectedSpends.length) {
          return CheckResult.rejected("Incorrect table size", elements);
        }

        boolean isCheckSuccess = true;
        List<SpendJson> actualSpends = new ArrayList<>();

        for (var i = 0; i < elements.size(); i++) {
          var element = elements.get(i);
          var tds = element.findElements(By.cssSelector("td"));

          var spendFromUi = new SpendJson(
              null,
              DateTimeUtils.dateFromString(tds.get(1).getText()),
              tds.get(4).getText(),
              CurrencyValues.valueOf(tds.get(3).getText()),
              Double.parseDouble(tds.get(2).getText()),
              tds.get(5).getText(),
              expectedSpends[i].username()
          );

          actualSpends.add(spendFromUi);

          if (!spendFromUi.equals(expectedSpends[i])) {
            isCheckSuccess = false;
          }
        }

        if (isCheckSuccess) {
          return CheckResult.accepted();
        } else {
          var errorMsg = String.format("Incorrect spends content. Expected spend list: %s, actual spend list: %s",
              Arrays.toString(expectedSpends), actualSpends);
          return CheckResult.rejected(errorMsg, elements);
        }
      }

      @Override
      public void fail(CollectionSource collection, CheckResult lastCheckResult, @Nullable Exception cause,
          long timeoutMs) {
        throw new AssertionError(lastCheckResult.message());
      }

      @Override
      public boolean missingElementSatisfiesCondition() {
        return false;
      }
    };
  }
}
