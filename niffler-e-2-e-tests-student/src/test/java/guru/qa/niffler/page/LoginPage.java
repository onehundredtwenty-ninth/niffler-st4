package guru.qa.niffler.page;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

public class LoginPage extends BasePage<LoginPage> {

  private final SelenideElement loginInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitBtn = $("button[type='submit']");

  public LoginPage setLogin(String login) {
    loginInput.setValue(login);
    return this;
  }

  public LoginPage setPassword(String password) {
    passwordInput.setValue(password);
    return this;
  }

  public void submit() {
    submitBtn.click();
  }
}
