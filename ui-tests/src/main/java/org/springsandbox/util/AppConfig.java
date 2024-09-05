package org.springsandbox.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springsandbox.config.DriverConfig;
import org.springsandbox.config.EnvConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class AppConfig {
    private static EnvConfig envConfig;
    private static DriverConfig driverConfig;
    private static Map<String, String> appProperties;
    private static ObjectMapper yamlMapper = ObjectMapperProvider.getInstance();

    private static void setAppConfig() {
        try {
            envConfig = yamlMapper.readValue(new File("src/main/resources/environment.yml"), EnvConfig.class);
            driverConfig = yamlMapper.readValue(new File("src/main/resources/driver.yml"), DriverConfig.class);
        } catch (IOException e) {
            Logger logger = LoggerFactory.getLogger(AppConfig.class.getSimpleName());
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
            System.exit(0);
        }
    }

    private static void setAppProperties() {
        appProperties = new HashMap<>(System.getenv());
        String appPropertiesFilePath = "src/main/resources/application.properties";
        try {
            File appPropsFile = new File(appPropertiesFilePath);
            Scanner scanner = new Scanner(appPropsFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String propName = line.split("=")[0];
                String value = line.split("=")[1];
                // args have higher priority
                if (!appProperties.containsKey(propName)) {
                    appProperties.put(propName, value);
                }
            }
        } catch (FileNotFoundException e) {
            Logger logger = LoggerFactory.getLogger(AppConfig.class.getSimpleName());
            logger.error("Oopsie-woopsie, file not found at: {}", appPropertiesFilePath);
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }
    }

    public static Map<String, String> getProperties() {
        if (Objects.isNull(appProperties)) {
            setAppProperties();
        }
        return appProperties;
    }

    public static DriverConfig getDriverConfig() {
        if (Objects.isNull(driverConfig)) {
            setAppConfig();
        }
        return driverConfig;
    }

    public static EnvConfig getEnvConfig() {
        if (Objects.isNull(envConfig)) {
            setAppConfig();
        }
        return envConfig;
    }
}
