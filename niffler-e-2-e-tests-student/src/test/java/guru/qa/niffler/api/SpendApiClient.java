package guru.qa.niffler.api;

import guru.qa.niffler.model.SpendJson;
import lombok.SneakyThrows;

public class SpendApiClient extends RestClient {

  private final SpendApi spendApi;

  public SpendApiClient() {
    super("http://127.0.0.1:8093");
    spendApi = retrofit.create(SpendApi.class);
  }

  @SneakyThrows
  public SpendJson addSpend(SpendJson spend) {
    return spendApi.addSpend(spend).execute().body();
  }
}
