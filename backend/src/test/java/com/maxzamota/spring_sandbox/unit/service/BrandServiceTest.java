package com.maxzamota.spring_sandbox.unit.service;

import com.maxzamota.spring_sandbox.exception.DuplicateResourceException;
import com.maxzamota.spring_sandbox.exception.ResourceNotFoundException;
import com.maxzamota.spring_sandbox.model.BrandEntity;
import com.maxzamota.spring_sandbox.repository.BrandRepository;
import com.maxzamota.spring_sandbox.service.BrandService;
import com.maxzamota.spring_sandbox.util.generators.BrandGenerator;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@Execution(ExecutionMode.CONCURRENT)
@Epic("Brand service unit tests")
@Tags({
        @Tag("unit-test"),
        @Tag("service-layer")
})
@Severity(SeverityLevel.BLOCKER)
public class BrandServiceTest {
    private BrandService serviceUnderTest;
    private AutoCloseable autoCloseable;
    private final ThreadLocal<Logger> loggerThreadLocal = new ThreadLocal<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String allureSuiteName = "Brand service unit tests";

    @Mock
    private BrandRepository brandRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        this.serviceUnderTest = new BrandService(this.brandRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }
// TODO: fix tests
//    @ParameterizedTest
//    @EnumSource(BrandSortType.class)
//    @NullSource
//    @DisplayName("Should return properly sorted list of brands when getSortedBrands() is called or throws an exception if null is passed")
//    @Tags({
//            @Tag("positive"),
//            @Tag("negative"),
//            @Tag("parameterized")
//    })
//    @AllureId("BRANDSERV-001")
//    @TmsLink("BRANDSERV-001")
//    @Issue("BRANDSERV-001")
//    void shouldInvokeReturnProperlySortedListOfBrands(BrandSortType sortType) {
//        this.loggerThreadLocal.set(LoggerFactory.getLogger(this.getClass()));
//        Logger logger = loggerThreadLocal.get();
//        Allure.suite(allureSuiteName);
//
//        // Arrange
//        step("Generate list of brands");
//        var generatedBrands = new BrandGenerator().buildList(69);
//        try (MDC.MDCCloseable mdc = MDC.putCloseable("userId", "mzamota")) {
//            logger.info("Generated brands:");
//            generatedBrands.forEach(b -> {
//                try {
//                    logger.info(mapper.writeValueAsString(b));
//                } catch (JsonProcessingException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//
//        }
//        step("Save generated brands");
//        var savedBrands = this.serviceUnderTest.saveAll(generatedBrands);
//        var retrievedBrands = this.serviceUnderTest.getAll();
//        logger.info("Retrieved brands:");
//        retrievedBrands.forEach(b -> logger.info(b.toString()));
//
//        // Act
//        step("Call service layer getSortedBrands() method");
//        Collection<BrandEntity> sortedBrands = null;
//        try {
//            sortedBrands = this.serviceUnderTest.getSortedBrands(sortType);
//        } catch (NullPointerException ignored) {
//        }
//        step("Filter sorted brands leaving only those we generated");
//        if (Objects.nonNull(sortedBrands)) {
//            sortedBrands = sortedBrands.stream()
//                    .filter(retrievedBrands::contains)
//                    .toList();
//            logger.info("Sorted brands:");
//            sortedBrands.forEach(b -> {
//                try {
//                    logger.info(mapper.writeValueAsString(b));
//                } catch (JsonProcessingException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//        }
//
//        // Assert
//        step("Verify that sorted brands is sorted as expected");
//        switch (sortType) {
//            case BY_NAME_ASC -> assertThat(sortedBrands)
//                    .isEqualTo(retrievedBrands.stream()
//                            .sorted(Comparator.comparing(BrandEntity::getName))
//                            .toList()
//                    );
//            case BY_NAME_DESC -> assertThat(sortedBrands)
//                    .isEqualTo(retrievedBrands.stream()
//                            .sorted(Comparator.comparing(BrandEntity::getName).reversed())
//                            .toList()
//                    );
//            case BY_ID_ASC -> assertThat(sortedBrands)
//                    .isEqualTo(retrievedBrands.stream()
//                            .sorted(Comparator.comparingInt(BrandEntity::getId))
//                            .toList()
//                    );
//            case BY_ID_DESC -> assertThat(sortedBrands)
//                    .isEqualTo(retrievedBrands.stream()
//                            .sorted(Comparator.comparingInt(BrandEntity::getId).reversed())
//                            .toList()
//                    );
//            case null ->
//                    assertThrows(NullPointerException.class, () -> this.serviceUnderTest.getSortedBrands(sortType));
//        }
//    }

