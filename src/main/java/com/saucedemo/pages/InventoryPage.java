package com.saucedemo.pages;

import com.saucedemo.model.SocialLink;
import com.saucedemo.model.SortOption;
import com.saucedemo.utils.Log;
import java.util.List;
import java.util.Locale;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class InventoryPage extends BasePage {

    private static final By PRODUCT_LIST = byTestId("inventory-list");
    private static final By PRODUCT_NAMES = byTestId("inventory-item-name");
    private static final By SORT_DROPDOWN = byTestId("product-sort-container");
    private static final By CART_BADGE = byTestId("shopping-cart-badge");
    private static final By CART_LINK = byTestId("shopping-cart-link");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected By readyLocator() {
        return PRODUCT_LIST;
    }

    public InventoryPage sortBy(SortOption option) {
        Log.step(name() + ": sort by " + option);
        new Select(find(SORT_DROPDOWN)).selectByValue(option.value());
        return this;
    }

    public List<String> productNames() {
        return findAll(PRODUCT_NAMES).stream().map(WebElement::getText).toList();
    }

    public InventoryPage addToCart(String productName) {
        click(byTestId("add-to-cart-" + slug(productName)));
        return this;
    }

    public InventoryPage removeFromCart(String productName) {
        click(byTestId("remove-" + slug(productName)));
        return this;
    }

    /** True when the product's button reads "Remove", i.e. the product is in the cart. */
    public boolean isInCart(String productName) {
        return isPresent(byTestId("remove-" + slug(productName)));
    }

    /** Number shown on the cart icon; 0 when the badge is not displayed. */
    public int cartCount() {
        return isPresent(CART_BADGE) ? Integer.parseInt(textOf(CART_BADGE)) : 0;
    }

    public CartPage openCart() {
        click(CART_LINK);
        return new CartPage(driver);
    }

    public String socialLinkTarget(SocialLink link) {
        return find(link.locator()).getAttribute("href");
    }

    public void clickSocialLink(SocialLink link) {
        click(link.locator());
    }

    /** "Sauce Labs Backpack" -> "sauce-labs-backpack", as used in the button ids. */
    private static String slug(String productName) {
        return productName.toLowerCase(Locale.ROOT).replace(' ', '-');
    }
}
