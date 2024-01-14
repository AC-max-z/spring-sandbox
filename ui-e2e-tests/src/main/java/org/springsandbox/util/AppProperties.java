package org.springsandbox.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class AppProperties {
    private static Map<String, String> appProperties;

    private static void setAppProperties() {
        appProperties = new HashMap<>(System.getenv());
        String appPropertiesFilePath = "src/main/resources/application.properties";
        try {
            File appProps = new File(appPropertiesFilePath);
            Scanner scanner = new Scanner(appProps);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String propName = line.split("=")[0];
                String value = line.split("=")[1];
                appProperties.put(propName, value);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Oopsie-woopsie, file not found at: " + appPropertiesFilePath);
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    public static Map<String, String> getProperties() {
        if (Objects.isNull(appProperties)) {
            setAppProperties();
        }
        return appProperties;
    }
}
