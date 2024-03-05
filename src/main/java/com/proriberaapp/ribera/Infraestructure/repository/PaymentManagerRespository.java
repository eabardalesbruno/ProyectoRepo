package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.PaymentStateEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PaymentManagerRespository extends R2dbcRepository<PaymentStateEntity, Integer> {

}
