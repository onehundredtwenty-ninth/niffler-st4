package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.model.CategoryEntity;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {

  CategoryEntity createCategory(CategoryEntity category);

  Optional<CategoryEntity> findCategoryByName(String userName, String name);

  void deleteCategory(UUID id);
}
