package guru.qa.niffler.test.web;

import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECEIVED;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SEND;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.steps.AuthorizationSteps;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Slf4j
@ExtendWith(UsersQueueExtension.class)
class FriendsRequestsTest {

  private final AuthorizationSteps authorizationSteps = new AuthorizationSteps();
  private final PeoplePage peoplePage = new PeoplePage();

  @Test
  void friendShouldPresentInTable(@User(WITH_FRIENDS) UserJson user) {
    authorizationSteps.doLogin(user);
    peoplePage.openByHeader();
    peoplePage.userWithStatusShouldBePresentedInTable("You are friends");
  }

  @Test
  void friendRequestShouldPresentInTable(@User(INVITATION_SEND) UserJson user) {
    authorizationSteps.doLogin(user);
    peoplePage.openByHeader();
    peoplePage.userWithStatusShouldBePresentedInTable("Pending invitation");
  }

  @Test
  void friendInvitationShouldPresentInTable(@User(INVITATION_RECEIVED) UserJson user) {
    authorizationSteps.doLogin(user);
    peoplePage.openByHeader();
    peoplePage.submitInvitationButtonShouldBePresented();
    peoplePage.declineInvitationButtonShouldBePresented();
  }
}
