package org.springsandbox.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springsandbox.utils.ObjectMapperProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class ConfigsProvider {
    private static final ObjectMapper YAML_MAPPER = ObjectMapperProvider.getInstance();
    private static final String DEFAULT_ENV_CONFIG_FILE_PATH = "src/main/resources/environment.yml";
    private static final String DEFAULT_DRIVER_CONFIG_FILE_PATH = "src/main/resources/driver.yml";
    private static final String CUSTOM_ENV_CONFIG_PATH_ARG = "envConfigFilePath";
    private static final String CUSTOM_DRIVER_CONFIG_PATH_ARG = "driverConfigFilePath";
    private static final String APP_PROPERTIES_FILE_PATH = "src/main/resources/application.properties";

    private static EnvConfig envConfig;
    private static DriverConfig driverConfig;
    private static Map<String, String> appProperties;

    private static void setConfigs() {
        var envConfigFilePath = DEFAULT_ENV_CONFIG_FILE_PATH;
        var driverConfigFilePath = DEFAULT_DRIVER_CONFIG_FILE_PATH;
        try {
            envConfigFilePath = System.getProperty(CUSTOM_ENV_CONFIG_PATH_ARG);
        } catch (IllegalArgumentException | NullPointerException ignored) {
        }
        try {
            driverConfigFilePath = System.getProperty(CUSTOM_DRIVER_CONFIG_PATH_ARG);
        } catch (IllegalArgumentException | NullPointerException ignored) {
        }
        try {
            envConfig = YAML_MAPPER.readValue(new File(envConfigFilePath), EnvConfig.class);
            driverConfig = YAML_MAPPER.readValue(new File(driverConfigFilePath), DriverConfig.class);
        } catch (IOException e) {
            handleConfigReadError(e);
        }
    }

    private static void setAppProperties() {
        appProperties = new HashMap<>(System.getenv());
        try {
            var appPropsFile = new File(APP_PROPERTIES_FILE_PATH);
            var scanner = new Scanner(appPropsFile);
            while (scanner.hasNextLine()) {
                var line = scanner.nextLine();
                var propName = line.split("=")[0];
                var value = line.split("=")[1];
                // args have higher priority
                if (!appProperties.containsKey(propName)) {
                    appProperties.put(propName, value);
                }
            }
        } catch (FileNotFoundException e) {
            handleConfigReadError(e);
        }
    }

    private static void handleConfigReadError(Throwable e) {
        var logger = LoggerFactory.getLogger(ConfigsProvider.class.getSimpleName());
        logger.error(e.getMessage());
        logger.error(Arrays.toString(e.getStackTrace()));
        System.exit(1);
    }

    public static Map<String, String> getAppProperties() {
        if (Objects.isNull(appProperties)) {
            setAppProperties();
        }
        return appProperties;
    }

    public static DriverConfig getDriverConfig() {
        if (Objects.isNull(driverConfig)) {
            setConfigs();
        }
        return driverConfig;
    }

    public static EnvConfig getEnvConfig() {
        if (Objects.isNull(envConfig)) {
            setConfigs();
        }
        return envConfig;
    }
}
