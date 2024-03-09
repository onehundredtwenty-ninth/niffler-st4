package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.CreateUser;
import guru.qa.niffler.jupiter.annotation.CreateUsers;
import guru.qa.niffler.jupiter.annotation.Friends.FriendshipRequestType;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.User.Point;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public abstract class CreateUserExtension implements BeforeEachCallback, ParameterResolver, AfterEachCallback {

  public static final ExtensionContext.Namespace CREATE_USER_NAMESPACE
      = ExtensionContext.Namespace.create(CreateUserExtension.class);

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    Map<User.Point, List<CreateUser>> usersForTest = extractUsersForTest(extensionContext);
    List<CategoryJson> createdCategories = new ArrayList<>();
    List<SpendJson> createdSpends = new ArrayList<>();

    Map<User.Point, List<UserJson>> createdUsers = new HashMap<>();
    for (Map.Entry<User.Point, List<CreateUser>> userInfo : usersForTest.entrySet()) {
      List<UserJson> usersForPoint = new ArrayList<>();
      for (CreateUser createUser : userInfo.getValue()) {
        var createdUser = createUser(createUser);
        usersForPoint.add(createdUser);

        createdCategories.addAll(createCategories(createUser, createdUser));
        createdSpends.addAll(createSpends(createUser, createdUser));
      }
      createdUsers.put(userInfo.getKey(), usersForPoint);
    }

    extensionContext.getStore(CREATE_USER_NAMESPACE).put(extensionContext.getUniqueId(), createdUsers);
    extensionContext.getStore(CREATE_USER_NAMESPACE)
        .put(extensionContext.getUniqueId() + "createdCategories", createdCategories);
    extensionContext.getStore(CREATE_USER_NAMESPACE)
        .put(extensionContext.getUniqueId() + "createdSpends", createdSpends);

    var innerUserJson = createdUsers.get(Point.INNER).get(0);
    var innerUser = usersForTest.get(Point.INNER).get(0);
    List<UserJson> futureFriends = new ArrayList<>();
    if (innerUser.friends().handle()) {
      for (int i = 0; i < innerUser.friends().count(); i++) {
        var createdUser = createRandomUser();
        futureFriends.add(createdUser);

        if (!innerUser.friends().pending()) {
          createFriendship(innerUserJson.id(), createdUser.id(), false);
        } else {
          if (innerUser.friends().friendshipRequestType() == FriendshipRequestType.OUTCOME) {
            createFriendship(innerUserJson.id(), createdUser.id(), true);
          } else {
            createFriendship(createdUser.id(), innerUserJson.id(), true);
          }
        }
      }
    }
    extensionContext.getStore(CREATE_USER_NAMESPACE)
        .put(extensionContext.getUniqueId() + "createdFriends", futureFriends);
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return AnnotationSupport.findAnnotation(parameterContext.getParameter(), User.class).isPresent() &&
        (parameterContext.getParameter().getType().isAssignableFrom(UserJson.class) ||
            parameterContext.getParameter().getType().isAssignableFrom(UserJson[].class));
  }

  @SuppressWarnings("unchecked")
  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    User user = AnnotationSupport.findAnnotation(parameterContext.getParameter(), User.class).orElseThrow();
    Map<User.Point, List<UserJson>> createdUsers = extensionContext.getStore(CREATE_USER_NAMESPACE)
        .get(extensionContext.getUniqueId(), Map.class);
    List<UserJson> userJsons = createdUsers.get(user.value());
    if (parameterContext.getParameter().getType().isAssignableFrom(UserJson[].class)) {
      return userJsons.stream().toList().toArray(new UserJson[0]);
    } else {
      return userJsons.get(0);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void afterEach(ExtensionContext extensionContext) throws Exception {
    Map<User.Point, List<UserJson>> createdUsers = extensionContext.getStore(CREATE_USER_NAMESPACE)
        .get(extensionContext.getUniqueId(), Map.class);
    List<UserJson> createdFriends = extensionContext.getStore(CREATE_USER_NAMESPACE)
        .get(extensionContext.getUniqueId() + "createdFriends", List.class);
    List<CategoryJson> createdCategories = extensionContext.getStore(CREATE_USER_NAMESPACE)
        .get(extensionContext.getUniqueId() + "createdCategories", List.class);
    List<SpendJson> createdSpends = extensionContext.getStore(CREATE_USER_NAMESPACE)
        .get(extensionContext.getUniqueId() + "createdSpends", List.class);

    createdSpends.forEach(s -> deleteSpend(s.id()));
    createdCategories.forEach(s -> deleteCategory(s.id()));
    createdFriends.forEach(s -> deleteUser(s.id()));
    createdUsers.forEach((k, v) -> v.forEach(user -> deleteUser(user.id())));
  }

  public abstract UserJson createUser(CreateUser user);

  public abstract List<CategoryJson> createCategories(CreateUser user, UserJson createdUser);

  public abstract List<SpendJson> createSpends(CreateUser user, UserJson createdUser);

  public abstract void deleteSpend(UUID id);

  public abstract void deleteCategory(UUID id);

  public abstract void deleteUser(UUID id);

  public abstract UserJson createRandomUser();

  public abstract void createFriendship(UUID firstFriendId, UUID secondFriendId, Boolean isPending);

  private Map<User.Point, List<CreateUser>> extractUsersForTest(ExtensionContext context) {
    Map<User.Point, List<CreateUser>> result = new HashMap<>();
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), ApiLogin.class).ifPresent(
        apiLogin -> {
          CreateUser user = apiLogin.user();
          if (user.handle()) {
            result.put(User.Point.INNER, List.of(user));
          }
        }
    );
    List<CreateUser> outerUsers = new ArrayList<>();
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), CreateUser.class).ifPresent(
        tu -> {
          if (tu.handle()) {
            outerUsers.add(tu);
          }
        }
    );
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), CreateUsers.class).ifPresent(
        CreateUsers -> Arrays.stream(CreateUsers.value())
            .filter(CreateUser::handle)
            .forEach(outerUsers::add)
    );
    result.put(User.Point.OUTER, outerUsers);
    return result;
  }
}
