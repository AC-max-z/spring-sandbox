package com.maxzamota.springbootexample;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SpringBootExampleApplicationTests {

    @Test
    void contextLoads(ApplicationContext ctx) {
        assertThat(ctx).isNotNull().satisfies(c -> {
            System.out.println("Got app context with id: " + c.getId());
            System.out.println("Application name: " + c.getApplicationName());
            assertThat(c.getApplicationName()).isNotNull();
            assertThat(c.getId()).isNotNull();
            assertThat(c.getBean("basicAppConfiguration")).isNotNull();
        });
    }

}
