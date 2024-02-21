package guru.qa.niffler.api;

import guru.qa.niffler.model.CurrencyCalculateJson;
import guru.qa.niffler.model.CurrencyJson;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CurrencyApi {

  @GET("/getAllCurrencies")
  Call<List<CurrencyJson>> getAllCurrencies();

  @POST("/calculate")
  Call<CurrencyCalculateJson> calculate(@Body CurrencyCalculateJson currencyCalculate);
}
