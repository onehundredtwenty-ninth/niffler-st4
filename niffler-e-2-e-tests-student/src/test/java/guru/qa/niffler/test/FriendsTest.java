package guru.qa.niffler.test;

import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.jupiter.extension.ContextHolderExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({ContextHolderExtension.class, ApiLoginExtension.class, UsersQueueExtension.class})
class FriendsTest {

  @ApiLogin(username = "bee", password = "123")
  @Test
  void friendsTableShouldNotBeEmpty0(@User(WITH_FRIENDS) UserJson user) throws Exception {
    Selenide.open(FriendsPage.URL);
    System.out.println();
  }

  @Test
  void friendsTableShouldNotBeEmpty1(@User(WITH_FRIENDS) UserJson user) throws Exception {
    Thread.sleep(3000);
  }

  @Test
  void friendsTableShouldNotBeEmpty2(@User(WITH_FRIENDS) UserJson user) throws Exception {
    Thread.sleep(3000);
  }
}
