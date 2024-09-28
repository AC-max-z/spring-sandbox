sup

**WHAT THE FUCK IS EVEN THAT?**

You may say. And the answer is...

Configurable UI tests with raw Selenium and Junit. With "auto-waits" and stuff.

| Configuration           | Default file location                                      | Custom file path program arg |
|-------------------------|------------------------------------------------------------|------------------------------|
| Env configuration       | `src/main/resources/environment.yml`                       | `envConfigFilePath`          |
| Driver configuration    | `src/main/resources/driver.yml`                            | `driverConfigFilePath`       |
| Customer test data feed | `src/test/resources/data/customer-tests-data-provider.yml` | `customerTestDataFilePath`   |

Env config example:
```yaml
# URL of application under test
appUrl: https://2girls1cup.com/
# URL of Selenium Grid to connect to (for Remote Drivers)
gridUrl: http://my.grid.url/wd/hub
# true if Selenoid Grid is to be used
selenoidEnabled: true
# Selenoid Grid option (enables VNC connections to remote driver sessions)
selenoidVncEnabled: true
# Selenoid Grid option (captures video recordings of remote driver sessions)
selenoidVideoEnabled: false
```

Driver config example:

```yaml
# WebDriver fluent wait timeout in ms
waitTimeoutMillis: 10000
# WebDriver default implicit wait time in ms
implicitWaitMillis: 500
# WebDriver fluent wait polling interval in ms
pollingIntervalMillis: 50
chromeLocalLoggingEnabled: true
chromeLocalLogPath: src/test/resources/logs/chrome.log
firefoxLocalLoggingEnabled: true
firefoxLocalLogPath: src/test/resources/logs/firefox.log
```
Customer test data example:
```yaml
# Set of test data goes here
data:
    # Driver type used for the test
  - driverType: CHROME_REMOTE
    # Set of Customer domain objects provided for the test 
    customers:
      - name: Bob
        email: uncle-bob-molester-420@gmail.com
        age: 69
        gender: MALE
      - name: Paul
        email: paul@paul.paul
        age: 30
        gender: MALE
  - driverType: CHROME_REMOTE_HEADLESS
    customers:
      - name: Alice
        email: heriohgeri@giewhgwoieg.gewihgw
        age: 25
        gender: FEMALE
      - name: agiehgiewbovw
        email: xxx@xx.io
        age: 90
        gender: DIFFERENT

```


Number of fork threads for tests is currently specified in `build.gradle.kts` but probably should be moved to
configuration as well.

**HOW DOES IT WORK**

#TODO: use different tool(s) to make better diagrams

![img.png](src/test/resources/high_level_abstract_code_architecture.png)

#TODO: put project structure here...

__Driver layer__

`src/main/java/org.springsandbox/utils` package contains utility classes:
* Singleton class which instantiates and provides `ObjectMapper` object. It is used for reading YAML configuration files and mapping them into respective config classes;
* Driver logger utility class which contains static method(s) for collecting, providing and logging `WebDriver` log entries;
* Configuration class that reads data from YAML configs and instantiates config objects to be used across driver layer;
* Driver wait configuration class which configures `WebDriver` instance's fluent and implicit waits, and contains decorator methods for elements waiting, page load finish waiting while disabling/re-enabling implicit waits to not mix it with explicit ones (cuz mixing is bad, mkay?).

Driver factory class `src/main/java/org.springsandbox/factories/WebDriverFactory.java`
is responsible for supplying `WebDriver` object for requested `DriverType`.
It configures options, program arguments, log preferences and instantiates `WebDriver`.

`WebDriver`'s types are defined in enum class at 
`src/main/java/org.springsandbox/enums/DriverType.java`.

Page object classes which contain page elements and interaction logic are located in 
`src/main/java/org.springsandbox/pages` package.

The `BasePage.java` abstract class defines common 
fields and elements interactions logic to be re-used by all page classes inheriting from it.
It utilizes underlying waiting logic encapsulated in `src/main/java/org.springsandbox/utils/DriverWaitConfiguration.java`
class to dynamically wait for elements before interacting with them and automatically re-initializes `PageFactory` on `StaleElementReferenceException`.
So that you don't need to worry about any of it when implementing other Page classes on top of it.

`src/main/java/org.springsandbox/domain` package contains domain classes.

`src/main/java/org.springsandbox/config` contains config classes and test data provider classes to which sir `ObjectMapper` maps your YAML configuration files.

__Test layer__

Tests are located in `src/test/java/tests` package.

`BaseTest.java` outlines JUnit abstract test class with JUnit test extensions, execution mode,
test tags, fields and hooks that are common among all tests.
More specifically it contains:
* Thread safe `ThreadLocal<?>` variables for your `WebDriver` and `Logger` objects,
* `ScreenshotExtension` test extension that attaches screenshot to Allure results for your failed test,
* `setupDriver(DriverType driverType)` method that calls `WebDriverFactory` to instantiate `WebDriver` object of provided `DriverType`, puts it into `ThreadLocal<WebDriver>` variable and sets this driver instance for JUnit test extensions,
* `@BeforeEach` hook that instantiates logger object,
* `@AfterEach` hook that attaches WebDriver logs to your Allure results and quits the `WebDriver`.

Other test classes inherit from it and can and should be written in a parameterized way. That way you can provide driver types, test domain objects and other data you need via data provider methods, CSVs, YAMLs or otherwise.

`utils` package contains `Faker` provider singleton class, test data provider class to read data for your tests from YAML files and `TestStep` utility class with decorator methods that mark Allure steps and log this step at the same time.

`matchers` contains re-usable custom matchers for your tests.

`helpers` contains re-usable test steps for your test (e.g. login/logout/do this/do that, which may combine a number of more basic page interaction calls...)

`generators` contains domain object generator classes.

And finally `extensions` for your JUnit test extension classes if you need those.

That's about it. Thanks. Bye. Have a nice life/everything else.

*P.S*. Never actually use this shit. Better stick with your Playwright or whatever. 
Or, better yet, find a better job/career/the thing you do with your limited time in this universe in this timespan you are entrapped in.
Anyway, you do you, you surely know better...

*TLDR*: Data-driven, cross-browser, concurrent/parallel, "scalable" UI tests project skeleton sample with:
* raw Selenium,
* parameterized JUnit tests,
* Allure reporting,
* driver and tests layers responsibility separation,
* POM,
* `WebDriver` factory,
* Selenoid grid ready,
* auto-fluent waits and `PageFactory` re-init,
* tag based tests launch configuration,
* failed test retries,
* Allure TestOPS friendly test markup (that is actually generated automatically with import via integration IDE plugin btw),
* Allure screenshots attachments,
* Allure `WebDriver` logs attachments,
* YML test data feed,
* YML environment and `WebDriver` configuration,
* domain object generators,
* test helpers and matchers.

Don't. Made this for job interview purposes with a certain level of absurdity in mind.