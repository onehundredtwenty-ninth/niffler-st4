package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.message.Msg;
import io.qameta.allure.Step;

public abstract class BasePage<T extends BasePage> {

  public abstract T waitForPageLoaded();

  protected final SelenideElement toaster = $(".Toastify__toast-body");

  @Step("Check that success message appears: {msg}")
  @SuppressWarnings("unchecked")
  public T checkToasterMessage(Msg msg) {
    toaster.should(Condition.visible).should(text(msg.getMessage()));
    return (T) this;
  }
}
