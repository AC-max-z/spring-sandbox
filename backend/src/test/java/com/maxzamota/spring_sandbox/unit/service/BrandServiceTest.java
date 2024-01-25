package com.maxzamota.spring_sandbox.unit.service;

import com.maxzamota.spring_sandbox.repository.jpa.BrandRepository;
import com.maxzamota.spring_sandbox.service.BrandService;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@Execution(ExecutionMode.CONCURRENT)
@Epic("Brand service unit tests")
@Tags({
        @Tag("unit-test"),
        @Tag("service-layer")
})
@Severity(SeverityLevel.BLOCKER)
public class BrandServiceTest {
    private BrandService serviceUnderTest;
    AutoCloseable autoCloseable;

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

    @ParameterizedTest
    @ValueSource(ints = {100, 69, 42, 420, 1})
    @DisplayName("Should invoke persistence layer method findAll() when ")
    void name() {
    }
}
