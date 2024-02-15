package guru.qa.niffler.api;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.StatisticJson;
import java.time.LocalDate;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SpendApi {

  @GET("/spends")
  Call<List<SpendJson>> spends(@Query("username") String username,
      @Query("filterCurrency") CurrencyValues filterCurrency,
      @Query("from") LocalDate from,
      @Query("to") LocalDate to
  );

  @GET("/statistic")
  Call<List<StatisticJson>> statistic(@Query("username") String username,
      @Query("userCurrency") CurrencyValues userCurrency,
      @Query("filterCurrency") CurrencyValues filterCurrency,
      @Query("from") LocalDate from,
      @Query("to") LocalDate to
  );

  @POST("/addSpend")
  Call<SpendJson> addSpend(@Body SpendJson spend);

  @PATCH("/editSpend")
  Call<SpendJson> editSpend(@Body SpendJson spend);

  @DELETE("/deleteSpends")
  Call<Void> deleteSpends(@Query("username") String username, @Query("ids") List<String> ids);
}
