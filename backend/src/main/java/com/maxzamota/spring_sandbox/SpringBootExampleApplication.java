package com.maxzamota.spring_sandbox;

import com.maxzamota.spring_sandbox.configuration.security.RsaConfigurationProperties;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;

@SpringBootApplication
//@EnableConfigurationProperties(RsaConfigurationProperties.class)
@EnableAspectJAutoProxy
@Slf4j
public class SpringBootExampleApplication {

    public static void main(String[] args) {
        var logger = LoggerFactory.getLogger(SpringBootExampleApplication.class);
        logger.info("JVM information:");
        logger.info("JVM started with PID: {}", ManagementFactory.getRuntimeMXBean().getName());
        logger.info("JVM vendor: {}", ManagementFactory.getRuntimeMXBean().getVmVendor());
        logger.info("JVM name: {}", ManagementFactory.getRuntimeMXBean().getVmName());
        logger.info("JVM version: {}", ManagementFactory.getRuntimeMXBean().getVmVersion());
        logger.info("JVM arguments: {}", ManagementFactory.getRuntimeMXBean().getInputArguments());
        logger.info("Max memory: {}", Runtime.getRuntime().maxMemory());
        logger.info("Total memory: {}", Runtime.getRuntime().totalMemory());
        logger.info("Available processors: {}", Runtime.getRuntime().availableProcessors());
        for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            logger.info("GC used: {}", gc.getName());
        }
        ApplicationContext ctx = SpringApplication.run(SpringBootExampleApplication.class, args);
    }
}
