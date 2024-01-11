package org.springsandbox.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AppProperties {
    public static Map<String, String> getProperties() {
        HashMap<String, String> properties = new HashMap<>(System.getenv());
        String appPropertiesFilePath = "src/main/resources/application.properties";
        try {
            File appProps = new File(appPropertiesFilePath);
            Scanner scanner = new Scanner(appProps);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String propName = line.split("=")[0];
                String value = line.split("=")[1];
                properties.put(propName, value);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Oopsie-woopsie, file not found at: " + appPropertiesFilePath);
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return properties;
    }
}
