package com.saucedemo.tests;

import com.saucedemo.driver.DriverFactory;
import com.saucedemo.pages.BasePage;
import com.saucedemo.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/** Gives every test a fresh browser sitting on the login page, and closes it afterwards. */
public abstract class BaseTest {

    protected WebDriver driver;
    protected LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void startBrowser() {
        driver = DriverFactory.createChrome();
        loginPage = new LoginPage(driver).open();
    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    /** Works for any page object: each page decides for itself what "loaded" means. */
    protected static <T extends BasePage> T assertLoaded(T page) {
        Assert.assertTrue(page.isLoaded(), page.name() + " should be displayed");
        return page;
    }
}
