package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.JdbcUrl;
import guru.qa.niffler.db.model.SpendEntity;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class SpendingRepositorySJdbc implements SpendingRepository {

  private final DataSource spendDs = DataSourceProvider.INSTANCE.dataSource(JdbcUrl.SPEND);
  private final JdbcTemplate spendTemplate = new JdbcTemplate(spendDs);

  @Override
  public SpendEntity createSpend(SpendEntity spendEntity) {
    KeyHolder kh = new GeneratedKeyHolder();
    spendTemplate.update(con -> {
      var ps = con.prepareStatement(
          "INSERT INTO \"spend\" " +
              "(username, spend_date, currency, amount, description, category_id) " +
              "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
      ps.setString(1, spendEntity.getUsername());
      ps.setDate(2, new Date(spendEntity.getSpendDate().toInstant().getEpochSecond()));
      ps.setString(3, spendEntity.getCurrency().name());
      ps.setDouble(4, spendEntity.getAmount());
      ps.setString(5, spendEntity.getDescription());
      ps.setObject(6, spendEntity.getCategory().getId());
      return ps;
    }, kh);

    spendEntity.setId((UUID) kh.getKeys().get("id"));
    return spendEntity;
  }

  @Override
  public void deleteSpendById(UUID id) {
    spendTemplate.update("DELETE FROM \"spend\" WHERE id = ?", id);
  }
}
