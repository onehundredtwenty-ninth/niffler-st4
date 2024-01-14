package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.GenerateCategory;
import guru.qa.niffler.jupiter.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
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
    welcomePage.clickLoginBtn();
    loginPage.fillLoginField("bee");
    loginPage.fillPasswordField("123");
    loginPage.submit();
  }

  @GenerateCategory(
      username = "bee",
      description = "Обучение"
  )
  @GenerateSpend(
      username = "bee",
      description = "QA.GURU Advanced 4",
      amount = 72500.00,
      currency = CurrencyValues.RUB
  )
  @Test
  void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
    mainPage.selectSpendingByDescription(spend.description());
    mainPage.deleteSelected();
    mainPage.tableWithSpendingShouldBeEmpty();
  }
}
