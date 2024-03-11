package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BookingRepository;
import com.proriberaapp.ribera.Infraestructure.services.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    @Override
    public Mono<BookingEntity> save(BookingEntity bookingEntity) {
        Integer userId = bookingEntity.getBookingId();
        Integer paymentMethodId = bookingEntity.getPaymentMethodId();
        Integer bookingStateId = bookingEntity.getBookingStateId();
        return bookingRepository.findByUserIDAndPaymentMethodIdAndBookingStateId(
                userId, paymentMethodId, bookingStateId
                ).hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalArgumentException("Booking already exists"))
                        : Mono.just(bookingEntity))
                .switchIfEmpty(bookingRepository.save(bookingEntity));
    }

    @Override
    public Flux<BookingEntity> saveAll(Flux<BookingEntity> bookingEntity) {
        Flux<Integer> userIds = bookingEntity.map(BookingEntity::getBookingId);
        Flux<Integer> paymentMethodIds = bookingEntity.map(BookingEntity::getPaymentMethodId);
        Flux<Integer> bookingStateIds = bookingEntity.map(BookingEntity::getBookingStateId);
        return bookingRepository.findByUserIDAndPaymentMethodIdAndBookingStateId(
                userIds, paymentMethodIds, bookingStateIds
                )
                .collectList()
                .flatMapMany(bookingEntities -> bookingRepository.saveAll(
                        bookingEntity.filter(
                                bookingEntity1 -> !bookingEntities.contains(bookingEntity1))
                ));
    }

    @Override
    public Mono<BookingEntity> findById(String id) {
        return bookingRepository.findById(Integer.parseInt(id));
    }

    @Override
    public Flux<BookingEntity> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return bookingRepository.deleteById(Integer.parseInt(id));
    }

    @Override
    public Mono<BookingEntity> update(BookingEntity bookingEntity) {
        return bookingRepository.save(bookingEntity);
    }
}
