package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.JdbcUrl;
import guru.qa.niffler.db.model.CategoryEntity;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class CategoryRepositorySJdbc implements CategoryRepository {

  private final DataSource spendDs = DataSourceProvider.INSTANCE.dataSource(JdbcUrl.SPEND);
  private final JdbcTemplate spendTemplate = new JdbcTemplate(spendDs);

  @Override
  public Optional<CategoryEntity> findCategoryByName(String name) {
    try {
      return Optional.ofNullable(spendTemplate.queryForObject(
          "SELECT * FROM \"category\" WHERE category = ?", (ResultSet rs, int rowNum) -> {
            var categoryEntity = new CategoryEntity();
            categoryEntity.setId((UUID) rs.getObject("id"));
            categoryEntity.setCategory(rs.getString("category"));
            categoryEntity.setUsername(rs.getString("username"));
            return categoryEntity;
          }, name)
      );
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }
}
