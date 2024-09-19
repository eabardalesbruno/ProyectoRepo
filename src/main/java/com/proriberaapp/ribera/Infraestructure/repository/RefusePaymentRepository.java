package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.RefuseEntity;
import com.proriberaapp.ribera.Domain.entities.RefusePaymentEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RefusePaymentRepository extends R2dbcRepository<RefusePaymentEntity, Integer> {
    @Query("SELECT * FROM refusereason WHERE refusereasonid != 1")
    Flux<RefuseEntity> findAllWhereRefuseReasonIdNotEqualToOne();

    Flux<RefusePaymentEntity> findAllByPaymentBookId(Integer paymentBookId);
}