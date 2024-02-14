package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.model.CategoryEntity;
import java.util.Optional;

public interface CategoryRepository {

  CategoryEntity createCategory(CategoryEntity category);

  Optional<CategoryEntity> findCategoryByName(String name);
}
