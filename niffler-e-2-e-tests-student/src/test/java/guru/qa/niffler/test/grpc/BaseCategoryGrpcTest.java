package guru.qa.niffler.test.grpc;

import guru.qa.grpc.niffler.grpc.NifflerCategoriesServiceGrpc;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.GrpcTest;
import guru.qa.niffler.utils.GrpcConsoleInterceptor;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;

@GrpcTest
public abstract class BaseCategoryGrpcTest {

  protected static final Config CFG = Config.getInstance();
  protected static Channel channel;
  protected static NifflerCategoriesServiceGrpc.NifflerCategoriesServiceBlockingStub blockingStub;
  protected static NifflerCategoriesServiceGrpc.NifflerCategoriesServiceStub stub;

  static {
    channel = ManagedChannelBuilder.forAddress(CFG.spendGrpcHost(), CFG.spendGrpcPort())
        .intercept(new AllureGrpc(), new GrpcConsoleInterceptor())
        .usePlaintext()
        .build();

    blockingStub = NifflerCategoriesServiceGrpc.newBlockingStub(channel);
    stub = NifflerCategoriesServiceGrpc.newStub(channel);
  }
}
