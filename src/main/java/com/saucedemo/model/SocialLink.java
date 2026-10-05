package com.saucedemo.model;

import java.net.URI;
import java.util.List;
import org.openqa.selenium.By;

/**
 * A social media icon in the page footer. Each network is a subclass that supplies its own
 * locator, link target and accepted landing domains, so a single test can verify any of them
 * through this common type.
 */
public abstract class SocialLink {

    /** Where the icon sits on the page. */
    public abstract By locator();

    /** The exact href the icon should carry. */
    public abstract String expectedHref();

    /** Domains the browser may land on after the network's own redirects. */
    protected abstract List<String> acceptedDomains();

    public String name() {
        return getClass().getSimpleName().replace("Link", "");
    }

    /** True when the URL belongs to one of this network's domains (or a subdomain of one). */
    public boolean isExpectedDestination(String url) {
        String host = URI.create(url).getHost();
        if (host == null) {
            return false;
        }
        String normalisedHost = host.toLowerCase();
        return acceptedDomains().stream()
                .anyMatch(domain -> normalisedHost.equals(domain)
                        || normalisedHost.endsWith("." + domain));
    }

    @Override
    public String toString() {
        return name();
    }

    protected static By footerIcon(String network) {
        return By.cssSelector("[data-test='social-" + network + "']");
    }
}
