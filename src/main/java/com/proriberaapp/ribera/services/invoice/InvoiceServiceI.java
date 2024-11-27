package com.proriberaapp.ribera.services.invoice;

import com.proriberaapp.ribera.Domain.invoice.InvoiceDomain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InvoiceServiceI {
    Mono<Void> save(InvoiceDomain invoice);

    Mono<InvoiceDomain> findById(String id);

    Flux<InvoiceDomain> finAll();

    Flux<InvoiceDomain> findByClientId(String clientId);
}
