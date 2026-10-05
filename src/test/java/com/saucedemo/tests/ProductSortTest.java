package com.saucedemo.tests;

import com.saucedemo.model.SortOption;
import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ProductSortTest extends LoggedInTest {

    @DataProvider(name = "nameSortOptions")
    public Object[][] nameSortOptions() {
        return new Object[][] {{SortOption.NAME_A_TO_Z}, {SortOption.NAME_Z_TO_A}};
    }

    @Test(
            groups = {"regression", "sorting"},
            dataProvider = "nameSortOptions",
            description = "Product list follows the selected name sort order")
    public void productsAreSortedByName(SortOption option) {
        // Start from a different order and require the list to change, so the check cannot
        // pass just because the option under test happens to be the page's default.
        List<String> before = List.of();
        for (SortOption other : SortOption.values()) {
            if (other != option) {
                before = inventoryPage.sortBy(other).productNames();
            }
        }

        List<String> actual = inventoryPage.sortBy(option).productNames();
        Assert.assertNotEquals(actual, before, "Choosing " + option + " did not reorder the list");

        List<String> expected = new ArrayList<>(actual);
        expected.sort(option.expectedOrder());

        Assert.assertTrue(actual.size() > 1, "Need at least two products to verify sorting");
        Assert.assertEquals(actual, expected, "Products are not sorted " + option);
    }
}
