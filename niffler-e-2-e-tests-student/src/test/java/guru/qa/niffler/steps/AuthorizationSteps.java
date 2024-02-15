package guru.qa.niffler.steps;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.page.WelcomePage;

public class AuthorizationSteps {

  private final WelcomePage welcomePage = new WelcomePage();
  private final LoginPage loginPage = new LoginPage();
  private final PeoplePage peoplePage = new PeoplePage();

  public void doLogin(UserJson user) {
    Selenide.open("http://frontend.niffler.dc");
    welcomePage.clickLoginBtn();
    loginPage.setLogin(user.username());
    loginPage.setPassword(user.testData().password());
    loginPage.submit();
  }
}
