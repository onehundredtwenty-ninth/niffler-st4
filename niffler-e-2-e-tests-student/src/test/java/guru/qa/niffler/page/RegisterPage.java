package guru.qa.niffler.page;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

public class RegisterPage extends BasePage<RegisterPage> {

  private final SelenideElement loginField = $("#username");
  private final SelenideElement passwordField = $("#password");
  private final SelenideElement submitPasswordField = $("#passwordSubmit");
  private final SelenideElement submitBtn = $(".form__submit");

  public RegisterPage fillLoginField(String login) {
    loginField.setValue(login);
    return this;
  }

  public RegisterPage fillPasswordField(String password) {
    passwordField.setValue(password);
    return this;
  }

  public RegisterPage fillSubmitPasswordField(String password) {
    submitPasswordField.setValue(password);
    return this;
  }

  public void clickSubmitBtn() {
    submitBtn.click();
  }
}
