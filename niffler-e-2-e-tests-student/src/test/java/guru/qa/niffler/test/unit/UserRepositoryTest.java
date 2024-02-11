package guru.qa.niffler.test.unit;

import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.repository.UserRepositoryJdbc;
import guru.qa.niffler.jupiter.DbUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserRepositoryTest {

  @DbUser
  @Test
  void selectUserFromAuthTest(UserAuthEntity userAuth) {
    var userRepository = new UserRepositoryJdbc();
    var selectedUser = userRepository.findByIdInAuth(userAuth.getId());

    Assertions.assertAll(
        () -> Assertions.assertEquals(userAuth.getId(), selectedUser.getId()),
        () -> Assertions.assertEquals(userAuth.getUsername(), selectedUser.getUsername()),
        () -> Assertions.assertEquals(userAuth.getEnabled(), selectedUser.getEnabled()),
        () -> Assertions.assertEquals(userAuth.getAccountNonExpired(), selectedUser.getAccountNonExpired()),
        () -> Assertions.assertEquals(userAuth.getAccountNonLocked(), selectedUser.getAccountNonLocked()),
        () -> Assertions.assertEquals(userAuth.getCredentialsNonExpired(), selectedUser.getCredentialsNonExpired()),
        () -> Assertions.assertEquals(userAuth.getAuthorities().size(), selectedUser.getAuthorities().size())
    );
  }

  @DbUser
  @Test
  void updateUserFromAuthTest(UserAuthEntity userAuth) {
    var userRepository = new UserRepositoryJdbc();
    userAuth.setUsername("updatedUserName");
    userAuth.setEnabled(false);
    userAuth.setAccountNonExpired(false);
    userAuth.setAccountNonLocked(false);
    userAuth.setCredentialsNonExpired(false);

    var updatedRecords = userRepository.updateInAuth(userAuth);
    Assertions.assertEquals(1, updatedRecords);

    var selectedUser = userRepository.findByIdInAuth(userAuth.getId());

    Assertions.assertAll(
        () -> Assertions.assertEquals(userAuth.getId(), selectedUser.getId()),
        () -> Assertions.assertEquals(userAuth.getUsername(), selectedUser.getUsername()),
        () -> Assertions.assertEquals(userAuth.getEnabled(), selectedUser.getEnabled()),
        () -> Assertions.assertEquals(userAuth.getAccountNonExpired(), selectedUser.getAccountNonExpired()),
        () -> Assertions.assertEquals(userAuth.getAccountNonLocked(), selectedUser.getAccountNonLocked()),
        () -> Assertions.assertEquals(userAuth.getCredentialsNonExpired(), selectedUser.getCredentialsNonExpired())
    );
  }
}
