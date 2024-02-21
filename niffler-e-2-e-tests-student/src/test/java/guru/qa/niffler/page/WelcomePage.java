package guru.qa.niffler.page;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

public class WelcomePage extends BasePage<WelcomePage> {

  private final SelenideElement loginBtn = $("a[href*='redirect']");
  private final SelenideElement registerBtn = $("a[href*='http://auth.niffler.dc:9000/register']");

  public void clickLoginBtn() {
    loginBtn.click();
  }

  public void clickRegisterBtn() {
    registerBtn.click();
  }
}
