package guru.qa.niffler.test.web;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import guru.qa.niffler.utils.DateTimeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpendingTest extends BaseWebTest {

  private final WelcomePage welcomePage = new WelcomePage();
  private final LoginPage loginPage = new LoginPage();
  private final MainPage mainPage = new MainPage();

  static {
    Configuration.browserSize = "1980x1024";
  }

  @BeforeEach
  void doLogin() {
    Selenide.open("http://frontend.niffler.dc");
    welcomePage.doLogin();
    loginPage.setUsername("bee");
    loginPage.setPassword("123");
    loginPage.submit();
  }

  @GenerateSpend(
      username = "bee",
      description = "QA.GURU Advanced 4",
      category = "Обучение49",
      amount = 72500.00,
      currency = CurrencyValues.RUB
  )
  @Test
  void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
    $(".spendings-table tbody")
        .$$("tr")
        .find(text(spend.description()))
        .$$("td")
        .first()
        .click();

    mainPage.getSpendingTable()
        .checkTableContains(spend);
  }

  @Test
  void spendingShouldBePresentedInTable() {
    var spend = new SpendJson(
        null,
        DateTimeUtils.dateFromString("19 Feb 24"),
        "Обучение49",
        CurrencyValues.USD,
        20000D,
        "1217ed04-eb31-4b8a-8253-1213bcad8aa1",
        "bee"
    );

    mainPage.getSpendingTable()
        .checkTableContains(spend);
  }
}
