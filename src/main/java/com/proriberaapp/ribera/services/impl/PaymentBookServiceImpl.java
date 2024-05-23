package com.proriberaapp.ribera.services.impl;

import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentBookRepository;
import com.proriberaapp.ribera.services.PaymentBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PaymentBookServiceImpl implements PaymentBookService {
    private final PaymentBookRepository paymentBookRepository;

    @Autowired
    public PaymentBookServiceImpl(PaymentBookRepository paymentBookRepository) {
        this.paymentBookRepository = paymentBookRepository;
    }

    @Override
    public Mono<PaymentBookEntity> createPaymentBook(PaymentBookEntity paymentBook) {
        return paymentBookRepository.save(paymentBook);
    }

    @Override
    public Mono<PaymentBookEntity> updatePaymentBook(Integer id, PaymentBookEntity paymentBook) {
        return paymentBookRepository.findById(id)
                .flatMap(existingBook -> {
                    existingBook.setBookingId(paymentBook.getBookingId());
                    existingBook.setUserClientId(paymentBook.getUserClientId());
                    existingBook.setRefuseReasonId(paymentBook.getRefuseReasonId());
                    existingBook.setPaymentMethodId(paymentBook.getPaymentMethodId());
                    existingBook.setPaymentStateId(paymentBook.getPaymentStateId());
                    existingBook.setPaymentTypeId(paymentBook.getPaymentTypeId());
                    existingBook.setPaymentSubTypeId(paymentBook.getPaymentSubTypeId());
                    existingBook.setCurrencyTypeId(paymentBook.getCurrencyTypeId());
                    existingBook.setAmount(paymentBook.getAmount());
                    existingBook.setDescription(paymentBook.getDescription());
                    existingBook.setPaymentDate(paymentBook.getPaymentDate());
                    existingBook.setOperationCode(paymentBook.getOperationCode());
                    existingBook.setNote(paymentBook.getNote());
                    existingBook.setTotalCost(paymentBook.getTotalCost());
                    existingBook.setImageVoucher(paymentBook.getImageVoucher());
                    existingBook.setTotalPoints(paymentBook.getTotalPoints());
                    existingBook.setPaymentComplete(paymentBook.getPaymentComplete());
                    return paymentBookRepository.save(existingBook);
                });
    }

    @Override
    public Mono<PaymentBookEntity> getPaymentBookById(Integer id) {
        return paymentBookRepository.findById(id);
    }

    @Override
    public Flux<PaymentBookEntity> getAllPaymentBooks() {
        return paymentBookRepository.findAll();
    }

    @Override
    public Flux<PaymentBookEntity> getPaymentBooksByUserClientId(Integer userClientId) {
        return paymentBookRepository.findByUserClientId(userClientId);
    }

    @Override
    public Flux<PaymentBookEntity> getPaymentBooksByClientTypeId(Integer clientTypeId) {
        return paymentBookRepository.findByClientTypeId(clientTypeId);
    }

    @Override
    public Mono<Void> deletePaymentBook(Integer id) {
        return paymentBookRepository.deleteById(id);
    }
}
