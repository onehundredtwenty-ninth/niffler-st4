package guru.qa.niffler.page;

import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.SelenideElement;

public class ProfilePage extends BasePage<ProfilePage> {

  private final SelenideElement nameField = $x("//input[@name='firstname']");
  private final SelenideElement surnameField = $x("//input[@name='surname']");
  private final SelenideElement currencyList = $x("//label[text()='Currency']/div[@class='select-wrapper']");
  private final SelenideElement submitBtn = $x("//button[@type='submit']");
  private final SelenideElement categoryField = $x("//input[@name='category']");
  private final SelenideElement createCategoryBtn = $x("//button[text()='Create']");

  public ProfilePage setNameField(String name) {
    nameField.setValue(name);
    return this;
  }

  public ProfilePage setSurnameField(String surname) {
    surnameField.setValue(surname);
    return this;
  }

  public ProfilePage chooseCurrency(String currencyName) {
    currencyList.click();
    $x("//*[text()='" + currencyName + "']").click();
    return this;
  }

  public ProfilePage clickSubmitBtn() {
    submitBtn.click();
    return this;
  }

  public ProfilePage setCategoryField(String category) {
    categoryField.setValue(category);
    return this;
  }

  public ProfilePage clickCreateCategoryBtn() {
    createCategoryBtn.click();
    return this;
  }
}
