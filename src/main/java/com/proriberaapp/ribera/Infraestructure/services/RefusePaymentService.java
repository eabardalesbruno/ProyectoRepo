package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Domain.entities.RefusePaymentEntity;
import com.proriberaapp.ribera.Infraestructure.repository.RefusePaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RefusePaymentService {

    private final RefusePaymentRepository refusePaymentRepository;

    @Autowired
    public RefusePaymentService(RefusePaymentRepository refusePaymentRepository) {
        this.refusePaymentRepository = refusePaymentRepository;
    }

    public Flux<RefusePaymentEntity> getAllRefusePayments() {
        return refusePaymentRepository.findAll();
    }

    public Mono<RefusePaymentEntity> createRefusePayment(RefusePaymentEntity refusePaymentEntity) {
        return refusePaymentRepository.save(refusePaymentEntity);
    }

    public Mono<RefusePaymentEntity> updateRefusePayment(Integer id, RefusePaymentEntity refusePaymentEntity) {
        refusePaymentEntity.setRefusePaymentId(id);
        return refusePaymentRepository.save(refusePaymentEntity);
    }

    public Mono<Void> deleteRefusePayment(Integer id) {
        return refusePaymentRepository.deleteById(id);
    }
}