package guru.qa.niffler.page;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

public class LoginPage {

  private final SelenideElement loginField = $("input[name='username']");
  private final SelenideElement passwordField = $("input[name='password']");
  private final SelenideElement submitBtn = $("button[type='submit']");

  public void fillLoginField(String login) {
    loginField.setValue(login);
  }

  public void fillPasswordField(String password) {
    passwordField.setValue(password);
  }

  public void submit() {
    submitBtn.click();
  }
}
