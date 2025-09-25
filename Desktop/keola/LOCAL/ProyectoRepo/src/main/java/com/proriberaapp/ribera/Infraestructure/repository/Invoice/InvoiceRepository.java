package com.proriberaapp.ribera.Infraestructure.repository.Invoice;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proriberaapp.ribera.Domain.entities.Invoice.InvoiceEntity;

public interface InvoiceRepository extends R2dbcRepository<InvoiceEntity, String> {

}
