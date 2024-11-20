package com.proriberaapp.ribera.Domain.enums.invoice;

public enum InvoiceSerie {
    F("FACTURA"),
    B("BOLETA");

    private final String name;

    InvoiceSerie(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    static public InvoiceSerie getInvoiceSerieByName(String name) {
        for (InvoiceSerie invoiceSerie : InvoiceSerie.values()) {
            if (invoiceSerie.getName().equals(name)) {
                return invoiceSerie;
            }
        }
        throw new IllegalArgumentException("Invalid serie");
    }

}
