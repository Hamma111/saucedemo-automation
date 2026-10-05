package com.saucedemo.tests;

import com.saucedemo.pages.CheckoutCompletePage;
import com.saucedemo.pages.CheckoutInfoPage;
import com.saucedemo.pages.CheckoutOverviewPage;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CheckoutTest extends LoggedInTest {

    private static final String FIRST_NAME = "ABC";
    private static final String LAST_NAME = "DEF";
    private static final String ZIP = "123456";

    private CheckoutInfoPage checkoutInfoPage;

    @BeforeMethod(alwaysRun = true)
    public void startCheckoutWithOneProduct() {
        checkoutInfoPage = assertLoaded(inventoryPage.addToCart(BACKPACK).openCart().checkout());
    }

    @Test(
            groups = {"smoke", "checkout"},
            description = "Order can be completed with valid customer details")
    public void orderCanBeCompleted() {
        CheckoutOverviewPage overviewPage = assertLoaded(
                checkoutInfoPage.enterDetails(FIRST_NAME, LAST_NAME, ZIP).continueToOverview());
        Assert.assertEquals(overviewPage.itemNames(), List.of(BACKPACK), "Order summary items");

        CheckoutCompletePage completePage = assertLoaded(overviewPage.finish());
        Assert.assertEquals(completePage.confirmationMessage(), "Thank you for your order!");
    }

    @Test(
            groups = {"regression", "checkout", "negative"},
            description = "Checkout is blocked with an error when first name is blank")
    public void blankFirstNameShowsError() {
        checkoutInfoPage.enterDetails("", LAST_NAME, ZIP).continueExpectingError();

        Assert.assertEquals(checkoutInfoPage.errorMessage(), "Error: First Name is required");
        Assert.assertTrue(
                driver.getCurrentUrl().endsWith("/checkout-step-one.html"),
                "Should stay on the customer details step but was " + driver.getCurrentUrl());
    }
}
