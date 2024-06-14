package com.maxzamota.spring_sandbox.unit.service;

import com.maxzamota.spring_sandbox.model.ProductEntity;
import com.maxzamota.spring_sandbox.repository.jpa.ProductRepository;
import com.maxzamota.spring_sandbox.service.ProductService;
import com.maxzamota.spring_sandbox.util.generators.ProductGenerator;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@Execution(ExecutionMode.CONCURRENT)
@Epic("Product service unit tests")
@Tags({
        @Tag("unit-test"),
        @Tag("service-layer")
})
@Severity(SeverityLevel.BLOCKER)
public class ProductServiceTest {
    private ProductService serviceUnderTest;
    private AutoCloseable autoCloseable;
    private final ThreadLocal<Logger> loggerThreadLocal = new ThreadLocal<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String allureSuiteName = "Product service unit tests";

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        this.serviceUnderTest = new ProductService(this.productRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }
// TODO: fix tests
//    @ParameterizedTest
//    @EnumSource(ProductSortType.class)
//    @NullSource
//    @DisplayName("Should return properly ordered list of products when calling getAllSorted() method")
//    @Tags({
//            @Tag("positive"),
//            @Tag("negative"),
//            @Tag("parameterized")
//    })
//    @AllureId("PRODSERV-001")
//    @TmsLink("PRODSERV-001")
//    @Issue("PRODSERV-001")
//    void shouldReturnSortedListOfProducts(ProductSortType sortType) {
//        this.loggerThreadLocal.set(LoggerFactory.getLogger(this.getClass()));
//        Logger logger = loggerThreadLocal.get();
//        Allure.suite(allureSuiteName);
//
//        // Arrange
//        step("Generate list of products");
//        var products = new ProductGenerator().buildList(69);
//        logger.info("Generated products:");
//        products.forEach(p -> {
//            try {
//                logger.info(mapper.writeValueAsString(p));
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        step("Save generated products");
//        var savedProducts = this.serviceUnderTest.saveAll(products);
//        var retrievedProducts = this.serviceUnderTest.getAll();
//        logger.info("Retrieved products:");
//        retrievedProducts.forEach(p -> {
//            try {
//                logger.info(mapper.writeValueAsString(p));
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//        // Act
//        step("Call service layer getAllSorted() method");
//        Collection<ProductEntity> sortedProducts = null;
//        try {
//            sortedProducts = this.serviceUnderTest.getAllSorted(sortType);
//        } catch (NullPointerException ignored) {
//        }
//        step("Filter sorted brands leaving only those we generated in this test");
//        if (Objects.nonNull(sortedProducts)) {
//            sortedProducts = sortedProducts.stream()
//                    .filter(retrievedProducts::contains)
//                    .toList();
//            logger.info("Sorted products:");
//            sortedProducts.forEach(p -> {
//                try {
//                    logger.info(mapper.writeValueAsString(p));
//                } catch (JsonProcessingException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//        }
//
//        // Assert
//        step("Check if sorted brands is sorted as expected and mock called");
//        switch (sortType) {
//            case BY_ID_ASC -> {
//                assertThat(sortedProducts)
//                        .isEqualTo(retrievedProducts.stream()
//                                .sorted(Comparator.comparingInt(ProductEntity::getId))
//                                .toList()
//                        );
//                verify(this.productRepository).findAllByOrderByIdAsc();
//            }
//            case BY_ID_DESC -> {
//                assertThat(sortedProducts)
//                        .isEqualTo(retrievedProducts.stream()
//                                .sorted(Comparator.comparingInt(ProductEntity::getId).reversed())
//                                .toList()
//                        );
//                verify(this.productRepository).findAllByOrderByIdDesc();
//            }
//            case BY_NAME_ASC -> {
//                assertThat(sortedProducts)
//                        .isEqualTo(retrievedProducts.stream()
//                                .sorted(Comparator.comparing(ProductEntity::getName))
//                                .toList()
//                        );
//                verify(this.productRepository).findAllByOrderByNameAsc();
//            }
//            case BY_NAME_DESC -> {
//                assertThat(sortedProducts)
//                        .isEqualTo(retrievedProducts.stream()
//                                .sorted(Comparator.comparing(ProductEntity::getName).reversed())
//                                .toList()
//                        );
//                verify(this.productRepository).findAllByOrderByNameDesc();
//            }
//            case null -> assertThatThrownBy(
//                    () -> this.serviceUnderTest.getAllSorted(sortType)
//            )
//                    .isInstanceOf(NullPointerException.class);
//        }
//    }
}
