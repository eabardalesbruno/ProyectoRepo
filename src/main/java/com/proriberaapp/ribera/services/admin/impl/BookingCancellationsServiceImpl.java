package com.proriberaapp.ribera.services.admin.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.bookingcancellations.request.BookingCancellationRequest;
import com.proriberaapp.ribera.Domain.entities.BookingCancellationsEntity;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BookingCancellationsRepository;
import com.proriberaapp.ribera.Infraestructure.repository.BookingRepository;
import com.proriberaapp.ribera.services.admin.BookingCancellationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingCancellationsServiceImpl implements BookingCancellationsService {

    private final BookingCancellationsRepository bookingCancellationsRepository;
    private final BookingRepository bookingRepository;

    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<BookingCancellationsEntity> createBookingCancellation(BookingCancellationRequest request) {
        log.info("Inicio del método createBookingCancellation");

        return insertBookingCancellationEntity(request)
                .flatMap(savedBookingCancellation -> {
                    log.info("BookingCancellationEntity creado con id {}", savedBookingCancellation.getCancellationId());
                    return bookingRepository.findByBookingId(savedBookingCancellation.getBookingId())
                            .flatMap(existingBookingEntity ->
                                    updateBookingEntity(existingBookingEntity, request))
                            .thenReturn(savedBookingCancellation)
                            .doOnError(error ->
                                    log.error("Error durante la actualizacion de entidad booking", error))
                            .doOnSuccess(success ->
                                    log.info("Actualizacion correcta de booking con id {}", savedBookingCancellation.getBookingId()));
                })
                .as(transactionalOperator::transactional)
                .doOnError(error -> log.error("Error durante creacion de entidad BookingCancellation", error))
                .doOnSuccess(success -> log.info("Fin del método createBookingRoomChange"));
    }

    private Mono<BookingCancellationsEntity> insertBookingCancellationEntity(BookingCancellationRequest request) {
        log.info("Inicio de método insertBookingCancellationEntity");

        var newBookingCancellation = BookingCancellationsEntity.builder()
                .bookingId(request.getBookingId())
                .userClientId(request.getUserClientId())
                .cancellationReason(request.getCancellationReason())
                .additionalCost(request.getAdditionalCost())
                .cancellationDate(LocalDateTime.now())
                .receptionistId(request.getReceptionistId())
                .build();
        return bookingCancellationsRepository.save(newBookingCancellation)
                .doOnSuccess(success -> log.info("Fin del método insertBookingCancellationEntity"));
    }

    private Mono<BookingEntity> updateBookingEntity(
            BookingEntity existingBookingEntity, BookingCancellationRequest request) {
        log.info("Inicio de método updateBookingEntity");

        Optional.ofNullable(request.getReceptionistId())
                .ifPresent(existingBookingEntity::setReceptionistId);
        //Validar esto cuando se debe pagar un adicional por la anulacion en donde se le agrega
        Optional.ofNullable(request.getAdditionalCost())
                .ifPresent(additionalCost -> {
                    BigDecimal currentCost = existingBookingEntity.getCostFinal()!=null
                            ? existingBookingEntity.getCostFinal()
                            :BigDecimal.ZERO;
                    BigDecimal newCost = currentCost.add(additionalCost);
                    existingBookingEntity.setCostFinal(newCost);
                });
        existingBookingEntity.setBookingStateId(4);

        return bookingRepository.save(existingBookingEntity)
                .doOnSuccess(success -> log.info("Fin del método updateBookingEntity"));
    }
}