    @Test
    @DisplayName("Should return existing brand when getBrandById() is called with existing id param")
    @Description("This test checks that getBrandById() service layer method returns same " +
            "BrandEntity object as repository returns when existing id param has been passed")
    @Tags({
            @Tag("positive")
    })
    @AllureId("BRANDSERV-002")
    @TmsLink("BRANDSERV-002")
    @Issue("BRANDSERV-002")
    @Severity(SeverityLevel.BLOCKER)
    void shouldReturnBrandIfExistsById() throws JsonProcessingException {
        this.loggerThreadLocal.set(LoggerFactory.getLogger(this.getClass()));
        Logger logger = loggerThreadLocal.get();
        Allure.suite(allureSuiteName);

        // Arrange
        step("Generate new brand");
        var id = 42;
        var generatedBrand = new BrandGenerator().withId(id).generate();
        logger.info("Generated brand:");
        logger.info(mapper.writeValueAsString(generatedBrand));
        step("Setup repository mock");
        when(this.brandRepository.findById(id)).thenReturn(Optional.of(generatedBrand));

        // Act
        step("Call getBrandById() service layer method");
        BrandEntity actual = null;
        try {
            actual = this.serviceUnderTest.getBrandByIdOrThrow(id);
        } catch (ResourceNotFoundException ignored) {
        }

        // Assert
        step("Verify mock is called");
        logger.info("Actual brand:");
        logger.info(mapper.writeValueAsString(actual));
        verify(this.brandRepository)
                .findById(id);
        step("Verify expected brand object is returned");
        assertThat(actual).isEqualTo(generatedBrand);
    }

