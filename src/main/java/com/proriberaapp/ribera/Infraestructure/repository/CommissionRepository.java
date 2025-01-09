package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.CommissionEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CommissionRepository extends R2dbcRepository <CommissionEntity, Integer> {

    Flux<CommissionEntity> findByPaymentBookId(Integer paymentBookId);
}
