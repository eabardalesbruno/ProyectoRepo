package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.BookingStateEntity;
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
    @Override
    public Mono<BookingStateEntity> save(BookingStateEntity bookingStateEntity) {
        return null;
    }

    @Override
    public Flux<BookingStateEntity> saveAll(Flux<BookingStateEntity> bookingStateEntity) {
        return null;
    }

    @Override
    public Mono<BookingStateEntity> findById(String id) {
        return null;
    }

    @Override
    public Flux<BookingStateEntity> findAll() {
        return null;
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return null;
    }

    @Override
    public Mono<BookingStateEntity> update(BookingStateEntity bookingStateEntity) {
        return null;
    }
}
