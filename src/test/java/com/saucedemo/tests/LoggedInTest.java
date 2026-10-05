package com.saucedemo.tests;

import com.saucedemo.pages.InventoryPage;
import org.testng.annotations.BeforeMethod;

/** Base for tests that start on the product list, already signed in as the standard user. */
public abstract class LoggedInTest extends BaseTest {

    protected static final String BACKPACK = "Sauce Labs Backpack";

    protected InventoryPage inventoryPage;

    @BeforeMethod(alwaysRun = true)
    public void login() {
        inventoryPage = assertLoaded(loginPage.loginAsStandardUser());
    }
}
