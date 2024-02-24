package guru.qa.niffler.api.interceptor;

import guru.qa.niffler.api.cookie.ThreadSafeCookieManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;

public class RequestCookieInterceptor implements Interceptor {

  @Override
  public Response intercept(Chain chain) throws IOException {
    var request = chain.request();

    request = request.newBuilder()
        .addHeader("Cookie", ThreadSafeCookieManager.INSTANCE.cookiesAsString())
        .build();

    return chain.proceed(request);
  }
}
