package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.BookingDetailEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BookingDetailRepository;
import com.proriberaapp.ribera.Infraestructure.services.BookingDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingDetailServiceImpl implements BookingDetailService {
    private final BookingDetailRepository bookingDetailRepository;
    @Override
    public Mono<BookingDetailEntity> save(BookingDetailEntity bookingDetailEntity) {
        Integer bookingId = bookingDetailEntity.getBookingId();
        Integer paymentStateId = bookingDetailEntity.getPaymentStateId();
        return bookingDetailRepository.findByBookingIdAndPaymentStateId(bookingId, paymentStateId).hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalArgumentException("Booking detail already exists"))
                        : bookingDetailRepository.save(bookingDetailEntity));
    }

    @Override
    public Flux<BookingDetailEntity> saveAll(Flux<BookingDetailEntity> bookingDetailEntity) {
        Flux<Integer> bookingIds = bookingDetailEntity.map(BookingDetailEntity::getBookingId);
        Flux<Integer> paymentStateIds = bookingDetailEntity.map(BookingDetailEntity::getPaymentStateId);
        return bookingDetailRepository.findByBookingIdAndPaymentStateId(bookingIds, paymentStateIds)
                .collectList()
                .flatMapMany(bookingDetailEntities -> bookingDetailRepository.saveAll(
                        bookingDetailEntity.filter(
                                bookingDetailEntity1 -> !bookingDetailEntities.contains(bookingDetailEntity1))
                ));
    }

    @Override
    public Mono<BookingDetailEntity> findById(String id) {
        return bookingDetailRepository.findById(Integer.parseInt(id));
    }

    @Override
    public Flux<BookingDetailEntity> findAll() {
        return bookingDetailRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return bookingDetailRepository.deleteById(Integer.parseInt(id));
    }

    @Override
    public Mono<BookingDetailEntity> update(BookingDetailEntity bookingDetailEntity) {
        return bookingDetailRepository.save(bookingDetailEntity);
    }
}
