package guru.qa.niffler.api;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.StatisticJson;
import java.time.LocalDate;
import java.util.List;
import lombok.SneakyThrows;

public class SpendApiClient extends RestClient {

  private final SpendApi spendApi;

  public SpendApiClient() {
    super("http://127.0.0.1:8093");
    spendApi = retrofit.create(SpendApi.class);
  }

  @SneakyThrows
  public List<SpendJson> spends(String username, CurrencyValues filterCurrency, LocalDate from, LocalDate to) {
    return spendApi.spends(username, filterCurrency, from, to).execute().body();
  }

  @SneakyThrows
  public List<StatisticJson> statistic(String username, CurrencyValues userCurrency, CurrencyValues filterCurrency,
      LocalDate from, LocalDate to) {
    return spendApi.statistic(username, userCurrency, filterCurrency, from, to).execute().body();
  }

  @SneakyThrows
  public SpendJson addSpend(SpendJson spend) {
    return spendApi.addSpend(spend).execute().body();
  }

  @SneakyThrows
  public SpendJson editSpend(SpendJson spend) {
    return spendApi.editSpend(spend).execute().body();
  }

  @SneakyThrows
  public void deleteSpends(String username, List<String> ids) {
    spendApi.deleteSpends(username, ids).execute();
  }
}
