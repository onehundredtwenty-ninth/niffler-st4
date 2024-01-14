package guru.qa.niffler.jupiter;

import static guru.qa.niffler.jupiter.User.UserType.COMMON;
import static guru.qa.niffler.jupiter.User.UserType.WITH_FRIENDS;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class UsersQueueExtension implements AfterTestExecutionCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE
      = ExtensionContext.Namespace.create(UsersQueueExtension.class);
  private static final Map<User.UserType, Queue<UserJson>> users = new ConcurrentHashMap<>();

  static {
    Queue<UserJson> friendsQueue = new ConcurrentLinkedQueue<>();
    Queue<UserJson> commonQueue = new ConcurrentLinkedQueue<>();
    friendsQueue.add(user("dima", "12345", WITH_FRIENDS));
    friendsQueue.add(user("duck", "12345", WITH_FRIENDS));
    commonQueue.add(user("bee", "123", COMMON));
    commonQueue.add(user("barsik", "12345", COMMON));
    users.put(WITH_FRIENDS, friendsQueue);
    users.put(COMMON, commonQueue);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void afterTestExecution(ExtensionContext context) {
    var usersFromTest = context.getStore(NAMESPACE).get(context.getUniqueId(), List.class);
    usersFromTest.forEach(s -> {
      var userFromTest = (UserJson) s;
      users.get(userFromTest.testData().userType()).add(userFromTest);
    });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class)
        && parameterContext.getParameter().isAnnotationPresent(User.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    var user = getUserFromPool(parameterContext.getParameter());
    addUserToStore(extensionContext, user);
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), List.class)
        .get(parameterContext.getIndex());
  }

  @SuppressWarnings("unchecked")
  private void addUserToStore(ExtensionContext context, UserJson user) {
    if (context.getStore(NAMESPACE).get(context.getUniqueId()) == null) {
      context.getStore(NAMESPACE).put(context.getUniqueId(), new ArrayList<>(List.of(user)));
    } else {
      context.getStore(NAMESPACE).get(context.getUniqueId(), List.class).add(user);
    }
  }

  private UserJson getUserFromPool(Parameter parameter) {
    UserJson testCandidate = null;
    var queue = users.get(parameter.getAnnotation(User.class).value());
    while (testCandidate == null) {
      testCandidate = queue.poll();
    }
    return testCandidate;
  }

  private static UserJson user(String username, String password, User.UserType userType) {
    return new UserJson(
        null,
        username,
        null,
        null,
        CurrencyValues.RUB,
        null,
        null,
        new TestData(
            password,
            userType
        )
    );
  }
}
