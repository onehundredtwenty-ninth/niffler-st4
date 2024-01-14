package guru.qa.niffler.page;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

public class WelcomePage {

  private final SelenideElement loginBtn = $("a[href*='redirect']");

  public void clickLoginBtn() {
    loginBtn.shouldBe(Condition.visible).click();
  }
}