    @ParameterizedTest
    @ValueSource(ints = {42, 69, 420, 66676, 0, -1})
    @DisplayName("Should throw ResourceNotFoundException when no brand with provided id found when calling getBrandById()")
    @Description("This test calls application layer getBrandById() method providing non-existent brand id as parameter" +
            " and verifies that ResourceNotFoundException is thrown")
    @Tags({
            @Tag("negative"),
            @Tag("parameterized")
    })
    @AllureId("BRANDSERV-003")
    @TmsLink("BRANDSERV-003")
    @Issue("BRANDSERV-003")
    void shouldThrowNotFoundExceptionWhenNoBrandById(int id) {
        Allure.suite(allureSuiteName);

        // Arrange
        step("Setup repository mock");
        when(this.brandRepository.findById(id)).thenReturn(Optional.empty());
        // Act
        step("Call getBrandById()");
        BrandEntity actual = null;
        try {
            actual = this.serviceUnderTest.getBrandByIdOrThrow(id);
        } catch (ResourceNotFoundException ignored) {
        }

        // Assert
        step("Verify that actual is null");
        assertThat(actual).isNull();
        step("Verify that mock is called");
        verify(this.brandRepository).findById(id);
        step("Verify that exception is thrown");
        assertThatThrownBy(() -> this.serviceUnderTest.getBrandByIdOrThrow(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Brand with id={%s} not found!".formatted(id));
    }

    @Test
    @DisplayName("Should save and return new BrandEntity when save() method is called")
    @Description("This test generates new BrandEntity object, calls application layer save() method " +
            "providing this generated object as parameter, verifies that BrandEntity object is returned " +
            "and that we can retrieve it calling getBrandById() application layer method")
    @Tags({
            @Tag("positive")
    })
    @AllureId("BRANDSERV-004")
    @TmsLink("BRANDSERV-004")
    @Issue("BRANDSERV-004")
    void shouldSaveBrand() {
        Allure.suite(allureSuiteName);

        // Arrange
        step("Generate brand object");
        var brand = new BrandGenerator().withId(42).generate();
        step("Setup repository mock");
        when(this.brandRepository.save(brand)).thenReturn(brand);
        when(this.brandRepository.findById(brand.getId())).thenReturn(Optional.of(brand));
        // Act
        step("Call save() method");
        var savedBrand = this.serviceUnderTest.save(brand);
        step("Call getBrandById() method");
        var retrievedBrand = this.serviceUnderTest.getBrandByIdOrThrow(brand.getId());
        // Assert
        step("Verify mock was called");
        verify(this.brandRepository).save(brand);
        verify(this.brandRepository).findById(brand.getId());
        step("Verify BrandEntity object is returned when calling save() method");
        assertThat(savedBrand).isEqualTo(brand);
        step("Verify BrandEntity object is returned when calling getBrandById() method");
        assertThat(retrievedBrand).isEqualTo(brand);
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when calling save() method providing brand with existing name")
    @Description("This test generates new BrandEntity, setups repository mock so that it " +
            "would return true when existsBrandByName() repository method is called with generated BrandEntity name " +
            "and verifies that DuplicateResourceException is thrown when calling application layer save() method providing generated brand as parameter")
    @Tags({
            @Tag("negative")
    })
    @AllureId("BRANDSERV-005")
    @TmsLink("BRANDSERV-005")
    @Issue("BRANDSERV-005")
    void shouldThrowDublicateExceptionWhenProvidedBrandNameExists() {
        Allure.suite(allureSuiteName);

        // Arrange
        step("Generate new BrandEntity object");
        var brand = new BrandGenerator().generate();
        step("Setup repository mock");
        when(this.brandRepository.existsByName(brand.getName()))
                .thenReturn(true);
        // Act
        step("Call save()");
        try {
            this.serviceUnderTest.save(brand);
        } catch (DuplicateResourceException ignored) {
        }
        // Assert
        step("Verify that existsBrandByName() repository mock method was called");
        verify(this.brandRepository, atMostOnce()).existsByName(brand.getName());
        step("Verify that save() repository mock method was never called");
        verify(this.brandRepository, never()).save(any());
        step("Verify exception is thrown");
        assertThatThrownBy(() -> this.serviceUnderTest.save(brand))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Brand with name={%s} already exists!".formatted(brand.getName()));
    }

    @ParameterizedTest
    @ValueSource(ints = {42, 69, 0, -1, 159761326})
    @DisplayName("Should return String message when deleteById() is called")
    @Description("This test calls delete() application layer method and checks that " +
            "repository mock deleteById() method was invoked and String message is returned")
    @Tags({
            @Tag("positive"),
            @Tag("parameterized")
    })
    @AllureId("BRANDSERV-006")
    @TmsLink("BRANDSERV-006")
    @Issue("BRANDSERV-006")
    void shouldDeleteBrandAndReturnStringMessage(int brandId) {
        Allure.suite(allureSuiteName);

        // Arrange
        // Act
        step("Call delete() method");
        var result = this.serviceUnderTest.deleteById(brandId);
        // Assert
        step("Verify mock was called");
        verify(this.brandRepository, atMostOnce()).deleteById(brandId);
        step("Verify string message returned");
        assertThat(result)
                .isEqualTo("Entity with id={%s} successfully deleted (or ignored if it did not exist in the first place)!".formatted(brandId));
    }


    @Test
    @DisplayName("Should update existing brand with unique name when update() method is called")
    @Description(
            "In this test 2 brand objects are generated, one is for initial brand data " +
            "another is for updated. First we call application layer save() method providing initial brand, " +
            "then we call update() application layer method providing updated brand and verify that update() " +
            "invokes save() repository mock method and returns updated brand data."
    )
    @Tags({
            @Tag("positive")
    })
    @AllureId("BRANDSERV-007")
    @TmsLink("BRANDSERV-007")
    @Issue("BRANDSERV-007")
    void shouldUpdateExistingBrandWithUniqueName() {
        Allure.suite(allureSuiteName);

        // Arrange
        step("Generate initial brand");
        var initialBrand = new BrandGenerator().withId(56).generate();
        step("Generate updated brand");
        var updatedBrand = new BrandGenerator()
                .withId(initialBrand.getId())
                .generate();
        step("Setup repository mock");
        when(this.brandRepository.save(initialBrand))
                .thenReturn(initialBrand);
        when(this.brandRepository.existsById(updatedBrand.getId()))
                .thenReturn(true);
        when(this.brandRepository.save(updatedBrand))
                .thenReturn(updatedBrand);
        when(this.brandRepository.findById(updatedBrand.getId()))
                .thenReturn(Optional.of(updatedBrand));
        // Act
        step("Call save() method");
        var initialSave = this.serviceUnderTest.save(initialBrand);
        step("Call update() method");
        var updateCall = this.serviceUnderTest.update(updatedBrand);
        step("Call getById() method");
        var getByIdCall = this.serviceUnderTest.getBrandByIdOrThrow(updatedBrand.getId());
        // Assert
        step("Verify mock was called");
        verify(this.brandRepository, atLeast(1)).save(any());
        verify(this.brandRepository, atMostOnce()).existsByName(initialBrand.getName());
        verify(this.brandRepository, atMostOnce()).existsById(updatedBrand.getId());
        verify(this.brandRepository, atMostOnce()).findAllByName(updatedBrand.getName());
        step("Verify expected brand data was returned");
        assertThat(initialSave).isEqualTo(initialBrand);
        assertThat(updateCall).isEqualTo(updatedBrand);
        assertThat(getByIdCall).isEqualTo(updatedBrand);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when calling update() providing brand with non-existent id")
    @Tags({
            @Tag("negative")
    })
    @AllureId("BRANDSERV-008")
    @TmsLink("BRANDSERV-008")
    @Issue("BRANDSERV-008")
    void shouldThrowResourceNotFoundExceptionWhenUpdatingBrandWithNonExistentId() {
        Allure.suite(this.allureSuiteName);

        // Arrange
        step("Generate brand");
        var brand = new BrandGenerator().withId(151236216).generate();
        // Act
        step("Call update()");
        try {
            this.serviceUnderTest.update(brand);
        } catch (ResourceNotFoundException ignored) {}
        // Assert
        step("Verify mock was called");
        verify(this.brandRepository).existsById(brand.getId());
        verify(this.brandRepository, never()).save(any());
        step("Verify exception thrown");
        assertThatThrownBy(() -> this.serviceUnderTest.update(brand))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Brand with id={%s} not found!".formatted(brand.getId()));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when calling update() providing brand with already taken name by another brand as parameter")
    @Tags({
            @Tag("negative")
    })
    @AllureId("BRANDSERV-009")
    @TmsLink("BRANDSERV-009")
    @Issue("BRANDSERV-009")
    void shouldThrowDuplicateResourceExceptionWhenCallingUpdateWithExistingBrandName() {
        Allure.suite(this.allureSuiteName);

        // Arrange
        step("Generate brand");
        var updatedBrand = new BrandGenerator().withId(69).generate();
        var existingBrand = new BrandGenerator()
                .withId(42)
                .withName(updatedBrand.getName())
                .generate();
        step("Setup mock");
        when(this.brandRepository.existsById(updatedBrand.getId()))
                .thenReturn(true);
        when(this.brandRepository.findAllByName(updatedBrand.getName()))
                .thenReturn(List.of(existingBrand, updatedBrand));
        // Act
        step("Call update()");
        try {
            this.serviceUnderTest.update(updatedBrand);
        } catch (DuplicateResourceException ignored) {}
        // Assert
        step("Verify mock was called");
        verify(this.brandRepository, atMostOnce())
                .findAllByName(updatedBrand.getName());
        verify(this.brandRepository, never()).save(any());
        step("Verify exception thrown");
        assertThatThrownBy(() -> this.serviceUnderTest.update(updatedBrand))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Brand with name={%s} already exists".formatted(existingBrand.getName()));
    }
}
