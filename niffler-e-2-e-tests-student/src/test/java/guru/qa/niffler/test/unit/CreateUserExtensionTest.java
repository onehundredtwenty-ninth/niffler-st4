package guru.qa.niffler.test.unit;

import static guru.qa.niffler.jupiter.annotation.User.Point.OUTER;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.CreateUser;
import guru.qa.niffler.jupiter.annotation.CreateUsers;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.web.BaseWebTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CreateUserExtensionTest extends BaseWebTest {

  @Test
  @CreateUsers({
      @CreateUser,
      @CreateUser
  })
  @ApiLogin(user = @CreateUser(
      categories = {
          @GenerateCategory(description = "Обучение101"),
          @GenerateCategory(description = "Обучение132")
      },
      spends = @GenerateSpend(
          category = "Обучение101",
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
}
