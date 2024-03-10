package guru.qa.niffler.service;

import com.google.protobuf.Empty;
import guru.qa.grpc.niffler.grpc.CurrencyValues;
import guru.qa.grpc.niffler.grpc.DeleteRequest;
import guru.qa.grpc.niffler.grpc.NifflerSpendServiceGrpc;
import guru.qa.grpc.niffler.grpc.Spend;
import guru.qa.grpc.niffler.grpc.SpendsRequest;
import guru.qa.grpc.niffler.grpc.SpendsResponse;
import guru.qa.grpc.niffler.grpc.StatisticsResponse;
import guru.qa.niffler.data.CategoryEntity;
import guru.qa.niffler.data.SpendEntity;
import guru.qa.niffler.data.repository.CategoryRepository;
import guru.qa.niffler.data.repository.SpendRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class GrpcSpendService extends NifflerSpendServiceGrpc.NifflerSpendServiceImplBase {

  private final SpendRepository spendRepository;
  private final CategoryRepository categoryRepository;

  @Autowired
  public GrpcSpendService(SpendRepository spendRepository, CategoryRepository categoryRepository) {
    this.spendRepository = spendRepository;
    this.categoryRepository = categoryRepository;
  }

  @Override
  public void getSpends(SpendsRequest request, StreamObserver<SpendsResponse> responseObserver) {
    var spends = getSpendsEntityForUser(
        request.getUsername(),
        request.getCurrencyValues(),
        dateFromString(request.getFrom()), dateFromString(request.getTo())
    ).toList();

    var spendsResponse = SpendsResponse.newBuilder()
        .addAllSpends(spends.stream().map(spendEntity ->
                Spend.newBuilder()
                    .setId(spendEntity.getId().toString())
                    .setSpendDate(spendEntity.getSpendDate().toString())
                    .setCategory(spendEntity.getCategory().getCategory())
                    .setCurrency(CurrencyValues.valueOf(spendEntity.getCurrency().toString()))
                    .setAmount(spendEntity.getAmount())
                    .setDescription(spendEntity.getDescription())
                    .setUsername(spendEntity.getUsername())
                    .build()
            ).toList()
        ).build();

    responseObserver.onNext(spendsResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getStatistic(SpendsRequest request, StreamObserver<StatisticsResponse> responseObserver) {
    responseObserver.onError(Status.UNIMPLEMENTED.withDescription("Unable get statistic by grpc").asRuntimeException());
  }

  @Override
  public void addSpend(Spend request, StreamObserver<Spend> responseObserver) {
    final String username = request.getUsername();
    final String category = request.getCategory();

    SpendEntity spendEntity = new SpendEntity();
    spendEntity.setUsername(username);
    spendEntity.setSpendDate(dateFromString(request.getSpendDate()));
    spendEntity.setCurrency(guru.qa.niffler.model.CurrencyValues.valueOf(request.getCurrency().toString()));
    spendEntity.setDescription(request.getDescription());
    spendEntity.setAmount(request.getAmount());

    CategoryEntity categoryEntity = categoryRepository.findAllByUsername(username)
        .stream()
        .filter(c -> c.getCategory().equals(category))
        .findFirst()
        .orElseThrow();

    spendEntity.setCategory(categoryEntity);
    spendEntity = spendRepository.save(spendEntity);

    var response = Spend.newBuilder()
        .setId(spendEntity.getId().toString())
        .setSpendDate(spendEntity.getSpendDate().toString())
        .setCategory(spendEntity.getCategory().getCategory())
        .setCurrency(CurrencyValues.valueOf(spendEntity.getCurrency().toString()))
        .setAmount(spendEntity.getAmount())
        .setDescription(spendEntity.getDescription())
        .setUsername(spendEntity.getUsername())
        .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void editSpend(Spend request, StreamObserver<Spend> responseObserver) {
    Optional<SpendEntity> spendById = spendRepository.findById(UUID.fromString(request.getId()));
    if (spendById.isEmpty()) {
      responseObserver.onError(Status.CANCELLED.withDescription(
              "Can`t find spend by given id: " + request.getId())
          .asRuntimeException());
    } else {
      final String category = request.getCategory();
      CategoryEntity categoryEntity = categoryRepository.findAllByUsername(request.getUsername())
          .stream()
          .filter(c -> c.getCategory().equals(category))
          .findFirst()
          .orElseThrow();

      SpendEntity spendEntity = spendById.get();
      spendEntity.setSpendDate(dateFromString(request.getSpendDate()));
      spendEntity.setCategory(categoryEntity);
      spendEntity.setAmount(request.getAmount());
      spendEntity.setDescription(request.getDescription());
      spendEntity = spendRepository.save(spendEntity);

      var response = Spend.newBuilder()
          .setId(spendEntity.getId().toString())
          .setSpendDate(spendEntity.getSpendDate().toString())
          .setCategory(spendEntity.getCategory().getCategory())
          .setCurrency(CurrencyValues.valueOf(spendEntity.getCurrency().toString()))
          .setAmount(spendEntity.getAmount())
          .setDescription(spendEntity.getDescription())
          .setUsername(spendEntity.getUsername())
          .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }

  @Transactional
  @Override
  public void deleteSpend(DeleteRequest request, StreamObserver<Empty> responseObserver) {
    spendRepository.deleteByUsernameAndIdIn(request.getUsername(),
        request.getIdsList().stream().map(UUID::fromString).toList());

    responseObserver.onNext(Empty.getDefaultInstance());
    responseObserver.onCompleted();
  }

  private @Nonnull Stream<SpendEntity> getSpendsEntityForUser(@Nonnull String username,
      @Nullable CurrencyValues filterCurrency,
      @Nullable Date dateFrom,
      @Nullable Date dateTo) {
    var currency = filterCurrency == CurrencyValues.UNSPECIFIED
        ? null
        : guru.qa.niffler.model.CurrencyValues.valueOf(filterCurrency.toString());

    dateTo = dateTo == null
        ? new Date()
        : dateTo;

    List<SpendEntity> spends = dateFrom == null
        ? spendRepository.findAllByUsernameAndSpendDateLessThanEqual(username, dateTo)
        : spendRepository.findAllByUsernameAndSpendDateGreaterThanEqualAndSpendDateLessThanEqual(username, dateFrom,
            dateTo);

    return spends.stream()
        .filter(se -> currency == null || se.getCurrency() == currency);
  }

  public static Date dateFromString(String dateAsString) {
    if (dateAsString.isBlank()) {
      return null;
    }
    return Date.from(
        LocalDate.parse(dateAsString, DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH))
            .atStartOfDay()
            .toInstant(ZoneOffset.UTC)
    );
  }
}
