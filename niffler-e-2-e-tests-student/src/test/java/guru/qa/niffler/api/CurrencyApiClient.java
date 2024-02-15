package guru.qa.niffler.api;

import guru.qa.niffler.model.CurrencyCalculateJson;
import guru.qa.niffler.model.CurrencyJson;
import java.util.List;
import lombok.SneakyThrows;

public class CurrencyApiClient extends RestClient {

  private final CurrencyApi currencyApi;

  public CurrencyApiClient() {
    super("http://127.0.0.1:8091");
    this.currencyApi = retrofit.create(CurrencyApi.class);
  }

  @SneakyThrows
  public List<CurrencyJson> getAllCurrencies() {
    return currencyApi.getAllCurrencies().execute().body();
  }

  @SneakyThrows
  public CurrencyCalculateJson calculate(CurrencyCalculateJson currencyCalculate) {
    return currencyApi.calculate(currencyCalculate).execute().body();
  }
}
