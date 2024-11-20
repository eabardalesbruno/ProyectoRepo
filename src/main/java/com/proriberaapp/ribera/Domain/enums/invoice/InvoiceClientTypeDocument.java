package com.proriberaapp.ribera.Domain.enums.invoice;

public enum InvoiceClientTypeDocument {
    DNI(8, "1"),
    RUC(11, "6");

    private final int lenght;
    private final String code;

    InvoiceClientTypeDocument(int lenght, String code) {
        this.code = code;
        this.lenght = lenght;
    }

    public String getCode() {
        return code;
    }

    static public InvoiceClientTypeDocument getInvoiceClientTypeDocumentByLenght(int lenght) {
        for (InvoiceClientTypeDocument invoiceClientTypeDocument : InvoiceClientTypeDocument.values()) {
            if (invoiceClientTypeDocument.lenght == lenght) {
                return invoiceClientTypeDocument;
            }
        }
        throw new IllegalArgumentException("Invalid lenght");
    }

    static public InvoiceClientTypeDocument getInvoiceClientTypeDocumentByName(String name) {
        for (InvoiceClientTypeDocument invoiceClientTypeDocument : InvoiceClientTypeDocument.values()) {
            if (invoiceClientTypeDocument.name().equals(name)) {
                return invoiceClientTypeDocument;
            }
        }
        throw new IllegalArgumentException("Invalid name");
    }

}
