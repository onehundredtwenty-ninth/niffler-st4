package guru.qa.niffler.page;

import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.SelenideElement;

public class FriendsPage extends BasePage<FriendsPage> {

  private final SelenideElement friendsTable = $x("//table[@class='table abstract-table']");
}
