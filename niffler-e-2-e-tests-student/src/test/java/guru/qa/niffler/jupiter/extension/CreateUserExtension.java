package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.CreateUser;
import guru.qa.niffler.jupiter.annotation.CreateUsers;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public abstract class CreateUserExtension implements BeforeEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace CREATE_USER_NAMESPACE
      = ExtensionContext.Namespace.create(CreateUserExtension.class);

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    Map<User.Point, List<CreateUser>> usersForTest = extractUsersForTest(extensionContext);

    Map<User.Point, List<UserJson>> createdUsers = new HashMap<>();
    for (Map.Entry<User.Point, List<CreateUser>> userInfo : usersForTest.entrySet()) {
      List<UserJson> usersForPoint = new ArrayList<>();
      for (CreateUser CreateUser : userInfo.getValue()) {
        usersForPoint.add(createUser(CreateUser));
      }
      createdUsers.put(userInfo.getKey(), usersForPoint);
    }

    extensionContext.getStore(CREATE_USER_NAMESPACE).put(extensionContext.getUniqueId(), createdUsers);
  }

  public abstract UserJson createUser(CreateUser user);

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
