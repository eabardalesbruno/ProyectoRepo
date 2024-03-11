package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.PaymentMethodEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PaymentMethodRepository extends R2dbcRepository<PaymentMethodEntity, Integer> {
}
