package com.proriberaapp.ribera.Domain.enums.invoice;

public enum InvoiceType {

    FACTURA(11, "6"),
    BOLETA(8, "1");

    private final int lenghtChart;
    private final String code;

    InvoiceType(int lenghtChart, String code) {
        this.lenghtChart = lenghtChart;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public int getLenghtChart() {
        return lenghtChart;
    }

    static public InvoiceType getInvoiceTypeByLenght(int lenghtChart) {
        for (InvoiceType invoiceType : InvoiceType.values()) {
            if (invoiceType.getLenghtChart() == lenghtChart) {
                return invoiceType;
            }
        }
        throw new IllegalArgumentException("Invalid lenght chart");
    }
}
