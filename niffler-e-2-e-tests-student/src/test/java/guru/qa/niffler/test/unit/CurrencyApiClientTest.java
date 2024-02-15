package guru.qa.niffler.test.unit;

import guru.qa.niffler.api.CurrencyApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CurrencyApiClientTest {

  private final CurrencyApiClient currencyApiClient = new CurrencyApiClient();

  @Test
  void getAllCurrenciesTest() {
    var currencies = currencyApiClient.getAllCurrencies();
    Assertions.assertAll(
        () -> Assertions.assertTrue(currencies.size() > 0)
    );
  }
}
