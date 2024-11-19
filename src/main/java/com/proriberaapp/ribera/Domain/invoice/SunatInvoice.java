package com.proriberaapp.ribera.Domain.invoice;

import reactor.core.publisher.Mono;

public interface SunatInvoice {
    Mono<InvoiceResponse> sendInvoice(InvoiceDomain invoice, CompanyDomain company);

    Mono<InvoiceResponseStatus> getInvoiceStatus(String invoiceId);
}
