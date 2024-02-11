package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import java.util.UUID;

public interface UserRepository {

  UserAuthEntity findByIdInAuth(UUID id);

  UserEntity findByIdInUserdata(UUID id);

  UserAuthEntity createInAuth(UserAuthEntity user);

  UserEntity createInUserdata(UserEntity user);

  void deleteInAuthById(UUID id);

  void deleteInUserdataById(UUID id);
}
