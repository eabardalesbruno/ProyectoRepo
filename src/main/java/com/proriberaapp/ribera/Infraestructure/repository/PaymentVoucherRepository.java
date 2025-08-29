package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.PaymentVoucherEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

public interface PaymentVoucherRepository extends R2dbcRepository<PaymentVoucherEntity, Integer> {
    @Query("SELECT * FROM paymentvoucher WHERE paymentbookid = :paymentBookId")
    Flux<PaymentVoucherEntity> findAllByPaymentBookId(@Param("paymentBookId") Integer paymentBookId);
}