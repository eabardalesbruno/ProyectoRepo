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
        Integer bookingId = bookingEntity.getBookingId();
        Integer bookingStateId = bookingEntity.getBookingStateId();
        return bookingRepository.findByBookingStateId(bookingStateId
                ).hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalArgumentException("Booking already exists"))
                        : bookingRepository.save(bookingEntity));
    }

    @Override
    public Flux<BookingEntity> saveAll(Flux<BookingEntity> bookingEntity) {
        Flux<Integer> bookingId = bookingEntity.map(BookingEntity::getBookingId);
        Flux<Integer> bookingStateIds = bookingEntity.map(BookingEntity::getBookingStateId);
        return bookingRepository.findByBookingStateId(bookingStateIds)
                .collectList()
                .flatMapMany(bookingEntities -> bookingRepository.saveAll(
                        bookingEntity.filter(
                                bookingEntity1 -> !bookingEntities.contains(bookingEntity1))
                ));
    }

    @Override
    public Mono<BookingEntity> findById(Integer id) {
        return bookingRepository.findById(id);
    }

    @Override
    public Flux<BookingEntity> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return bookingRepository.deleteById(id);
    }

    @Override
    public Mono<BookingEntity> update(BookingEntity bookingEntity) {
        return bookingRepository.save(bookingEntity);
    }
}
