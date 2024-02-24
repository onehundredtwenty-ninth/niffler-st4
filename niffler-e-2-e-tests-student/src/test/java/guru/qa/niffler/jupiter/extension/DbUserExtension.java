package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.UserRepositorySupplier;
import guru.qa.niffler.db.logging.JsonAllureAppender;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.DbUser;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public class DbUserExtension implements BeforeEachCallback, ParameterResolver, AfterEachCallback {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(DbUserExtension.class);
  public final Faker faker = new Faker();

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(UserAuthEntity.class)
        && extensionContext.getRequiredTestMethod().isAnnotationPresent(DbUser.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE)
        .get(getStoreKeyForAuth(extensionContext.getUniqueId()), UserAuthEntity.class);
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    var userRepository = new UserRepositorySupplier().get();
    var userAuth = context.getStore(NAMESPACE).get(getStoreKeyForAuth(context.getUniqueId()), UserAuthEntity.class);
    var user = context.getStore(NAMESPACE).get(getStoreKeyForUserData(context.getUniqueId()), UserEntity.class);

    userRepository.deleteInAuthById(userAuth.getId());
    userRepository.deleteInUserdataById(user.getId());
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    var userRepository = new UserRepositorySupplier().get();
    var dbUser = AnnotationSupport.findAnnotation(
        context.getRequiredTestMethod(),
        DbUser.class
    ).orElseGet(() -> {
      Optional<ApiLogin> apiLogin = AnnotationSupport.findAnnotation(
          context.getRequiredTestMethod(),
          ApiLogin.class
      );
      return apiLogin.map(ApiLogin::user).orElse(null);
    });

    if (dbUser != null) {
      var userAuth = new UserAuthEntity();
      userAuth.setUsername(dbUser.username().isBlank() ? faker.name().username() : dbUser.username());
      userAuth.setPassword(dbUser.password().isBlank() ? faker.internet().password() : dbUser.password());

      userAuth.setEnabled(true);
      userAuth.setAccountNonExpired(true);
      userAuth.setAccountNonLocked(true);
      userAuth.setCredentialsNonExpired(true);
      userAuth.setAuthorities(Arrays.stream(Authority.values())
          .map(e -> {
            AuthorityEntity ae = new AuthorityEntity();
            ae.setAuthority(e);
            return ae;
          }).toList()
      );

      var user = new UserEntity();
      user.setUsername(userAuth.getUsername());
      user.setCurrency(CurrencyValues.RUB);
      userRepository.createInAuth(userAuth);
      userRepository.createInUserdata(user);

      context.getStore(NAMESPACE).put(getStoreKeyForAuth(context.getUniqueId()), userAuth);
      context.getStore(NAMESPACE).put(getStoreKeyForUserData(context.getUniqueId()), user);
      new JsonAllureAppender().logJson(user);
    }
  }

  public static String getStoreKeyForAuth(String uniqueId) {
    return uniqueId + "-userAuth";
  }

  public static String getStoreKeyForUserData(String uniqueId) {
    return uniqueId + "-user";
  }
}
