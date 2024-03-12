package guru.qa.niffler.test.unit;

import static guru.qa.niffler.jupiter.annotation.User.Point.OUTER;

import guru.qa.niffler.api.FriendsApiClient;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.CreateUser;
import guru.qa.niffler.jupiter.annotation.CreateUsers;
import guru.qa.niffler.jupiter.annotation.Friend;
import guru.qa.niffler.jupiter.annotation.Friend.FriendshipRequestType;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.web.BaseWebTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CreateUserExtensionTest extends BaseWebTest {

  private final FriendsApiClient friendsApiClient = new FriendsApiClient();

  @Test
  @CreateUsers({
      @CreateUser,
      @CreateUser
  })
  @ApiLogin(user = @CreateUser(
      categories = {
          @GenerateCategory(description = "Обучение5"),
          @GenerateCategory(description = "Обучение6")
      },
      spends = @GenerateSpend(
          category = "Обучение5",
          description = "Вокал",
          amount = 14000,
          currency = CurrencyValues.RUB
      )
  ))
  void createUsersTest(@User UserJson user, @User(OUTER) UserJson[] outerUsers) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(user),
        () -> Assertions.assertNotNull(user.id()),
        () -> Assertions.assertEquals(2, outerUsers.length)
    );
  }

  @Test
  @ApiLogin(user = @CreateUser(friends = {
      @Friend,
      @Friend
  }))
  void createUserWithFriendsTest(@User UserJson user) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(user),
        () -> Assertions.assertNotNull(user.id())
    );
    var friends = friendsApiClient.friends(user.username(), false);
    Assertions.assertAll(
        () -> Assertions.assertNotNull(friends),
        () -> Assertions.assertEquals(2, friends.size())
    );
  }

  @Test
  @ApiLogin(user = @CreateUser(
      friends = {
          @Friend(
              pending = true,
              friendshipRequestType = FriendshipRequestType.OUTCOME
          ),
          @Friend(
              pending = true,
              friendshipRequestType = FriendshipRequestType.OUTCOME
          )})
  )
  void createUserWithFriendsRequestsTest(@User UserJson user) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(user),
        () -> Assertions.assertNotNull(user.id())
    );

    var friends = friendsApiClient.friends(user.username(), false);
    Assertions.assertEquals(0, friends.size());

    var friendsInvitations = friendsApiClient.invitations(user.username());
    Assertions.assertEquals(0, friendsInvitations.size());

    var friendsRequests = friendsApiClient.friends(user.username(), true);
    Assertions.assertAll(
        () -> Assertions.assertNotNull(friendsRequests),
        () -> Assertions.assertEquals(2, friendsRequests.size())
    );
  }

  @Test
  @ApiLogin(user = @CreateUser(
      friends = {
          @Friend(
              pending = true,
              friendshipRequestType = FriendshipRequestType.INCOME
          ),
          @Friend(
              pending = true,
              friendshipRequestType = FriendshipRequestType.INCOME
          )})
  )
  void createUserWithFriendsIncomeRequestsTest(@User UserJson user) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(user),
        () -> Assertions.assertNotNull(user.id())
    );

    var friends = friendsApiClient.friends(user.username(), false);
    Assertions.assertEquals(0, friends.size());

    var friendsInvitations = friendsApiClient.invitations(user.username());
    Assertions.assertEquals(2, friendsInvitations.size());

    var friendsRequests = friendsApiClient.friends(user.username(), true);
    Assertions.assertAll(
        () -> Assertions.assertNotNull(friendsRequests),
        () -> Assertions.assertEquals(0, friendsRequests.size())
    );
  }

  @Test
  @ApiLogin(user = @CreateUser(
      friends = {
          @Friend(
              pending = true,
              friendshipRequestType = FriendshipRequestType.INCOME
          ),
          @Friend(
              pending = true,
              friendshipRequestType = FriendshipRequestType.OUTCOME
          )})
  )
  void createUserWithFriendsIncomeAndOutcomeRequestsTest(@User UserJson user) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(user),
        () -> Assertions.assertNotNull(user.id())
    );

    var friends = friendsApiClient.friends(user.username(), false);
    Assertions.assertEquals(0, friends.size());

    var friendsInvitations = friendsApiClient.invitations(user.username());
    Assertions.assertEquals(1, friendsInvitations.size());

    var friendsRequests = friendsApiClient.friends(user.username(), true);
    Assertions.assertAll(
        () -> Assertions.assertNotNull(friendsRequests),
        () -> Assertions.assertEquals(1, friendsRequests.size())
    );
  }
}
