package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Calendar;
import guru.qa.niffler.page.component.Footer;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.Select;
import guru.qa.niffler.page.component.SpendingTable;
import io.qameta.allure.Step;
import java.util.Date;

public class MainPage extends BasePage<MainPage> {

  public static final String URL = "/main";

  protected final Header header = new Header();
  protected final Footer footer = new Footer();
  protected final SpendingTable spendingTable = new SpendingTable();
  private final SelenideElement addSpendingSection = $(".main-content__section-add-spending");
  private final Select categorySelect = new Select(addSpendingSection.$("div.select-wrapper"));
  private final Calendar calendar = new Calendar(addSpendingSection.$(".react-datepicker"));
  private final SelenideElement amountInput = addSpendingSection.$("input[name='amount']");
  private final SelenideElement descriptionInput = addSpendingSection.$("input[name='description']");
  private final SelenideElement submitNewSpendingButton = addSpendingSection.$("button[type='submit']");
  private final SelenideElement errorContainer = addSpendingSection.$(".form__error");
  private final SelenideElement todayFilterBtn = $x("//button[text()='Today']");
  private final SelenideElement lastWeekFilterBtn = $x("//button[text()='Last week']");
  private final SelenideElement lastMonthFilterBtn = $x("//button[text()='Last month']");
  private final SelenideElement allTimeFilterBtn = $x("//button[text()='All time']");
  private final SelenideElement currencyFilter = $x(
      "//div[@class='spendings__table-controls']//div[@class='select-wrapper']");

  public Header getHeader() {
    return header;
  }

  public Footer getFooter() {
    return footer;
  }

  public SpendingTable getSpendingTable() {
    spendingTable.getSelf().scrollIntoView(true);
    return spendingTable;
  }

  @Step("Check that page is loaded")
  @Override
  public MainPage waitForPageLoaded() {
    header.getSelf().should(visible).shouldHave(text("Niffler. The coin keeper."));
    footer.getSelf().should(visible).shouldHave(text("Study project for QA Automation Advanced. 2023"));
    spendingTable.getSelf().should(visible).shouldHave(text("History of spendings"));
    return this;
  }

  @Step("Select new spending category: {0}")
  public MainPage setNewSpendingCategory(String category) {
    Selenide.sleep(1000);
    categorySelect.setValue(category);
    return this;
  }

  @Step("Set new spending amount: {0}")
  public MainPage setNewSpendingAmount(int amount) {
    amountInput.setValue(String.valueOf(amount));
    return this;
  }

  @Step("Set new spending amount: {0}")
  public MainPage setNewSpendingAmount(double amount) {
    amountInput.setValue(String.valueOf(amount));
    return this;
  }

  @Step("Set new spending date: {0}")
  public MainPage setNewSpendingDate(Date date) {
    calendar.selectDateInCalendar(date);
    addSpendingSection.$(byText("Add new spending")).click();
    return this;
  }

  @Step("Set new spending description: {0}")
  public MainPage setNewSpendingDescription(String description) {
    descriptionInput.setValue(description);
    return this;
  }

  @Step("Click submit button to create new spending")
  public MainPage submitNewSpending() {
    submitNewSpendingButton.click();
    return this;
  }

  @Step("Check error: {0} is displayed")
  public MainPage checkError(String error) {
    errorContainer.shouldHave(text(error));
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

  public void selectSpendingByIndex(int spendIndex) {
    findSpendingByIndex(spendIndex)
        .$("td")
        .click();
  }

  private SelenideElement findSpendingByIndex(int spendIndex) {
    return getSpendingTableRows().get(spendIndex);
  }

  private ElementsCollection getSpendingTableRows() {
    return $(".spendings-table tbody")
        .$$("tr");
  }

  public MainPage setCurrencyFilter(String currencyName) {
    currencyFilter.click();
    $x("//*[text()='" + currencyName + "']").click();
    return this;
  }
}
