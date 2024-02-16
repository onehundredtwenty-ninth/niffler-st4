package guru.qa.niffler.test.unit;

import guru.qa.niffler.api.UserApiClient;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserApiClientTest {

  private final UserApiClient userApiClient = new UserApiClient();

  @Test
  void updateUserInfoTest() {
    var updatedUser = new UserJson(
        UUID.fromString("616de6e1-1424-4743-b2d4-3c4564a12e1c"),
        "bee",
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        CurrencyValues.USD,
        null,
        null,
        null
    );

    var updateUserInfo = userApiClient.updateUserInfo(updatedUser);
    Assertions.assertAll(
        () -> Assertions.assertEquals(updatedUser.firstname(), updateUserInfo.firstname()),
        () -> Assertions.assertEquals(updatedUser.surname(), updateUserInfo.surname())
    );
  }

  @Test
  void currentUserTest() {
    var user = userApiClient.currentUser("bee");
    Assertions.assertAll(
        () -> Assertions.assertEquals("bee", user.username()),
        () -> Assertions.assertNotNull(user.id()),
        () -> Assertions.assertNotNull(user.currency())
    );
  }

  @Test
  void allUsersTest() {
    var allUsers = userApiClient.allUsers("bee");
    Assertions.assertAll(
        () -> Assertions.assertTrue(allUsers.size() > 0)
    );
  }
}
