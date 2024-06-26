package guru.qa.niffler.test.unit;

import static guru.qa.niffler.jupiter.annotation.UserQueue.UserType.COMMON;
import static guru.qa.niffler.jupiter.annotation.UserQueue.UserType.WITH_FRIENDS;

import guru.qa.niffler.jupiter.annotation.UserQueue;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Slf4j
@ExtendWith(UsersQueueExtension.class)
class UsersResolverWithBeforeTest {

  private UserJson userWithFriends;
  private UserJson commonUser;

  @BeforeEach
  void doLogin(@UserQueue(WITH_FRIENDS) UserJson userWithFriends, @UserQueue(COMMON) UserJson commonUser) {
    log.info("UserData: {}", userWithFriends);
    log.info("UserData: {}", commonUser);

    Assertions.assertAll(
        () -> Assertions.assertNotNull(userWithFriends),
        () -> Assertions.assertEquals(WITH_FRIENDS, userWithFriends.testData().userType()),
        () -> Assertions.assertNotNull(commonUser),
        () -> Assertions.assertEquals(COMMON, commonUser.testData().userType())
    );

    this.userWithFriends = userWithFriends;
    this.commonUser = commonUser;
  }

  @Test
  void usersShouldBeResolved(@UserQueue(WITH_FRIENDS) UserJson userWithFriends, @UserQueue(COMMON) UserJson commonUser) {
    log.info("UserData: {}", userWithFriends);
    log.info("UserData: {}", commonUser);

    Assertions.assertAll(
        () -> Assertions.assertNotNull(userWithFriends),
        () -> Assertions.assertEquals(WITH_FRIENDS, userWithFriends.testData().userType()),
        () -> Assertions.assertEquals(userWithFriends, this.userWithFriends),
        () -> Assertions.assertNotNull(commonUser),
        () -> Assertions.assertEquals(COMMON, commonUser.testData().userType()),
        () -> Assertions.assertEquals(commonUser, this.commonUser)
    );
  }
}
