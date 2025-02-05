package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.dto.CommissionDTO;
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

    @Query("SELECT * FROM commission c WHERE c.disbursementdate BETWEEN :startOfDay AND :endOfDay AND c.processed = false")
    Flux<CommissionEntity> findByDisbursementDateRange(@Param("startOfDay") Timestamp startOfDay, @Param("endOfDay") Timestamp endOfDay);


    @Query("SELECT serialNumber FROM commission ORDER BY commissionId DESC LIMIT 1")
    Mono<String> findLastSerialNumber();
    Flux<CommissionEntity> findByPromoterId(Integer promoterId);

    Mono<CommissionEntity> findByCommissionId(Integer commissionId);


    @Query("""
        SELECT 
            c.commissionid,
            c.disbursementdate,
            c.promoterid,
            p.firstname || ' ' || p.lastname AS promoter_fullname,
            c.rucnumber,
            c.commissionamount,
            c.currencytypeid,
            c.status,
            c.invoicedocument,
            c.processed,
            c.currencytypeid,
            (SELECT COUNT(*) FROM commission WHERE rucnumber IS NOT NULL) AS total_commissions
            FROM commission c
        LEFT JOIN userpromoter p ON c.promoterid = p.userpromoterid
        WHERE c.rucnumber IS NOT NULL
        ORDER BY c.disbursementdate DESC
        LIMIT :size OFFSET :offset
    """)
    Flux<CommissionDTO> findAllWithPromoter(int size, int offset);

}
