package guru.qa.niffler.test.web;

import static guru.qa.niffler.jupiter.annotation.UserQueue.UserType.WITH_FRIENDS;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.CreateUser;
import guru.qa.niffler.jupiter.annotation.UserQueue;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.jupiter.extension.ContextHolderExtension;
import guru.qa.niffler.jupiter.extension.DbUserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({ContextHolderExtension.class, DbUserExtension.class, ApiLoginExtension.class, UsersQueueExtension.class})
class FriendsTest {

  @ApiLogin(user = @CreateUser)
  @Test
  void friendsTableShouldNotBeEmpty0(@UserQueue(WITH_FRIENDS) UserJson user) throws Exception {
    Selenide.open(FriendsPage.URL);
    System.out.println();
  }

  @Test
  void friendsTableShouldNotBeEmpty1(@UserQueue(WITH_FRIENDS) UserJson user) throws Exception {
    Thread.sleep(3000);
  }

  @Test
  void friendsTableShouldNotBeEmpty2(@UserQueue(WITH_FRIENDS) UserJson user) throws Exception {
    Thread.sleep(3000);
  }
}
