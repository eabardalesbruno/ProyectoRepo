package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.PaymentTokenEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PaymentTokenRepository extends R2dbcRepository<PaymentTokenEntity, Integer> {

    @Query("INSERT INTO paymenttoken (paymenttoken, startdate, enddate, bookingid, paymentbookid) " +
            "VALUES (:paymentToken, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '24 hours', :bookingId, :paymentBookId)")
    Mono<PaymentTokenEntity> generateAndSaveToken(String paymentToken, Integer bookingId, Integer paymentBookId);
}
