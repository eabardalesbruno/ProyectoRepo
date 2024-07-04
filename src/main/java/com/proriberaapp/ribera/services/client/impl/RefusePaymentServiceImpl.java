package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.RefuseEntity;
import com.proriberaapp.ribera.Domain.entities.RefusePaymentEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentBookRepository;
import com.proriberaapp.ribera.Infraestructure.repository.RefusePaymentRepository;
import com.proriberaapp.ribera.services.client.RefusePaymentService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RefusePaymentServiceImpl implements RefusePaymentService {

    private final RefusePaymentRepository refusePaymentRepository;
    private final PaymentBookRepository paymentBookRepository;

    public RefusePaymentServiceImpl(RefusePaymentRepository refusePaymentRepository, PaymentBookRepository paymentBookRepository) {
        this.refusePaymentRepository = refusePaymentRepository;
        this.paymentBookRepository = paymentBookRepository;
    }

    @Override
    public Flux<RefusePaymentEntity> getAllRefusePayments() {
        return refusePaymentRepository.findAll();
    }

    @Override
    public Flux<RefuseEntity> getAllRefuseReason() {
        return refusePaymentRepository.findAllWhereRefuseReasonIdNotEqualToOne();
    }

    @Override
    public Mono<RefusePaymentEntity> getRefusePaymentById(Integer id) {
        return refusePaymentRepository.findById(id);
    }

    /* ANTES
    @Override
    public Mono<RefusePaymentEntity> saveRefusePayment(RefusePaymentEntity refusePayment) {
        return refusePaymentRepository.save(refusePayment);
    }
     */

    @Override
    public Mono<RefusePaymentEntity> saveRefusePayment(RefusePaymentEntity refusePayment) {
        return refusePaymentRepository.save(refusePayment)
                .flatMap(savedRefusePayment -> {
                    return paymentBookRepository.findById(savedRefusePayment.getPaymentBookId())
                            .flatMap(paymentBook -> {
                                paymentBook.setRefuseReasonId(savedRefusePayment.getRefuseReasonId());
                                return paymentBookRepository.save(paymentBook);
                            })
                            .then(Mono.just(savedRefusePayment));
                });
    }

    @Override
    public Mono<Void> deleteRefusePayment(Integer id) {
        return refusePaymentRepository.deleteById(id);
    }
}