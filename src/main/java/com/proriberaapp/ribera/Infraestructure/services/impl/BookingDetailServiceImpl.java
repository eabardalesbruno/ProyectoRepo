package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.BookingDetailEntity;
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
    @Override
    public Mono<BookingDetailEntity> save(BookingDetailEntity bookingDetailEntity) {
        return null;
    }

    @Override
    public Flux<BookingDetailEntity> saveAll(Flux<BookingDetailEntity> bookingDetailEntity) {
        return null;
    }

    @Override
    public Mono<BookingDetailEntity> findById(String id) {
        return null;
    }

    @Override
    public Flux<BookingDetailEntity> findAll() {
        return null;
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return null;
    }

    @Override
    public Mono<BookingDetailEntity> update(BookingDetailEntity bookingDetailEntity) {
        return null;
    }
}
