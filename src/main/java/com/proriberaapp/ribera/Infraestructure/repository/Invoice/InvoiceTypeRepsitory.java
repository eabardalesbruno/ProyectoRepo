package com.proriberaapp.ribera.Infraestructure.repository.Invoice;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proriberaapp.ribera.Domain.entities.Invoice.InvoiceTypeEntity;

import reactor.core.publisher.Mono;

public interface InvoiceTypeRepsitory extends R2dbcRepository<InvoiceTypeEntity, Integer> {
    Mono<InvoiceTypeEntity> findByName(String name);

    @Query("UPDATE invoiceType SET correlative = correlative + 1  WHERE name = :name")
    Mono<Void> addCorrelative(String name);
}
