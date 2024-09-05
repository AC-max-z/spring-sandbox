Configurable UI tests with raw Selenium and Junit.

You can provide environment configuration in file `sr/main/resources/environment.yml` file that is used by default, or provide program argument `envConfigFilePath` with path to custom env config file.

Driver configuration is provided in `src/main/resources/driver.yml` by default or with a `driverConfigFilePath` arg pointing to custom config file.

To feed tests with data you can use `test/resources/data/customer-tests-data-provider.yml` file by default or custom file specifying path in `customerTestDataFilePath` arg.

Number of fork threads for tests is currently specified in `build.gradle.kts` but should be moved to configuration as well.

Its overengineered by design.