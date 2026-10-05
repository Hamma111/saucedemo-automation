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

## Step-by-step setup

All code lives on the **`dev`** branch, which is the repository's default branch. `main` is
intentionally empty.

1. **Install a JDK (17 or newer).** On macOS with Homebrew:

   ```bash
   brew install openjdk
   ```

   This installs the latest JDK, which is fine: anything from 17 up works. On Windows or
   Linux, install any JDK 17+ build (for example Eclipse Temurin). Check it
   with `java -version`.

2. **Install Google Chrome** if it is not already installed.

3. **Clone the repository.** The clone checks out `dev` automatically.

   ```bash
   git clone https://github.com/hammadcui20/saucedemo-automation.git
   ```

4. **Go into the project folder.**

   ```bash
   cd saucedemo-automation
   ```

5. **Run the tests.** The first run downloads Maven, the libraries and ChromeDriver, so it
   takes a few minutes; later runs take about 30 seconds.

   ```bash
   ./mvnw clean test
   ```

   On Windows: `mvnw.cmd clean test`. Add `-Dheadless=true` to run without a visible browser.

6. **Check the result.** The console ends with `Tests run: 8, Failures: 0, Errors: 0,
   Skipped: 0` and `BUILD SUCCESS`.

7. **Open the HTML report.**

   ```bash
   open target/surefire-reports/index.html
   ```

   On Windows use `start`, on Linux `xdg-open`. See [Reports and logs](#reports-and-logs).

8. **Run it in the pipeline.** Push to `dev` or open a pull request, or go to
   **Actions > UI Tests > Run workflow**. When the run finishes, download the `test-report`
   artifact from the run's summary page. See [CI/CD](#cicd-github-actions).

9. **(Optional) Open it in an IDE.** In IntelliJ IDEA choose **File > Open** and select the
   project folder; it reads `pom.xml` and sets everything up. Right-click
   `src/test/resources/testng.xml` and choose **Run** to run the suite from the IDE.

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
| `./mvnw clean test -Dgroups=typo`         | Fails with "No tests were executed" (by design) |
| `./mvnw clean test -DtimeoutSeconds=20`   | Change the element wait timeout (default 10 s) |
| `./mvnw clean test -DbaseUrl=https://...` | Point the suite at another environment         |

Headless mode switches on automatically when the `CI` environment variable is `true`.

## Reports and logs

Everything is written under `target/` after a run (the screenshots folder only appears when
something fails):

| Path                                          | What it is                                                         |
| --------------------------------------------- | ------------------------------------------------------------------ |
| `target/surefire-reports/index.html`          | TestNG HTML report: results by class and group, timings, stack traces |
| `target/surefire-reports/emailable-report.html` | Single-file HTML summary, easy to share                            |
| `target/surefire-reports/testng-results.xml`  | TestNG XML results                                                 |
| `target/surefire-reports/junitreports/`       | JUnit-style XML, understood by most CI dashboards                  |
| `target/logs/test-run.log`                    | Step-by-step log (the same lines are printed to the console)       |
| `target/screenshots/`                         | A screenshot per failure, named `<method>-<timestamp>.png`         |

Open `index.html` in a browser to read the report. Green means passed and red means failed;
click a failed method to see the assertion message and stack trace, then find the matching
screenshot in `target/screenshots/`.

In the log, `SETUP` lines mark the browser start, login and browser close around each test.
Each test has a `START` line, indented `>` lines for every page action, and a
`PASSED` / `FAILED` / `SKIPPED` line. A `FINISHED` total closes the run. Passwords are masked.

If a setup step such as login fails, the log shows a `SETUP FAILED` line, a screenshot named
after the setup method is saved, the tests that depended on it are reported as skipped, and
the build fails.

A report from a real pipeline run is committed in [`sample-report/`](sample-report/) as an
example (its `test-run.log` sits next to the HTML there, rather than in a separate `logs` folder).

## CI/CD (GitHub Actions)

The pipeline is defined in [`.github/workflows/ui-tests.yml`](.github/workflows/ui-tests.yml).
It runs:

- on every push to `dev` and on every pull request
- on demand: **Actions > UI Tests > Run workflow** (optionally entering a group such as `smoke`)

Each run executes the suite in headless Chrome, prints the pass/fail list on the run's
summary page, and uploads a **`test-report`** artifact containing the HTML/XML reports, the
logs, the full console output and any failure screenshots. Download it from the bottom of the
run's summary page.

No secrets or manual setup are needed: the runner image already has Chrome, and the workflow
installs JDK 17.

## Project structure

```
pom.xml                         Build definition (dependencies, surefire, TestNG suite)
mvnw, mvnw.cmd, .mvn/           Maven wrapper, so Maven itself need not be installed
.github/workflows/ui-tests.yml  GitHub Actions pipeline
sample-report/                  Report from a real pipeline run, as an example
src/main/java/com/saucedemo/
  config/Config.java            Run settings (URL, credentials, timeout, headless)
  driver/DriverFactory.java     Builds the Chrome session
  pages/                        Page objects
    BasePage.java               Abstract parent: waits, click, type, isLoaded()
    LoginPage, InventoryPage, CartPage,
    CheckoutInfoPage, CheckoutOverviewPage, CheckoutCompletePage
  model/
    SortOption.java             Sort dropdown entries and the order each should produce
    SocialLink.java             Abstract footer icon
    TwitterLink.java, FacebookLink.java   The two concrete icons
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

## Known limits

- Tests run one at a time. The suite is not set up for TestNG parallel execution; each test
  class keeps its browser in an instance field.
- The social link tests check which site the new tab lands on, not the content of that page,
  because X and Facebook may show a login wall to automated browsers.

## Extending

- **New page:** extend `BasePage`, implement `readyLocator()`, add action methods.
- **New test:** extend `LoggedInTest` (or `BaseTest` to start on the login page), tag it with
  groups, and add the class to `src/test/resources/testng.xml`.
- **Run one class:** `./mvnw clean test -Dtest=CheckoutTest`.
- **New social icon:** add a `SocialLink` subclass and one line in the `socialLinks` data
  provider.

## Troubleshooting

- **Driver download fails behind a proxy/firewall:** Selenium Manager needs internet access on
  first run. Set `HTTPS_PROXY`, or install ChromeDriver yourself and put it on your `PATH`.
- **Surefire version:** the plugin is pinned to 3.5.x on purpose. Version 3.6.0 no longer
  supports `testng.xml` suite files.
