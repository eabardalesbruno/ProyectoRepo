package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentBookRepository extends R2dbcRepository<PaymentBookEntity, Integer> {
    Flux<PaymentBookEntity> findAll();
    Flux<PaymentBookEntity> findByUserClientId(Integer userClientId);
    Mono<PaymentBookEntity> findById(Integer id);
    @Query("SELECT userclientid FROM paymentbook WHERE paymentbookid = :id")
    Mono<Integer> findUserClientIdByPaymentBookId(Integer id);

    @Query("SELECT * FROM paymentbook WHERE refusereasonid != :refuseReasonId AND pendingpay = :pendingPay LIMIT :size OFFSET :offset")
    Flux<PaymentBookEntity> findAllByRefuseReasonIdAndPendingPay(int refuseReasonId, int pendingPay, int size, int offset);

    @Query("SELECT COUNT(*) FROM paymentbook WHERE refusereasonid != :refuseReasonId AND pendingpay = :pendingPay")
    Mono<Long> countByRefuseReasonIdAndPendingPay(int refuseReasonId, int pendingPay);
}