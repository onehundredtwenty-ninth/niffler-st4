package guru.qa.niffler.test.grpc;

import guru.qa.grpc.niffler.grpc.CurrencyValues;
import guru.qa.grpc.niffler.grpc.DeleteRequest;
import guru.qa.grpc.niffler.grpc.Spend;
import guru.qa.grpc.niffler.grpc.SpendsRequest;
import guru.qa.grpc.niffler.grpc.SpendsResponse;
import io.qameta.allure.Allure;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SpendGrpcTest extends BaseSpendGrpcTest {

  @Test
  void allSpendsTest() {
    final SpendsRequest request = SpendsRequest.newBuilder()
        .setUsername("bee")
        .setCurrencyValues(CurrencyValues.USD)
        .build();

    final SpendsResponse response = blockingStub.getSpends(request);

    Assertions.assertFalse(response.getSpendsList().isEmpty());

    final List<Spend> spendsList = response.getSpendsList();
    Spend spend = spendsList.get(0);
    Allure.step("Check category name", () ->
        Assertions.assertEquals("Обучение49", spend.getCategory()
        ));
  }

  @Test
  void addSpendTest() {
    final Spend request = Spend.newBuilder()
        .setCategory("Обучение")
        .setUsername("bee")
        .setSpendDate("2024-02-19")
        .setCurrency(CurrencyValues.EUR)
        .setAmount(24000.00)
        .setDescription(UUID.randomUUID().toString())
        .build();

    final Spend response = blockingStub.addSpend(request);
    Allure.step("Check description", () ->
        Assertions.assertEquals(request.getDescription(), response.getDescription()
        ));
  }

  @Test
  void editSpendTest() {
    final Spend request = Spend.newBuilder()
        .setCategory("Обучение")
        .setUsername("bee")
        .setSpendDate("2024-02-19")
        .setCurrency(CurrencyValues.EUR)
        .setAmount(24000.00)
        .setDescription(UUID.randomUUID().toString())
        .build();

    var response = blockingStub.addSpend(request);

    final Spend updateRequest = Spend.newBuilder()
        .setId(response.getId())
        .setCategory("Обучение")
        .setUsername("bee")
        .setSpendDate("2024-02-19")
        .setCurrency(CurrencyValues.EUR)
        .setAmount(24000.00)
        .setDescription(UUID.randomUUID().toString())
        .build();

    final Spend updateResponse = blockingStub.editSpend(updateRequest);

    Allure.step("Check description", () ->
        Assertions.assertEquals(updateRequest.getDescription(), updateResponse.getDescription()
        ));
  }

  @Test
  void deleteSpendTest() {
    final Spend request = Spend.newBuilder()
        .setCategory("Обучение")
        .setUsername("bee")
        .setSpendDate("2024-02-19")
        .setCurrency(CurrencyValues.EUR)
        .setAmount(24000.00)
        .setDescription(UUID.randomUUID().toString())
        .build();

    var response = blockingStub.addSpend(request);

    final DeleteRequest deleteRequest = DeleteRequest.newBuilder()
        .setUsername("bee")
        .addIds(response.getId())
        .build();

    var deleteResponse = blockingStub.deleteSpend(deleteRequest);
    Assertions.assertNotNull(deleteResponse);
  }
}
