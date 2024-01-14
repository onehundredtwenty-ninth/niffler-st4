package guru.qa.niffler.test.unit;

import static guru.qa.niffler.jupiter.User.UserType.COMMON;
import static guru.qa.niffler.jupiter.User.UserType.WITH_FRIENDS;

import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.jupiter.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Slf4j
@ExtendWith(UsersQueueExtension.class)
class UsersResolverWithoutBeforeTest {

  @BeforeEach
  void doLogin() {

  }

  @Test
  void friendsTableShouldNotBeEmpty(@User(WITH_FRIENDS) UserJson userWithFriends, @User(COMMON) UserJson commonUser) {
    log.info("UserData: {}", userWithFriends);
    log.info("UserData: {}", commonUser);

    Assertions.assertAll(
        () -> Assertions.assertNotNull(userWithFriends),
        () -> Assertions.assertEquals(WITH_FRIENDS, userWithFriends.testData().userType()),
        () -> Assertions.assertNotNull(commonUser),
        () -> Assertions.assertEquals(COMMON, commonUser.testData().userType())
    );
  }
}