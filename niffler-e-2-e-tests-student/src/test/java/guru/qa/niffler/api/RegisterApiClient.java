package guru.qa.niffler.api;

import guru.qa.niffler.api.interceptor.RequestCookieInterceptor;
import guru.qa.niffler.api.interceptor.ResponceCookieInterceptor;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import lombok.SneakyThrows;

public class RegisterApiClient extends RestClient {

  private final RegisterApi registerApi;

  public RegisterApiClient() {
    super(CFG.registerUrl(),
        true,
        new ResponceCookieInterceptor(), new RequestCookieInterceptor());
    registerApi = retrofit.create(RegisterApi.class);
  }

  @SneakyThrows
  public void getRegisterCookies() {
    registerApi.getRegisterCookies().execute();
  }

  @SneakyThrows
  public void register(String username, String password) {
    registerApi.register(username, password, password, ApiLoginExtension.getCsrfToken()).execute();
  }
}
