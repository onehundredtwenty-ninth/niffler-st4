package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.JdbcUrl;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;

public class FriendshipRepositorySJdbc implements FriendshipRepository {

  private final JdbcTemplate udTemplate = new JdbcTemplate(DataSourceProvider.INSTANCE.dataSource(JdbcUrl.USERDATA));

  @Override
  public void createFriendship(UUID firstFriendId, UUID secondFriendId, Boolean isPending) {
    udTemplate.update("INSERT INTO friendship "
        + "(user_id, friend_id, pending) "
        + "VALUES(?, ?, ?)", firstFriendId, secondFriendId, isPending
    );
  }
}
