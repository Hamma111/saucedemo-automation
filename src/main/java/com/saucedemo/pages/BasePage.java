package com.saucedemo.pages;

import com.saucedemo.config.Config;
import com.saucedemo.utils.Log;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Parent of every page object. Holds the shared waiting/clicking/typing helpers and defines the
 * contract each page fulfils in its own way: {@link #readyLocator()}.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Config.TIMEOUT);
    }

    /** An element that is only visible once this particular page is ready to use. */
    protected abstract By readyLocator();

    /** Same call for every page; the behaviour depends on the concrete page's locator. */
    public boolean isLoaded() {
        try {
            find(readyLocator());
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public String name() {
        return getClass().getSimpleName();
    }

    protected static By byTestId(String id) {
        return By.cssSelector("[data-test='" + id + "']");
    }

    protected WebElement find(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected List<WebElement> findAll(By locator) {
        return driver.findElements(locator);
    }

    protected void click(By locator) {
        Log.step(name() + ": click " + locator);
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator, String text) {
        Log.step(name() + ": type '" + text + "' into " + locator);
        WebElement field = find(locator);
        field.clear();
        field.sendKeys(text);
    }

    protected String textOf(By locator) {
        return find(locator).getText().trim();
    }

    protected boolean isPresent(By locator) {
        return !findAll(locator).isEmpty();
    }
}
