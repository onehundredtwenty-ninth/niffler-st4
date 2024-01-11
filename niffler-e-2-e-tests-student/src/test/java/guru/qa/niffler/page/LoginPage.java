package guru.qa.niffler.page;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

public class LoginPage {

  private final SelenideElement loginField = $("input[name='username']");
  private final SelenideElement passwordField = $("input[name='password']");
  private final SelenideElement submitBtn = $("button[type='submit']");

  public void fillLoginField(String login) {
    loginField.shouldBe(Condition.visible).setValue(login);
  }

  public void fillPasswordField(String password) {
    passwordField.shouldBe(Condition.visible).setValue(password);
  }

  public void submit() {
    submitBtn.shouldBe(Condition.visible).click();
  }
}
