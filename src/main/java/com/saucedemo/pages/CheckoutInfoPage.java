package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Checkout step one: the customer's name and postal code. */
public class CheckoutInfoPage extends BasePage {

    private static final By FIRST_NAME = byTestId("firstName");
    private static final By LAST_NAME = byTestId("lastName");
    private static final By POSTAL_CODE = byTestId("postalCode");
    private static final By CONTINUE_BUTTON = byTestId("continue");
    private static final By ERROR_MESSAGE = byTestId("error");

    public CheckoutInfoPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected By readyLocator() {
        return CONTINUE_BUTTON;
    }

    public CheckoutInfoPage enterDetails(String firstName, String lastName, String postalCode) {
        type(FIRST_NAME, firstName);
        type(LAST_NAME, lastName);
        type(POSTAL_CODE, postalCode);
        return this;
    }

    public String firstName() {
        return valueOf(FIRST_NAME);
    }

    public String lastName() {
        return valueOf(LAST_NAME);
    }

    public String postalCode() {
        return valueOf(POSTAL_CODE);
    }

    /** Use when the details are valid and the overview page is expected next. */
    public CheckoutOverviewPage continueToOverview() {
        click(CONTINUE_BUTTON);
        return new CheckoutOverviewPage(driver);
    }

    /** Use when the details are invalid and the form is expected to stay put with an error. */
    public CheckoutInfoPage continueExpectingError() {
        click(CONTINUE_BUTTON);
        return this;
    }

    public String errorMessage() {
        return textOf(ERROR_MESSAGE);
    }
}
