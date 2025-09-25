package com.proriberaapp.ribera.Domain.invoice;

import org.json.JSONObject;

import reactor.core.publisher.Mono;

public interface SunatInvoice {
    Mono<InvoiceResponse> sendInvoice(InvoiceDomain invoice);

    Mono<InvoiceResponseStatus> getInvoiceStatus(String invoiceId);

    JSONObject formatJson(InvoiceDomain invoice);
}
