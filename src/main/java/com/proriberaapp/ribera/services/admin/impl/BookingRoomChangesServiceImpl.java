package com.proriberaapp.ribera.services.admin.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.bookingroomchanges.request.BookingRoomChangesRequest;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Domain.entities.BookingRoomChangesEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BookingRepository;
import com.proriberaapp.ribera.Infraestructure.repository.BookingRoomChangesRepository;
import com.proriberaapp.ribera.services.admin.BookingRoomChangesService;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.utils.emails.BaseEmailReserve;
import com.proriberaapp.ribera.utils.emails.NewChangeRoomBookingTemplateEmail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingRoomChangesServiceImpl implements BookingRoomChangesService {

    private final BookingRoomChangesRepository bookingRoomChangesRepository;
    private final BookingRepository bookingRepository;

    private final EmailService emailService;

    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<BookingRoomChangesEntity> createBookingRoomChangeAndUpdateBooking(BookingRoomChangesRequest request) {
        log.info("Inicio de metodo createBookingRoomChangeAndUpdateBooking");

        return insertBookingRoomChangeEntity(request)
                .flatMap(savedChange -> {
                    log.info("BookingRoomChangeEntity creado con id {}", savedChange.getRoomChangeId());
                    return bookingRepository.findByBookingId(savedChange.getBookingId())
                            .flatMap(existingBookingEntity ->
                                    updateBookingEntity(existingBookingEntity, request)
                                            .flatMap(updatedBooking -> sendRoomChangeEmail(request, savedChange.getRoomChangeId())
                                            )
                            )
                            .thenReturn(savedChange)
                            .doOnError(error ->
                                    log.error("Error durante la actualizacion de entidad booking", error))
                            .doOnSuccess(success ->
                                    log.info("Actualizacion correcta de booking con id {}", savedChange.getBookingId()));
                })
                .as(transactionalOperator::transactional)
                .doOnError(error -> log.error("Error durante creacion de entidad BookingRoomChange", error))
                .doOnSuccess(success -> log.info("Fin del metodo createBookingRoomChange"));

    }

    private Mono<BookingRoomChangesEntity> insertBookingRoomChangeEntity(BookingRoomChangesRequest request) {
        log.info("Inicio de metodo insertBookingRoomChangeEntity");

        var newBookingRoomChange = BookingRoomChangesEntity.builder()
                .bookingId(request.getBookingId())
                .userClientId(request.getUserClientId())
                .changeReason(request.getChangeReason())
                .oldRoomOfferId(request.getOldRoomOfferId())
                .newRoomOfferId(request.getNewRoomOfferId())
                .additionalCost(request.getAdditionalCost())
                .changeDate(LocalDateTime.now())
                .receptionistId(request.getReceptionistId())
                .build();
        return bookingRoomChangesRepository.save(newBookingRoomChange)
                .doOnSuccess(success -> log.info("Fin del metodo insertBookingRoomChangeEntity"));
    }

    private Mono<BookingEntity> updateBookingEntity(
            BookingEntity existingBookingEntity, BookingRoomChangesRequest request) {
        log.info("Inicio de metodo updateBookingEntity");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Optional.ofNullable(request.getNewRoomOfferId())
                .ifPresent(existingBookingEntity::setRoomOfferId);
        Optional.ofNullable(request.getReceptionistId())
                .ifPresent(existingBookingEntity::setReceptionistId);
        Optional.ofNullable(request.getAdditionalService())
                .ifPresent(existingBookingEntity::setAdditionalServices);
        Optional.ofNullable(request.getAdditionalCost())
                .ifPresent(additionalCost -> {
                    BigDecimal currentCost = existingBookingEntity.getCostFinal()!=null
                            ? existingBookingEntity.getCostFinal()
                            :BigDecimal.ZERO;
                    BigDecimal newCost = currentCost.add(additionalCost);
                    existingBookingEntity.setCostFinal(newCost);
                });
        Optional.ofNullable(request.getCheckIn())
                .ifPresent(checkIn -> {
                    try {
                        LocalTime existingTimeCheckIn = existingBookingEntity.getDayBookingInit().toLocalDateTime().toLocalTime();
                        LocalDate newDate = LocalDate.parse(checkIn, dateFormatter);
                        LocalDateTime newDateTime = LocalDateTime.of(newDate, existingTimeCheckIn);
                        existingBookingEntity.setDayBookingInit(Timestamp.valueOf(newDateTime));
                    } catch (Exception e) {
                        log.error("Error al parsear checkIn: " + checkIn, e);
                    }
                });
        Optional.ofNullable(request.getCheckOut())
                .ifPresent(checkOut -> {
                    try {
                        LocalTime existingTimeCheckOut = existingBookingEntity.getDayBookingEnd().toLocalDateTime().toLocalTime();
                        LocalDate newDate = LocalDate.parse(checkOut, dateFormatter);
                        LocalDateTime newDateTime = LocalDateTime.of(newDate, existingTimeCheckOut);
                        existingBookingEntity.setDayBookingEnd(Timestamp.valueOf(newDateTime));
                    } catch (Exception e) {
                        log.error("Error al parsear checkIn: " + checkOut, e);
                    }
                });

        return bookingRepository.save(existingBookingEntity)
                .doOnSuccess(success -> log.info("Fin del metodo updateBookingEntity"));
    }

    private Mono<Void> sendRoomChangeEmail(BookingRoomChangesRequest request, Integer roomChangeId) {
        log.info("Inicio de método sendRoomChangeEmail");
        return bookingRoomChangesRepository.getDataForEmailRoomChange(request.getBookingId(), roomChangeId)
                .flatMap(bookingRoomChangeEmailResponseDto -> {
                    var templatechangeRoomEmail = NewChangeRoomBookingTemplateEmail
                            .builder()
                            .imgUrlNew(bookingRoomChangeEmailResponseDto.getImgurlnew())
                            .clientName(bookingRoomChangeEmailResponseDto.getClientname())
                            .roomNameOld(bookingRoomChangeEmailResponseDto.getRoomnameold())
                            .roomNameNew(bookingRoomChangeEmailResponseDto.getRoomnamenew())
                            .bookingId(bookingRoomChangeEmailResponseDto.getBookingid())
                            .checkIn(bookingRoomChangeEmailResponseDto.getCheckin())
                            .checkOut(bookingRoomChangeEmailResponseDto.getCheckout())
                            .approximateArrival("10:00 A.M")
                            .totalNights(bookingRoomChangeEmailResponseDto.getTotalnights())
                            .totalPeople(bookingRoomChangeEmailResponseDto.getTotalpeople())
                            .location("Km 29.5 Carretera Cieneguilla Mz B. Lt. 72 OTR. Predio Rustico Etapa III, Cercado de Lima 15593")
                            .build();

                    BaseEmailReserve emailReserve = new BaseEmailReserve();
                    emailReserve.addEmailHandler(templatechangeRoomEmail);
                    String emailBody = emailReserve.execute();

                    return emailService.sendEmail(bookingRoomChangeEmailResponseDto.getClientemail(),
                                    "Tu reserva ha sido cambiada", emailBody)
                            .doOnSuccess(s -> log.info("Correo de cambio de habitacion enviado para la reserva {}",
                                    bookingRoomChangeEmailResponseDto.getBookingid()))
                            .onErrorResume(e -> {
                                log.error("Error al enviar el correo de cambio de habitacion para la reserva {}: {}",
                                        bookingRoomChangeEmailResponseDto.getBookingid(), e.getMessage());
                                return Mono.empty();
                            });
                });
    }
}
