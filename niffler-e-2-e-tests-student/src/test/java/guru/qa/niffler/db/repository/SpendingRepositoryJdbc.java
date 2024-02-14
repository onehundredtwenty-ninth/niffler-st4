package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.JdbcUrl;
import guru.qa.niffler.db.model.SpendEntity;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import javax.sql.DataSource;

public class SpendingRepositoryJdbc implements SpendingRepository {

  private final DataSource spendDs = DataSourceProvider.INSTANCE.dataSource(JdbcUrl.SPEND);

  @Override
  public SpendEntity createSpend(SpendEntity spendEntity) {
    try (Connection conn = spendDs.getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement(
          "INSERT INTO \"spend\" " +
              "(username, spend_date, currency, amount, description, category_id) " +
              "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, spendEntity.getUsername());
        ps.setDate(2, new Date(spendEntity.getSpendDate().toInstant().getEpochSecond()));
        ps.setString(3, spendEntity.getCurrency().name());
        ps.setDouble(4, spendEntity.getAmount());
        ps.setString(5, spendEntity.getDescription());
        ps.setObject(6, spendEntity.getCategory().getId());
        ps.executeUpdate();

        UUID userId;
        try (ResultSet keys = ps.getGeneratedKeys()) {
          if (keys.next()) {
            userId = UUID.fromString(keys.getString("id"));
          } else {
            throw new IllegalStateException("Can`t find id");
          }
        }
        spendEntity.setId(userId);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return spendEntity;
  }

  @Override
  public void deleteSpendById(UUID id) {
    var query = "DELETE FROM \"spend\" WHERE id = ?";

    try (var con = spendDs.getConnection();
        var ps = con.prepareStatement(query)) {
      ps.setObject(1, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
