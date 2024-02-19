package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainPage extends BasePage<MainPage> {

  private final SelenideElement categoryList = $x("//label[text()='Category']/div[@class='select-wrapper']");
  private final SelenideElement amountField = $x("//input[@name='amount']");
  private final SelenideElement spendDateField = $x("//div[@class='calendar-wrapper ']//input");
  private final SelenideElement descriptionField = $x("//input[@name='description']");
  private final SelenideElement submitBtn = $x("//button[@type='submit']");
  private final SelenideElement todayFilterBtn = $x("//button[text()='Today']");
  private final SelenideElement lastWeekFilterBtn = $x("//button[text()='Last week']");
  private final SelenideElement lastMonthFilterBtn = $x("//button[text()='Last month']");
  private final SelenideElement allTimeFilterBtn = $x("//button[text()='All time']");

  public MainPage setCategory(String category) {
    categoryList.click();
    $x("//*[text()='" + category + "']").click();
    return this;
  }

  public MainPage setAmount(Double amount) {
    amountField.setValue(String.valueOf(amount));
    return this;
  }

  public MainPage setSpendDateField(LocalDate date) {
    spendDateField.setValue(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    spendDateField.pressEnter();
    return this;
  }

  public MainPage setDescription(String description) {
    descriptionField.setValue(description);
    return this;
  }

  public MainPage clickSubmitBtn() {
    submitBtn.click();
    return this;
  }

  public MainPage clickTodayFilterBtn() {
    todayFilterBtn.click();
    return this;
  }

  public MainPage clickLastWeekFilterBtn() {
    lastWeekFilterBtn.click();
    return this;
  }

  public MainPage clickLastMonthFilterBtn() {
    lastMonthFilterBtn.click();
    return this;
  }

  public MainPage clickAllTimeFilterBtn() {
    allTimeFilterBtn.click();
    return this;
  }

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
