package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

  Optional<UserAuthEntity> findByIdInAuth(UUID id);

  Optional<UserEntity> findByIdInUserdata(UUID id);

  UserAuthEntity createInAuth(UserAuthEntity user);

  UserEntity createInUserdata(UserEntity user);

  UserAuthEntity updateInAuth(UserAuthEntity user);

  UserEntity updateInUserdata(UserEntity user);

  void deleteInAuthById(UUID id);

  void deleteInUserdataById(UUID id);
}
