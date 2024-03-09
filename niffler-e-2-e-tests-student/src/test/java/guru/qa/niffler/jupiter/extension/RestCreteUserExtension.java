package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.CategoryApiClient;
import guru.qa.niffler.api.RegisterApiClient;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.api.UserApiClient;
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
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.NotImplementedException;
import org.awaitility.Awaitility;

public class RestCreteUserExtension extends CreateUserExtension {

  private final RegisterApiClient registerApiClient = new RegisterApiClient();
  private final UserApiClient userApiClient = new UserApiClient();
  private final CategoryApiClient categoryApiClient = new CategoryApiClient();
  private final SpendApiClient spendApiClient = new SpendApiClient();

  @Override
  public UserJson createUser(CreateUser user) {
    String username = user.username().isEmpty()
        ? DataUtils.generateRandomUsername()
        : user.username();
    String password = user.password().isEmpty()
        ? "12345"
        : user.password();

    registerApiClient.getRegisterCookies();
    registerApiClient.register(username, password);

    var createdUser = Awaitility
        .await("Ожидаем создания пользователя")
        .atMost(60000, TimeUnit.MILLISECONDS)
        .pollInterval(10000, TimeUnit.MILLISECONDS)
        .ignoreExceptions()
        .until(() -> userApiClient.currentUser(username), Objects::nonNull);

    return new UserJson(
        createdUser.id(),
        createdUser.username(),
        createdUser.firstname(),
        createdUser.surname(),
        createdUser.currency(),
        createdUser.photo(),
        createdUser.friendState(),
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

    categories.forEach(categoryData -> {
      var categoryJson = new CategoryJson(
          null,
          categoryData.description(),
          createdUser.username()
      );

      var existedUserCategories = categoryApiClient.getCategories(categoryData.username());
      var existedUserCategory = Objects.requireNonNull(existedUserCategories).stream()
          .filter(s -> s.category().equals(categoryData.description()))
          .findAny();

      var categoryForTest = existedUserCategory.orElseGet(() -> categoryApiClient.addCategory(categoryJson));
      createdCategories.add(categoryForTest);
    });

    return createdCategories;
  }

  @Override
  public List<SpendJson> createSpends(CreateUser user, UserJson createdUser) {
    var spends = Arrays.stream(user.spends());
    List<SpendJson> createdSpends = new ArrayList<>();

    spends.forEach(spend -> {
      var existedUserCategories = categoryApiClient.getCategories(createdUser.username());
      var existedUserCategory = Objects.requireNonNull(existedUserCategories).stream()
          .filter(s -> s.category().equals(spend.category()))
          .findAny()
          .orElseThrow();

      var spendJson = new SpendJson(
          null,
          new Date(),
          existedUserCategory.category(),
          spend.currency(),
          spend.amount(),
          spend.description(),
          createdUser.username()
      );

      var createdSpend = spendApiClient.addSpend(spendJson);
      createdSpends.add(createdSpend);
    });
    return createdSpends;
  }

  @Override
  public void deleteSpend(UUID id) {

  }

  @Override
  public void deleteCategory(UUID id) {

  }

  @Override
  public void deleteUser(UUID id) {

  }

  @Override
  public UserJson createRandomUser() {
    throw new NotImplementedException();
  }

  @Override
  public void createFriendship(UUID firstFriendId, UUID secondFriendId) {
    throw new NotImplementedException();
  }
}
