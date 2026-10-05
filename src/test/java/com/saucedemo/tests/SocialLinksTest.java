package com.saucedemo.tests;

import com.saucedemo.model.FacebookLink;
import com.saucedemo.model.SocialLink;
import com.saucedemo.model.TwitterLink;
import com.saucedemo.utils.BrowserUtils;
import com.saucedemo.utils.Log;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SocialLinksTest extends LoggedInTest {

    @DataProvider(name = "socialLinks")
    public Object[][] socialLinks() {
        return new Object[][] {{new TwitterLink()}, {new FacebookLink()}};
    }

    /**
     * Polymorphism in action: the test only knows the abstract SocialLink type, and each
     * subclass supplies its own locator, href and accepted destination.
     */
    @Test(
            groups = {"regression", "social"},
            dataProvider = "socialLinks",
            description = "Footer social icon opens the correct link in a new tab")
    public void socialIconOpensCorrectLink(SocialLink link) {
        Assert.assertEquals(
                inventoryPage.socialLinkTarget(link),
                link.expectedHref(),
                link.name() + " icon href");

        String originalTab = driver.getWindowHandle();
        String openedUrl =
                BrowserUtils.openNewTabAndGetUrl(driver, () -> inventoryPage.clickSocialLink(link));
        Log.step(link.name() + " icon opened " + openedUrl);

        Assert.assertTrue(
                link.isExpectedDestination(openedUrl),
                link.name() + " icon opened an unexpected page: " + openedUrl);

        BrowserUtils.closeTabAndReturn(driver, originalTab);
        assertLoaded(inventoryPage);
    }
}
