package guru.qa.niffler.db.repository;

import java.util.UUID;

public interface FriendshipRepository {

  void createFriendship(UUID firstFriendId, UUID secondFriendId, Boolean isPending);
}
