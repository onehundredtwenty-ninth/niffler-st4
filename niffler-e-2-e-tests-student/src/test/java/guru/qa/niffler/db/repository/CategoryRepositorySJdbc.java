package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.JdbcUrl;
import guru.qa.niffler.db.model.CategoryEntity;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class CategoryRepositorySJdbc implements CategoryRepository {

  private final DataSource spendDs = DataSourceProvider.INSTANCE.dataSource(JdbcUrl.SPEND);
  private final JdbcTemplate spendTemplate = new JdbcTemplate(spendDs);

  @Override
  public CategoryEntity createCategory(CategoryEntity category) {
    KeyHolder kh = new GeneratedKeyHolder();
    spendTemplate.update(con -> {
      var ps = con.prepareStatement(
          "INSERT INTO \"category\" " +
              "(category, username) " +
              "VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
      ps.setString(1, category.getCategory());
      ps.setString(2, category.getUsername());
      return ps;
    }, kh);

    category.setId((UUID) kh.getKeys().get("id"));
    return category;
  }

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
