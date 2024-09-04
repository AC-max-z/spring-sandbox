package org.springsandbox.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class ObjectMapperProvider {
    private static final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    public static ObjectMapper getInstance() {
        return yamlMapper;
    }
}
