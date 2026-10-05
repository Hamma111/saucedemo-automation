package com.saucedemo.model;

import java.util.Comparator;

/** The name-based entries of the product sort dropdown, with the order each one should produce. */
public enum SortOption {

    NAME_A_TO_Z("az", String.CASE_INSENSITIVE_ORDER),
    NAME_Z_TO_A("za", String.CASE_INSENSITIVE_ORDER.reversed());

    private final String value;
    private final Comparator<String> expectedOrder;

    SortOption(String value, Comparator<String> expectedOrder) {
        this.value = value;
        this.expectedOrder = expectedOrder;
    }

    /** The option's value attribute in the dropdown. */
    public String value() {
        return value;
    }

    public Comparator<String> expectedOrder() {
        return expectedOrder;
    }
}
