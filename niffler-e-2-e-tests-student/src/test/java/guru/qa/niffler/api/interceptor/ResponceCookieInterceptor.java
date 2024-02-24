package guru.qa.niffler.api.interceptor;

import guru.qa.niffler.api.cookie.ThreadSafeCookieManager;
import java.io.IOException;
import java.net.HttpCookie;
import okhttp3.Cookie;
import okhttp3.Interceptor;
import okhttp3.Response;

public class ResponceCookieInterceptor implements Interceptor {

  @Override
  public Response intercept(Chain chain) throws IOException {
    final Response response = chain.proceed(chain.request());

    var cookies = Cookie.parseAll(chain.request().url(), response.headers());
    cookies.forEach(s -> {
      var httpCookie = new HttpCookie(s.name(), s.value());
      ThreadSafeCookieManager.INSTANCE.add(chain.request().url().uri(), httpCookie);
    });

    return response;
  }
}
