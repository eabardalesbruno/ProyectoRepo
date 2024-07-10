package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.CancelEntity;
import com.proriberaapp.ribera.Domain.entities.CancelPaymentEntity;
import com.proriberaapp.ribera.Domain.entities.RefuseEntity;
import com.proriberaapp.ribera.Domain.entities.RefusePaymentEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CancelPaymentRepository extends R2dbcRepository<CancelPaymentEntity, Integer> {
    @Query("SELECT * FROM cancelreason WHERE cancelreasonid != 1")
    Flux<CancelEntity> findAllWhereCancelReasonIdNotEqualToOne();
}
