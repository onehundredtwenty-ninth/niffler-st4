package guru.qa.niffler.jupiter.extension;

import static guru.qa.niffler.jupiter.annotation.User.UserType.COMMON;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECEIVED;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SEND;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class UsersQueueExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE
      = ExtensionContext.Namespace.create(UsersQueueExtension.class);
  private static final Map<User.UserType, Queue<UserJson>> users = new ConcurrentHashMap<>();

  static {
    Queue<UserJson> friendsQueue = new ConcurrentLinkedQueue<>();
    Queue<UserJson> commonQueue = new ConcurrentLinkedQueue<>();
    Queue<UserJson> invitationSend = new ConcurrentLinkedQueue<>();
    Queue<UserJson> invitationReceived = new ConcurrentLinkedQueue<>();

    friendsQueue.add(user("dima", "12345", WITH_FRIENDS));
    friendsQueue.add(user("duck", "12345", WITH_FRIENDS));

    commonQueue.add(user("bee", "123", COMMON));
    commonQueue.add(user("barsik", "12345", COMMON));

    invitationSend.add(user("barsik2", "123", INVITATION_SEND));

    invitationReceived.add(user("barsik3", "123", INVITATION_RECEIVED));

    users.put(WITH_FRIENDS, friendsQueue);
    users.put(COMMON, commonQueue);
    users.put(INVITATION_SEND, invitationSend);
    users.put(INVITATION_RECEIVED, invitationReceived);
  }

  @Override
  public void beforeEach(ExtensionContext context) {
    var parametersFromTestMethod = Arrays.stream(context.getRequiredTestMethod().getParameters())
        .filter(s -> s.getAnnotation(User.class) != null && s.getType().isAssignableFrom(UserJson.class))
        .toList();
    var parametersFromBeforeMethod = getParamsFromBeforeEach(context);
    validateParameters(parametersFromTestMethod, parametersFromBeforeMethod);

    var allUsersForTest = new ArrayList<UserJson>();

    parametersFromTestMethod.forEach(s -> {
      var testCandidate = getUserFromPool(s);
      allUsersForTest.add(testCandidate);
    });

    if (parametersFromTestMethod.isEmpty() && !parametersFromBeforeMethod.isEmpty()) {
      parametersFromBeforeMethod.forEach(s -> {
        var testCandidate = getUserFromPool(s);
        allUsersForTest.add(testCandidate);
      });
    }
    context.getStore(NAMESPACE).put(context.getUniqueId(), allUsersForTest);
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
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), List.class)
        .get(parameterContext.getIndex());
  }

  private UserJson getUserFromPool(Parameter parameter) {
    UserJson testCandidate = null;
    var queue = users.get(parameter.getAnnotation(User.class).value());
    while (testCandidate == null) {
      testCandidate = queue.poll();
    }
    return testCandidate;
  }

  private void validateParameters(List<Parameter> parametersFromTestMethod,
      List<Parameter> parametersFromBeforeMethod) {
    if (!parametersFromBeforeMethod.isEmpty() && !parametersFromTestMethod.isEmpty()) {
      if (parametersFromBeforeMethod.size() != parametersFromTestMethod.size()) {
        throw new IllegalStateException("Если мы подставляем пользователей И в before метод И в тестовый метод "
            + "одновременно, то их количество должно совпадать");
      }

      for (int i = 0; i < parametersFromBeforeMethod.size(); i++) {
        var beforeMethodParameterValue = parametersFromBeforeMethod.get(i).getAnnotation(User.class).value();
        var testMethodParameterValue = parametersFromTestMethod.get(i).getAnnotation(User.class).value();

        if (!beforeMethodParameterValue.equals(testMethodParameterValue)) {
          throw new IllegalStateException("Тип пользователей переданных в before метод не совпадает с типами "
              + "пользователей, переданных в тестовый метод");
        }
      }
    }
  }

  private List<Parameter> getParamsFromBeforeEach(ExtensionContext context) {
    return Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
        .filter(s -> s.getAnnotation(BeforeEach.class) != null && s.getParameterCount() != 0)
        .flatMap(s -> Arrays.stream(s.getParameters()))
        .filter(s -> s.getAnnotation(User.class) != null && s.getType().isAssignableFrom(UserJson.class))
        .toList();
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
