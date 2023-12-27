package com.maxzamota.spring_sandbox;

import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Epic("Context load Spring unit test")
@Severity(SeverityLevel.BLOCKER)
class SpringBootExampleApplicationTests {

    @Test
    void contextLoads(ApplicationContext ctx) {
        Allure.suite("Context load Spring unit test");
        assertThat(ctx).isNotNull().satisfies(c -> {
            System.out.println("Got app context with id: " + c.getId());
            System.out.println("Application name: " + c.getApplicationName());
            assertThat(c.getApplicationName()).isNotNull();
            assertThat(c.getId()).isNotNull();
            assertThat(c.getBean("basicAppConfiguration")).isNotNull();
        });
    }

}
