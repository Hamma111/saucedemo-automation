package com.saucedemo.model;

import java.util.List;
import org.openqa.selenium.By;

public class FacebookLink extends SocialLink {

    @Override
    public By locator() {
        return footerIcon("facebook");
    }

    @Override
    public String expectedHref() {
        return "https://www.facebook.com/saucelabs";
    }

    @Override
    protected List<String> acceptedDomains() {
        return List.of("facebook.com");
    }
}
