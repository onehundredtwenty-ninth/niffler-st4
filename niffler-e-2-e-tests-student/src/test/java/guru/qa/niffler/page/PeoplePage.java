package guru.qa.niffler.page;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;

public class PeoplePage extends BasePage<PeoplePage> {

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
