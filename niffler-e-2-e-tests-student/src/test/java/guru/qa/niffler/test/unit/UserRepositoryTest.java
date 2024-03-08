package guru.qa.niffler.test.unit;

import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.jupiter.annotation.CreateUser;
import guru.qa.niffler.jupiter.extension.UserRepositoryExtension;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UserRepositoryExtension.class)
class UserRepositoryTest {

  private UserRepository userRepository;

  @CreateUser
  @Test
  void selectUserFromAuthTest(UserAuthEntity userAuth) {
    var selectedUser = userRepository.findByIdInAuth(userAuth.getId()).orElseThrow();

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

  @CreateUser
  @Test
  void updateUserFromAuthTest(UserAuthEntity userAuth) {
    userAuth.setUsername("updatedUserName" + UUID.randomUUID());
    userAuth.setEnabled(false);
    userAuth.setAccountNonExpired(false);
    userAuth.setAccountNonLocked(false);
    userAuth.setCredentialsNonExpired(false);

    userRepository.updateInAuth(userAuth);
    var selectedUser = userRepository.findByIdInAuth(userAuth.getId()).orElseThrow();

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
