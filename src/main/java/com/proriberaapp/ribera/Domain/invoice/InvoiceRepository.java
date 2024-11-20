package com.proriberaapp.ribera.Domain.invoice;

import java.util.List;

import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceStatus;

public interface InvoiceRepository {
    void save(InvoiceDomain invoice);

    InvoiceDomain findById(String invoiceId);

    List<InvoiceDomain> getAllInvoices();

    List<InvoiceDomain> getInvoicesByStatus(InvoiceStatus status);
}
