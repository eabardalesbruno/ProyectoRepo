package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.BookingEntity;
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
    @Override
    public Mono<BookingEntity> save(BookingEntity bookingEntity) {
        return null;
    }

    @Override
    public Flux<BookingEntity> saveAll(Flux<BookingEntity> bookingEntity) {
        return null;
    }

    @Override
    public Mono<BookingEntity> findById(String id) {
        return null;
    }

    @Override
    public Flux<BookingEntity> findAll() {
        return null;
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return null;
    }

    @Override
    public Mono<BookingEntity> update(BookingEntity bookingEntity) {
        return null;
    }
}
