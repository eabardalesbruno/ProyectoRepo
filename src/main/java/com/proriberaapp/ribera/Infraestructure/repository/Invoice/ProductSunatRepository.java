package com.proriberaapp.ribera.Infraestructure.repository.Invoice;

import com.proriberaapp.ribera.Domain.invoice.ProductSunatDomain;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ProductSunatRepository extends R2dbcRepository<ProductSunatDomain, Integer> {
}
