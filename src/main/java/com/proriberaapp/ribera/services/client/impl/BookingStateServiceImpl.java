package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.BookingStateEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BookingStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingStateServiceImpl implements com.proriberaapp.ribera.services.BookingStateService {
    private final BookingStateRepository bookingStateRepository;
    @Override
    public Mono<BookingStateEntity> save(BookingStateEntity bookingStateEntity) {
        return bookingStateRepository.findByBookingStateName(bookingStateEntity.getBookingStateName()).hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalArgumentException("Booking state already exists"))
                        : bookingStateRepository.save(bookingStateEntity));
    }

    @Override
    public Flux<BookingStateEntity> saveAll(List<BookingStateEntity> bookingStateEntity) {
        return bookingStateRepository.findAllByBookingStateNameIn(bookingStateEntity)
                .collectList()
                .flatMapMany(bookingStateEntities -> bookingStateRepository.saveAll(
                        bookingStateEntity.stream().filter(
                                bookingStateEntity1 -> !bookingStateEntities.contains(bookingStateEntity1)).toList()
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
