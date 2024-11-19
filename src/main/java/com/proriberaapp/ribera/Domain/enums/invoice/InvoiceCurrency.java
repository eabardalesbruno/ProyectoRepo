package com.proriberaapp.ribera.Domain.enums.invoice;

public enum InvoiceCurrency {
    PEN("PEN", "1", "S/"),
    USD("USD", "2", "$");

    private String currency;
    private String decimalPlaces;
    private String symbol;

    InvoiceCurrency(String currency, String decimalPlaces, String symbol) {
        this.currency = currency;
        this.symbol = symbol;
        this.decimalPlaces = decimalPlaces;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDecimalPlaces() {
        return decimalPlaces;
    }

    public String getSymbol() {
        return symbol;
    }

    static public InvoiceCurrency getInvoiceCurrencyByCurrency(String currency) {
        for (InvoiceCurrency invoiceCurrency : InvoiceCurrency.values()) {
            if (invoiceCurrency.getCurrency().equals(currency)) {
                return invoiceCurrency;
            }
        }
        throw new IllegalArgumentException("Invalid currency");
    }
}
