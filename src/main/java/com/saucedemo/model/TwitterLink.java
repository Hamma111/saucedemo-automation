package com.saucedemo.model;

import java.util.List;
import org.openqa.selenium.By;

/** Twitter is now X; the site labels the icon "X" and links straight to x.com. */
public class TwitterLink extends SocialLink {

    @Override
    public By locator() {
        return footerIcon("x");
    }

    @Override
    public String expectedHref() {
        return "https://x.com/saucelabs";
    }

    @Override
    protected List<String> acceptedDomains() {
        return List.of("x.com", "twitter.com");
    }
}
