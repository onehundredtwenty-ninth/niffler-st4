package guru.qa.niffler.service;

import guru.qa.grpc.niffler.grpc.Category;
import guru.qa.grpc.niffler.grpc.CategoryResponse;
import guru.qa.grpc.niffler.grpc.NifflerCategoriesServiceGrpc;
import guru.qa.grpc.niffler.grpc.UserName;
import guru.qa.niffler.data.CategoryEntity;
import guru.qa.niffler.data.repository.CategoryRepository;
import guru.qa.niffler.model.CategoryJson;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@GrpcService
public class GrpcCategoryService extends NifflerCategoriesServiceGrpc.NifflerCategoriesServiceImplBase {

  private static final Logger LOG = LoggerFactory.getLogger(GrpcCategoryService.class);
  private static final int MAX_CATEGORIES_SIZE = 7;
  private final CategoryRepository categoryRepository;

  @Autowired
  public GrpcCategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public void getAllCategories(UserName request, StreamObserver<CategoryResponse> responseObserver) {
    var categories = categoryRepository.findAllByUsername(request.getUsername())
        .stream()
        .map(CategoryJson::fromEntity)
        .toList();

    var categoryResponse = CategoryResponse.newBuilder()
        .addAllAllCategories(categories.stream().map(s ->
            Category.newBuilder()
                .setId(s.id().toString())
                .setCategory(s.category())
                .setUsername(s.username())
                .build()).toList()
        )
        .build();

    responseObserver.onNext(categoryResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void addCategory(Category request, StreamObserver<Category> responseObserver) {
    if (categoryRepository.findAllByUsername(request.getUsername()).size() > MAX_CATEGORIES_SIZE) {
      LOG.error("Can`t add over than 7 categories for user: {}", request.getUsername());
      responseObserver.onError(Status.CANCELLED.withDescription(
              "Can`t add over than 7 categories for user: " + request.getUsername())
          .asRuntimeException());
    }

    var categoryEntity = new CategoryEntity();
    categoryEntity.setCategory(request.getCategory());
    categoryEntity.setUsername(request.getUsername());

    try {
      categoryEntity = categoryRepository.save(categoryEntity);
    } catch (DataIntegrityViolationException e) {
      LOG.error("### Error while creating category: {}", e.getMessage());
      responseObserver.onError(Status.ABORTED.withDescription(
          "Category with name '" + request.getCategory() + "' already exists").asRuntimeException()
      );
    }

    var response = Category.newBuilder()
        .setId(categoryEntity.getId().toString())
        .setCategory(categoryEntity.getCategory())
        .setUsername(categoryEntity.getUsername())
        .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
