package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.UserRepositorySupplier;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.CategoryEntity;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.SpendEntity;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.db.repository.CategoryRepository;
import guru.qa.niffler.db.repository.CategoryRepositorySJdbc;
import guru.qa.niffler.db.repository.FriendshipRepository;
import guru.qa.niffler.db.repository.FriendshipRepositorySJdbc;
import guru.qa.niffler.db.repository.SpendingRepository;
import guru.qa.niffler.db.repository.SpendingRepositorySJdbc;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.jupiter.annotation.CreateUser;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.utils.DataUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DataBaseCreateUserExtension extends CreateUserExtension {

  private final UserRepository userRepository = new UserRepositorySupplier().get();
  private final CategoryRepository categoryRepository = new CategoryRepositorySJdbc();
  private final SpendingRepository spendingRepository = new SpendingRepositorySJdbc();
  private final FriendshipRepository friendshipRepository = new FriendshipRepositorySJdbc();

  @Override
  public UserJson createUser(CreateUser user) {
    String username = user.username().isEmpty()
        ? DataUtils.generateRandomUsername()
        : user.username();
    String password = user.password().isEmpty()
        ? "12345"
        : user.password();

    UserAuthEntity userAuth = new UserAuthEntity();
    userAuth.setUsername(username);
    userAuth.setPassword(password);
    userAuth.setEnabled(true);
    userAuth.setAccountNonExpired(true);
    userAuth.setAccountNonLocked(true);
    userAuth.setCredentialsNonExpired(true);
    var authorities = Arrays.stream(Authority.values()).map(
        a -> {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setAuthority(a);
          return ae;
        }
    ).toList();

    userAuth.setAuthorities(authorities);

    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(username);
    userEntity.setCurrency(CurrencyValues.RUB);

    userRepository.createInAuth(userAuth);
    userRepository.createInUserdata(userEntity);

    System.out.println("Создан пользователь с id " + userEntity.getId());
    return new UserJson(
        userEntity.getId(),
        userEntity.getUsername(),
        userEntity.getFirstname(),
        userEntity.getSurname(),
        guru.qa.niffler.model.CurrencyValues.valueOf(userEntity.getCurrency().name()),
        userEntity.getPhoto() == null ? "" : new String(userEntity.getPhoto()),
        null,
        new TestData(
            password,
            null
        )
    );
  }

  @Override
  public List<CategoryJson> createCategories(CreateUser user, UserJson createdUser) {
    var categories = Arrays.stream(user.categories());
    List<CategoryJson> createdCategories = new ArrayList<>();

    categories.forEach(s -> {
      var categoryOptional = categoryRepository.findCategoryByName(user.username(), s.description());
      var category = categoryOptional.orElseGet(() -> {
            var categoryEntity = new CategoryEntity();
            categoryEntity.setUsername(user.username());
            categoryEntity.setCategory(s.description());
            return categoryRepository.createCategory(categoryEntity);
          }
      );

      System.out.println("Создана категория с id " + category.getId() + " и именем " + category.getCategory());
      createdCategories.add(new CategoryJson(
              category.getId(),
              category.getCategory(),
              category.getUsername()
          )
      );
    });

    return createdCategories;
  }

  @Override
  public List<SpendJson> createSpends(CreateUser user, UserJson createdUser) {
    var spends = Arrays.stream(user.spends());
    List<SpendJson> createdSpends = new ArrayList<>();

    spends.forEach(spend -> {
      var spendEntity = new SpendEntity();
      spendEntity.setUsername(spend.username());
      spendEntity.setSpendDate(new Date());
      spendEntity.setCurrency(spend.currency());
      spendEntity.setAmount(spend.amount());
      spendEntity.setDescription(spend.description());

      var category = categoryRepository.findCategoryByName(spend.username(), spend.category()).orElseThrow();
      spendEntity.setCategory(category);

      spendEntity = spendingRepository.createSpend(spendEntity);

      System.out.println("Создана трата с id " + spendEntity.getId() + " и именем " + spendEntity.getDescription());
      createdSpends.add(new SpendJson(
              spendEntity.getId(),
              spendEntity.getSpendDate(),
              spend.category(),
              spendEntity.getCurrency(),
              spendEntity.getAmount(),
              spendEntity.getDescription(),
              spendEntity.getUsername()
          )
      );
    });
    return createdSpends;
  }

  @Override
  public void deleteSpend(UUID id) {
    System.out.println("Удаляем трату с id " + id);
    spendingRepository.deleteSpendById(id);
  }

  @Override
  public void deleteCategory(UUID id) {
    System.out.println("Удаляем категорию с id " + id);
    categoryRepository.deleteCategory(id);
  }

  @Override
  public void deleteUser(UUID id) {
    System.out.println("Удаляем пользователя с id " + id);
    userRepository.deleteInAuthById(id);
    userRepository.deleteInUserdataById(id);
  }

  @Override
  public UserJson createRandomUser() {
    String username = DataUtils.generateRandomUsername();
    String password ="12345";

    UserAuthEntity userAuth = new UserAuthEntity();
    userAuth.setUsername(username);
    userAuth.setPassword(password);
    userAuth.setEnabled(true);
    userAuth.setAccountNonExpired(true);
    userAuth.setAccountNonLocked(true);
    userAuth.setCredentialsNonExpired(true);
    var authorities = Arrays.stream(Authority.values()).map(
        a -> {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setAuthority(a);
          return ae;
        }
    ).toList();

    userAuth.setAuthorities(authorities);

    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(username);
    userEntity.setCurrency(CurrencyValues.RUB);

    userRepository.createInAuth(userAuth);
    userRepository.createInUserdata(userEntity);

    System.out.println("Создан пользователь с id " + userEntity.getId());
    return new UserJson(
        userEntity.getId(),
        userEntity.getUsername(),
        userEntity.getFirstname(),
        userEntity.getSurname(),
        guru.qa.niffler.model.CurrencyValues.valueOf(userEntity.getCurrency().name()),
        userEntity.getPhoto() == null ? "" : new String(userEntity.getPhoto()),
        null,
        new TestData(
            password,
            null
        )
    );
  }

  @Override
  public void createFriendship(UUID firstFriendId, UUID secondFriendId, Boolean isPending) {
    friendshipRepository.createFriendship(firstFriendId, secondFriendId, isPending);
  }
}
