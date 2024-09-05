Configurable UI tests with raw Selenium and Junit.

| Configuration           | Default file location                                  | Custom file path program arg |
|-------------------------|--------------------------------------------------------|------------------------------|
| Env configuration       | `src/main/resources/environment.yml`                   | `envConfigFilePath`          |
| Driver configuration    | `src/main/resources/driver.yml`                        | `driverConfigFilePath`       |
| Customer test data feed | `test/resources/data/customer-tests-data-provider.yml` | `customerTestDataFilePath`   |

Number of fork threads for tests is currently specified in `build.gradle.kts` but probably should be moved to
configuration as well.

Its overengineered by design.