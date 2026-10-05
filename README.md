# SauceDemo UI Automation

Selenium + TestNG suite covering the key user journeys on <https://www.saucedemo.com>.
Java, Maven, Page Object Model, Chrome, GitHub Actions.

## Scenarios

| Scenario                                            | Test                                                | Groups                             |
| --------------------------------------------------- | --------------------------------------------------- | ---------------------------------- |
| Successful login                                    | `LoginTest.standardUserCanLogIn`                    | `smoke`, `login`                   |
| Product list sorted A-Z and Z-A                     | `ProductSortTest.productsAreSortedByName`           | `regression`, `sorting`            |
| Add a product, remove it, add it again              | `CartTest.productCanBeAddedRemovedAndReAdded`       | `smoke`, `cart`                    |
| Full checkout (First `ABC`, Last `DEF`, Zip `123456`) | `CheckoutTest.orderCanBeCompleted`                  | `smoke`, `checkout`                |
| Checkout with blank first name shows an error       | `CheckoutTest.blankFirstNameShowsError`             | `regression`, `checkout`, `negative` |
| Twitter (X) and Facebook icons open the right links | `SocialLinksTest.socialIconOpensCorrectLink`        | `regression`, `social`             |

The sort and social tests are data-driven, so the suite reports 8 test results.

> SauceDemo has rebranded its Twitter icon to X: the icon now links to
> `https://x.com/saucelabs`, and that is what the test asserts.

## Prerequisites

- **JDK 17 or newer** (`java -version`)
- **Google Chrome**

That is all. Maven does not need to be installed, because the repo includes the Maven
wrapper (`./mvnw`). ChromeDriver does not need to be installed either: Selenium Manager
(built into Selenium 4) downloads the driver matching your Chrome on first run.

## Running the tests

```bash
./mvnw clean test
```

On Windows use `mvnw.cmd clean test`. If you have Maven installed, `mvn clean test` works
the same way.

Useful options:

| Command                                   | Effect                                         |
| ----------------------------------------- | ---------------------------------------------- |
| `./mvnw clean test -Dheadless=true`       | Run without opening a browser window           |
| `./mvnw clean test -Dgroups=smoke`        | Run one group (see the table above)            |
| `./mvnw clean test -Dgroups=cart,checkout` | Run several groups                             |
| `./mvnw clean test -DtimeoutSeconds=20`   | Change the element wait timeout (default 10 s) |
| `./mvnw clean test -DbaseUrl=https://...` | Point the suite at another environment         |

Headless mode switches on automatically when the `CI` environment variable is `true`.

## Reports and logs

Everything is written under `target/` after a run:

| Path                                          | What it is                                                         |
| --------------------------------------------- | ------------------------------------------------------------------ |
| `target/surefire-reports/index.html`          | TestNG HTML report: results by class and group, timings, stack traces |
| `target/surefire-reports/emailable-report.html` | Single-file HTML summary, easy to share                            |
| `target/surefire-reports/testng-results.xml`  | TestNG XML results                                                 |
| `target/surefire-reports/junitreports/`       | JUnit-style XML, understood by most CI dashboards                  |
| `target/logs/test-run.log`                    | Step-by-step log (the same lines are printed to the console)       |
| `target/screenshots/`                         | A screenshot per failed test, named after the test method          |

Open `index.html` in a browser to read the report. Green means passed and red means failed;
click a failed method to see the assertion message and stack trace, then find the matching
screenshot in `target/screenshots/`.

In the log, each test has a `START` line, indented `>` lines for every page action, and a
`PASSED` / `FAILED` / `SKIPPED` line, followed by a `FINISHED` total.

A report from a real run is committed in [`sample-report/`](sample-report/) as an example.

## CI/CD (GitHub Actions)

The pipeline is defined in [`.github/workflows/ui-tests.yml`](.github/workflows/ui-tests.yml).
It runs:

- on every push to `main` and on every pull request
- on demand: **Actions > UI Tests > Run workflow** (optionally entering a group such as `smoke`)

Each run executes the suite in headless Chrome, prints the pass/fail list on the run's
summary page, and uploads a **`test-report`** artifact containing the HTML/XML reports, the
logs, the full console output and any failure screenshots. Download it from the bottom of the
run's summary page.

No secrets or manual setup are needed: the runner image already has Chrome, and the workflow
installs JDK 17.

## Project structure

```
src/main/java/com/saucedemo/
  config/Config.java            Run settings (URL, credentials, timeout, headless)
  driver/DriverFactory.java     Builds the Chrome session
  pages/                        Page objects
    BasePage.java               Abstract parent: waits, click, type, isLoaded()
    LoginPage, InventoryPage, CartPage,
    CheckoutInfoPage, CheckoutOverviewPage, CheckoutCompletePage
  model/
    SortOption.java             Sort dropdown entries and the order each should produce
    SocialLink.java             Abstract footer icon; TwitterLink and FacebookLink extend it
  utils/
    BrowserUtils.java           New-tab handling, screenshots
    Log.java                    Console + file logging
src/test/java/com/saucedemo/
  tests/
    BaseTest.java               Opens/closes the browser around each test
    LoggedInTest.java           Adds login for tests that start on the product list
    LoginTest, ProductSortTest, CartTest, CheckoutTest, SocialLinksTest
  listeners/TestListener.java   Pass/fail logging and failure screenshots
src/test/resources/testng.xml   Suite definition
```

## Design notes

- **Page Object Model.** Tests never touch locators. Each page class owns its locators and
  exposes actions that return the next page (`loginPage.loginAsStandardUser()` returns an
  `InventoryPage`), so a journey reads as a chain of steps.
- **Polymorphism.**
  - `SocialLink` is an abstract class; `TwitterLink` and `FacebookLink` each override the
    locator, expected href and accepted domains. `SocialLinksTest` has one test method that
    works on the `SocialLink` type and is fed both subclasses.
  - `BasePage.isLoaded()` relies on the abstract `readyLocator()` that every page overrides,
    so `BaseTest.assertLoaded(page)` works for any page object.
- **Helpers.** Waiting, clicking and typing live once in `BasePage`; browser-level helpers
  live in `BrowserUtils`. There are no `Thread.sleep` calls; everything uses explicit waits.
- **Dependencies.** Only Selenium and TestNG. Logging uses `java.util.logging` and reports
  are TestNG's built-in ones, so there is nothing extra to maintain.

## Extending

- **New page:** extend `BasePage`, implement `readyLocator()`, add action methods.
- **New test:** extend `LoggedInTest` (or `BaseTest` to start on the login page), tag it with
  groups, and add the class to `src/test/resources/testng.xml`.
- **New social icon:** add a `SocialLink` subclass and one line in the `socialLinks` data
  provider.

## Troubleshooting

- **Driver download fails behind a proxy/firewall:** Selenium Manager needs internet access on
  first run. Set `HTTPS_PROXY`, or install ChromeDriver yourself and put it on your `PATH`.
- **Surefire version:** the plugin is pinned to 3.5.x on purpose. Version 3.6.0 no longer
  supports `testng.xml` suite files.
