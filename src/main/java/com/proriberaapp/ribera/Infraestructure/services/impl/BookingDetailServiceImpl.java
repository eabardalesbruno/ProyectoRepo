package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.BookingDetailEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BookingDetailRepository;
import com.proriberaapp.ribera.Infraestructure.services.BookingDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingDetailServiceImpl implements BookingDetailService {
    private final BookingDetailRepository bookingDetailRepository;
    @Override
    public Mono<BookingDetailEntity> save(BookingDetailEntity bookingDetailEntity) {
        Integer bookingId = bookingDetailEntity.getBookingId();
        return bookingDetailRepository.findByBookingId(bookingDetailEntity).hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalArgumentException("Booking detail already exists"))
                        : bookingDetailRepository.save(bookingDetailEntity));
    }

    @Override
    public Flux<BookingDetailEntity> saveAll(List<BookingDetailEntity> bookingDetailEntity) {
        return bookingDetailRepository.findAllByBookingIdIn(bookingDetailEntity)
                .collectList()
                .flatMapMany(bookingDetailEntities -> bookingDetailRepository.saveAll(
                        bookingDetailEntity.stream().filter(
                                bookingDetailEntity1 -> !bookingDetailEntities.contains(bookingDetailEntity1)).toList()
                ));
    }

    @Override
    public Mono<BookingDetailEntity> findById(Integer id) {
        return bookingDetailRepository.findById(id);
    }

    @Override
    public Flux<BookingDetailEntity> findAll() {
        return bookingDetailRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return bookingDetailRepository.deleteById(id);
    }

    @Override
    public Mono<BookingDetailEntity> update(BookingDetailEntity bookingDetailEntity) {
        return bookingDetailRepository.save(bookingDetailEntity);
    }
}
