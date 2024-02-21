package guru.qa.niffler.test.unit;

import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECEIVED;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

import guru.qa.niffler.api.FriendsApiClient;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UsersQueueExtension.class)
class FriendApiClientTest {

  private final FriendsApiClient friendsApiClient = new FriendsApiClient();

  @Test
  void friendsTest(@User(WITH_FRIENDS) UserJson user) {
    var friends = friendsApiClient.friends(user.username(), true);
    Assertions.assertTrue(friends.size() > 0);
  }

  @Test
  void invitationsTest(@User(INVITATION_RECEIVED) UserJson user) {
    var friends = friendsApiClient.invitations(user.username());
    Assertions.assertTrue(friends.size() > 0);
  }
}
