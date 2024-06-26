package guru.qa.niffler.test.web;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.jupiter.annotation.CreateUser;
import org.junit.jupiter.api.Test;

class LoginStudentTest extends BaseWebTest {

  @CreateUser
  @Test
  void statisticShouldBeVisibleAfterLogin(UserAuthEntity userAuth) {
    Selenide.open("http://frontend.niffler.dc");
    $("a[href*='redirect']").click();
    $("input[name='username']").setValue(userAuth.getUsername());
    $("input[name='password']").setValue(userAuth.getPassword());
    $("button[type='submit']").click();
    $(".main-content__section-stats").should(visible);
  }
}
