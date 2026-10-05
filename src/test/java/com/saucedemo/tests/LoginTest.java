package com.saucedemo.tests;

import com.saucedemo.pages.InventoryPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(groups = {"smoke", "login"}, description = "Standard user can log in")
    public void standardUserCanLogIn() {
        assertLoaded(loginPage);

        InventoryPage inventoryPage = loginPage.loginAsStandardUser();

        assertLoaded(inventoryPage);
        Assert.assertTrue(
                inventoryPage.currentUrl().endsWith("/inventory.html"),
                "Should land on the inventory page but was " + inventoryPage.currentUrl());
        Assert.assertFalse(inventoryPage.productNames().isEmpty(), "Products should be listed");
    }
}
