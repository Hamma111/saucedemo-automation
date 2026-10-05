package com.saucedemo.tests;

import com.saucedemo.pages.CartPage;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartTest extends LoggedInTest {

    @Test(
            groups = {"smoke", "cart"},
            description = "Product can be added to the cart, removed and added again")
    public void productCanBeAddedRemovedAndReAdded() {
        inventoryPage.addToCart(BACKPACK);
        Assert.assertEquals(inventoryPage.cartCount(), 1, "Cart count after adding");
        Assert.assertTrue(inventoryPage.isInCart(BACKPACK), "Button should switch to Remove");

        inventoryPage.removeFromCart(BACKPACK);
        Assert.assertEquals(inventoryPage.cartCount(), 0, "Cart count after removing");
        Assert.assertFalse(inventoryPage.isInCart(BACKPACK), "Button should switch back to Add");

        inventoryPage.addToCart(BACKPACK);
        Assert.assertEquals(inventoryPage.cartCount(), 1, "Cart count after re-adding");

        CartPage cartPage = assertLoaded(inventoryPage.openCart());
        Assert.assertEquals(cartPage.itemNames(), List.of(BACKPACK), "Cart contents");
    }
}
