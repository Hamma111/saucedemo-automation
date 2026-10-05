package com.saucedemo.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CartPage extends BasePage {

    private static final By CHECKOUT_BUTTON = byTestId("checkout");
    private static final By ITEM_NAMES = byTestId("inventory-item-name");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected By readyLocator() {
        return CHECKOUT_BUTTON;
    }

    public List<String> itemNames() {
        return findAll(ITEM_NAMES).stream().map(WebElement::getText).toList();
    }

    public CheckoutInfoPage checkout() {
        click(CHECKOUT_BUTTON);
        return new CheckoutInfoPage(driver);
    }
}
