package com.saucedemo.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/** Checkout step two: order summary. */
public class CheckoutOverviewPage extends BasePage {

    private static final By FINISH_BUTTON = byTestId("finish");
    private static final By ITEM_NAMES = byTestId("inventory-item-name");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected By readyLocator() {
        return FINISH_BUTTON;
    }

    public List<String> itemNames() {
        return findAll(ITEM_NAMES).stream().map(WebElement::getText).toList();
    }

    public CheckoutCompletePage finish() {
        click(FINISH_BUTTON);
        return new CheckoutCompletePage(driver);
    }
}
