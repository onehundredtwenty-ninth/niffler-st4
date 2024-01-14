package guru.qa.niffler.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class SpendClient {

  public Retrofit getDefault() {
    return new Retrofit.Builder()
        .client(new OkHttpClient.Builder().build())
        .baseUrl("http://127.0.0.1:8093")
        .addConverterFactory(JacksonConverterFactory.create())
        .build();
  }
}
