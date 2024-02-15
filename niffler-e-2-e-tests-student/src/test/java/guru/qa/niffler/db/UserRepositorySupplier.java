package guru.qa.niffler.db;

import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.db.repository.UserRepositoryJdbc;
import guru.qa.niffler.db.repository.UserRepositorySJdbc;
import java.util.function.Supplier;

public class UserRepositorySupplier implements Supplier<UserRepository> {

  @Override
  public UserRepository get() {
    return "sjdbc".equals(System.getProperty("repository", "sjdbc"))
        ? new UserRepositorySJdbc()
        : new UserRepositoryJdbc();
  }
}
