package guru.qa.niffler.jupiter;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.api.SpendClient;
import guru.qa.niffler.model.SpendJson;
import lombok.SneakyThrows;

public class RestSpendExtension extends SpendExtension {

  private final SpendApi spendApi = new SpendClient().getDefault().create(SpendApi.class);

  @SneakyThrows
  @Override
  public SpendJson create(SpendJson spend) {
    return spendApi.addSpend(spend).execute().body();
  }
}
