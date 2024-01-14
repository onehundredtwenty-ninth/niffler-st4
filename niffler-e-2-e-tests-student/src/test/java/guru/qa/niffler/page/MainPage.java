package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

public class MainPage {

  public void tableWithSpendingShouldBeEmpty() {
    getSpendingTableRows().shouldHave(CollectionCondition.empty);
  }

  public void deleteSelected() {
    $(byText("Delete selected")).click();
  }

  public void selectSpendingByDescription(String spendDescription) {
    findSpendingByDescription(spendDescription)
        .$("td")
        .click();
  }

  private SelenideElement findSpendingByDescription(String spendDescription) {
    return getSpendingTableRows().find(text(spendDescription));
  }

  private ElementsCollection getSpendingTableRows() {
    return $(".spendings-table tbody")
        .$$("tr");
  }
}
