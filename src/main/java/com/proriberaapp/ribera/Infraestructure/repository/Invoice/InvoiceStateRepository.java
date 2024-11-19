package com.proriberaapp.ribera.Infraestructure.repository.Invoice;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proriberaapp.ribera.Domain.entities.Invoice.InvoiceStatusEntity;

import reactor.core.publisher.Mono;

public interface InvoiceStateRepository extends R2dbcRepository<InvoiceStatusEntity, Integer> {
    Mono<InvoiceStatusEntity> findByName(String name);
}
