package com.saucedemo.pages;

import com.saucedemo.config.Config;
import com.saucedemo.utils.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME = byTestId("username");
    private static final By PASSWORD = byTestId("password");
    private static final By LOGIN_BUTTON = byTestId("login-button");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected By readyLocator() {
        return LOGIN_BUTTON;
    }

    public LoginPage open() {
        Log.step("Open " + Config.BASE_URL);
        driver.get(Config.BASE_URL);
        return this;
    }

    public InventoryPage loginAs(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        click(LOGIN_BUTTON);
        return new InventoryPage(driver);
    }

    public InventoryPage loginAsStandardUser() {
        return loginAs(Config.USERNAME, Config.PASSWORD);
    }
}
