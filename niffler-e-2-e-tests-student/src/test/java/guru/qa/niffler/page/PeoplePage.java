package guru.qa.niffler.page;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.PeopleTable;
import io.qameta.allure.Step;

public class PeoplePage extends BasePage<PeoplePage> {

  public static final String URL = "/people";

  private final SelenideElement tableContainer = $(".people-content");
  private final PeopleTable table = new PeopleTable($(".table"));

  @Step("Check that the page is loaded")
  @Override
  public PeoplePage waitForPageLoaded() {
    tableContainer.shouldBe(Condition.visible);
    return this;
  }

  @Step("Send invitation to user: {username}")
  public PeoplePage sendFriendInvitationToUser(String username) {
    SelenideElement friendRow = table.getRowByUsername(username);
    SelenideElement actionsCell = table.getActionsCell(friendRow);
    actionsCell.$(".button-icon_type_add")
        .click(usingJavaScript());
    return this;
  }

  @Step("Check invitation status for user: {username}")
  public PeoplePage checkInvitationSentToUser(String username) {
    SelenideElement friendRow = table.getRowByUsername(username);
    SelenideElement actionsCell = table.getActionsCell(friendRow);
    actionsCell.shouldHave(text("Pending invitation"));
    return this;
  }

  public void openByHeader() {
    $("a[href*='people']").click();
  }

  public void userWithStatusShouldBePresentedInTable(String status) {
    $("div.people-content")
        .$$("div.people-content div.abstract-table__buttons")
        .shouldHave(CollectionCondition.itemWithText(status));
  }

  public void submitInvitationButtonShouldBePresented() {
    $("div.people-content div.abstract-table__buttons div[data-tooltip-id=submit-invitation]")
        .shouldBe(Condition.visible);
  }

  public void declineInvitationButtonShouldBePresented() {
    $("div.people-content div.abstract-table__buttons div[data-tooltip-id=decline-invitation]")
        .shouldBe(Condition.visible);
  }
}
