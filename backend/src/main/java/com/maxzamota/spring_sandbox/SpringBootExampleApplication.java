package com.maxzamota.spring_sandbox;

import com.maxzamota.spring_sandbox.configuration.security.RsaConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableConfigurationProperties(RsaConfigurationProperties.class)
@EnableAspectJAutoProxy
public class SpringBootExampleApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(SpringBootExampleApplication.class, args);
    }
}
