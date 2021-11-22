package com.golightyear.core;

public enum Currency {

    EUR("Euro", "€"),
    USD("US dollar", "$");

    Currency(String displayName, String currencySymbol) {
        this.displayName = displayName;
        this.currencySymbol = currencySymbol;
    }

    String displayName;
    String currencySymbol;
}
