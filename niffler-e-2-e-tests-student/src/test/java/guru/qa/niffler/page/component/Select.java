package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

import com.codeborne.selenide.SelenideElement;

public class Select extends BaseComponent<Select> {

  public Select(SelenideElement self) {
    super(self);
  }

  private final SelenideElement input = self.$("input");

  public void setValue(String value) {
    input.setValue(value);
    $$("div[id^='react-select']").find(exactText(value)).click();
  }

  public void checkSelectValueIsEqualTo(String value) {
    self.shouldHave(text(value));
  }
}
