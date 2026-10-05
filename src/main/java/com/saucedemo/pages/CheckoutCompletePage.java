package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends BasePage {

    private static final By CONFIRMATION_HEADER = byTestId("complete-header");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected By readyLocator() {
        return CONFIRMATION_HEADER;
    }

    public String confirmationMessage() {
        return textOf(CONFIRMATION_HEADER);
    }
}
