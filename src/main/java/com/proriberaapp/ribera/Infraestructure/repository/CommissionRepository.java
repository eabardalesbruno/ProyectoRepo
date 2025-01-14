package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.CommissionEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Repository
public interface CommissionRepository extends R2dbcRepository <CommissionEntity, Integer> {

    Flux<CommissionEntity> findByPaymentBookId(Integer paymentBookId);

    @Query("SELECT SUM(commissionamount) FROM commission WHERE promoterid = :promoterId")
    Mono<BigDecimal> findTotalCommissionByPromoterId(@Param("promoterId") Integer promoterId);

    Flux<CommissionEntity> findByDisbursementDate(Timestamp date);

}
