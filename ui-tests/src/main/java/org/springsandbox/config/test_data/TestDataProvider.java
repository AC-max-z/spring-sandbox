package org.springsandbox.config.test_data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springsandbox.config.test_data.customer.CustomerTestDataYaml;
import org.springsandbox.utils.ObjectMapperProvider;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class TestDataProvider {
    private static final ObjectMapper YAML_MAPPER = ObjectMapperProvider.getInstance();
    private static final String DEFAULT_CUSTOMER_TEST_DATA_FILE_PATH = "src/test/resources/data/customer-tests-data-provider.yml";
    private static final String CUSTOMER_TEST_DATA_PATH_ARG = "customerTestDataFilePath";

    public static CustomerTestDataYaml provideCustomerData() {
        var customerTestDataFilePath = DEFAULT_CUSTOMER_TEST_DATA_FILE_PATH;
        try {
            customerTestDataFilePath = System.getProperty(CUSTOMER_TEST_DATA_PATH_ARG);
        } catch (IllegalArgumentException | NullPointerException ignored) {
        }
        try {
            return YAML_MAPPER.readValue(new File(customerTestDataFilePath),
                    CustomerTestDataYaml.class);
        } catch (IOException e) {
            var logger = LoggerFactory.getLogger(TestDataProvider.class.getSimpleName());
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        return null;
    }
}
