package com.proriberaapp.ribera.Infraestructure.repository.Invoice;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import com.proriberaapp.ribera.Domain.entities.Invoice.InvoiceItemEntity;

public interface InvoiceItemRepository extends R2dbcRepository<InvoiceItemEntity, Integer> {

}
