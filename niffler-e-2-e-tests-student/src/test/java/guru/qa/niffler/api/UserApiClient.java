package guru.qa.niffler.api;

import guru.qa.niffler.model.UserJson;
import java.util.List;
import lombok.SneakyThrows;

public class UserApiClient extends RestClient {

  private final UserApi userApi;

  public UserApiClient() {
    super("http://127.0.0.1:8089");
    userApi = retrofit.create(UserApi.class);
  }

  @SneakyThrows
  public UserJson updateUserInfo(UserJson userJson) {
    return userApi.updateUserInfo(userJson).execute().body();
  }

  @SneakyThrows
  public UserJson currentUser(String username) {
    return userApi.currentUser(username).execute().body();
  }

  @SneakyThrows
  public List<UserJson> allUsers(String username) {
    return userApi.allUsers(username).execute().body();
  }
}
