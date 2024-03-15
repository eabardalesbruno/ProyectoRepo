package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.BookingStateEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BookingStateRepository;
import com.proriberaapp.ribera.Infraestructure.services.BookingStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingStateServiceImpl implements BookingStateService {
    private final BookingStateRepository bookingStateRepository;
    @Override
    public Mono<BookingStateEntity> save(BookingStateEntity bookingStateEntity) {
        return bookingStateRepository.findByBookingStateName(bookingStateEntity.getBookingStateName()).hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalArgumentException("Booking state already exists"))
                        : bookingStateRepository.save(bookingStateEntity));
    }

    @Override
    public Flux<BookingStateEntity> saveAll(Flux<BookingStateEntity> bookingStateEntity) {
        return bookingStateRepository.findByBookingStateName(bookingStateEntity)
                .collectList()
                .flatMapMany(bookingStateEntities -> bookingStateRepository.saveAll(
                        bookingStateEntity.filter(
                                bookingStateEntity1 -> !bookingStateEntities.contains(bookingStateEntity1))
                ));
    }

    @Override
    public Mono<BookingStateEntity> findById(Integer id) {
        return bookingStateRepository.findById(id);
    }

    @Override
    public Flux<BookingStateEntity> findAll() {
        return bookingStateRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return bookingStateRepository.deleteById(id);
    }

    @Override
    public Mono<BookingStateEntity> update(BookingStateEntity bookingStateEntity) {
        return bookingStateRepository.save(bookingStateEntity);
    }
}
