package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.client.dto.request.ExchangeHistoryRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.HistoricalExchangeResponse;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Domain.entities.ExchangeHistoryEntity;
import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BookingRepository;
import com.proriberaapp.ribera.Infraestructure.repository.ExchangeHistoryRepository;
import com.proriberaapp.ribera.Infraestructure.repository.RoomRepository;
import com.proriberaapp.ribera.services.client.ExchangeHistoryService;
import com.proriberaapp.ribera.utils.constants.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeHistoryServiceImpl implements ExchangeHistoryService {

    private final ExchangeHistoryRepository exchangeHistoryRepository;
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<HistoricalExchangeResponse> findByUserId(
            Integer userId, String startDate, String endDate, String exchangeType, String serviceType, Integer size, Integer page) {

        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        Integer offset = page * size;

        Mono<Integer> totalMono = exchangeHistoryRepository.countByUserIdAndFilters(
                userId, startDate, endDate, exchangeType, serviceType);
        Mono<List<ExchangeHistoryEntity>> dataMono = exchangeHistoryRepository.findByUserIdAndFilters(
                        userId, startDate, endDate, exchangeType, serviceType, size, offset)
                .collectList();

        Integer finalSize = size;
        Integer finalPage = page;
        return Mono.zip(totalMono, dataMono)
                .map(tuple -> {
                    Integer total = tuple.getT1();
                    List<ExchangeHistoryEntity> data = tuple.getT2();
                    boolean result = !data.isEmpty();
                    return HistoricalExchangeResponse.builder()
                            .result(result)
                            .total(total)
                            .data(data)
                            .size(finalSize)
                            .page(finalPage)
                            .build();
                })
                .switchIfEmpty(Mono.just(HistoricalExchangeResponse.builder()
                        .result(false)
                        .total(0)
                        .data(List.of())
                        .size(size)
                        .page(page)
                        .build()));
    }

    @Override
    public Mono<HistoricalExchangeResponse> findByUsername(
            String username, String startDate, String endDate, String exchangeType, String serviceType, Integer size, Integer page) {

        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        int offset = page * size;

        Mono<Integer> totalMono = exchangeHistoryRepository.countByUsernameAndFilters(
                username, startDate, endDate, exchangeType, serviceType);
        Mono<List<ExchangeHistoryEntity>> dataMono = exchangeHistoryRepository.findByUsernameAndFilters(
                        username, startDate, endDate, exchangeType, serviceType, size, offset)
                .collectList();

        Integer finalSize = size;
        Integer finalPage = page;
        return Mono.zip(totalMono, dataMono)
                .map(tuple -> {
                    Integer total = tuple.getT1();
                    List<ExchangeHistoryEntity> data = tuple.getT2();
                    boolean result = !data.isEmpty();
                    return HistoricalExchangeResponse.builder()
                            .result(result)
                            .total(total)
                            .data(data)
                            .size(finalSize)
                            .page(finalPage)
                            .build();
                })
                .switchIfEmpty(Mono.just(HistoricalExchangeResponse.builder()
                        .result(false)
                        .total(0)
                        .data(List.of())
                        .size(size)
                        .page(page)
                        .build()));
    }

    @Override
    public Mono<ExchangeHistoryEntity> createExchangeHistory(ExchangeHistoryRequest request) {
        return bookingRepository.findByBookingId(request.getBookingId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("No se encontró la reserva con ID: " + request.getBookingId())))
                .flatMap(bookingEntity -> {
                    String finalUsername = (request.getUsername() == null || request.getUsername().isEmpty())
                            ? null
                            : request.getUsername();

                    Integer roomId = bookingEntity.getRoomOfferId();

                    if (roomId == null) {
                        return Mono.error(new IllegalArgumentException("El ID de la habitación no se encontró en la reserva."));
                    }

                    Mono<RoomEntity> roomMono = roomRepository.findById(roomId)
                            .switchIfEmpty(Mono.error(new IllegalArgumentException("No se encontró la habitación con ID: " + roomId)));

                    return Mono.zip(Mono.just(bookingEntity), roomMono)
                            .map(tuple -> {
                                RoomEntity roomEntity = tuple.getT2();

                                String finalDescription = String.format("%s con Número de habitacion igual a %s",
                                        roomEntity.getRoomName(),
                                        roomEntity.getRoomNumber());

                                return ExchangeHistoryEntity.builder()
                                        .userId(request.getUserId())
                                        .username(finalUsername)
                                        .exchangeDate(request.getExchangeDate())
                                        .exchangeType(Constants.EXCHANGE_TYPE)
                                        .service(Constants.SERVICE_TYPE)
                                        .description(finalDescription)
                                        .checkInDate(request.getCheckInDate())
                                        .checkOutDate(request.getCheckOutDate())
                                        .usdRewards(request.getUsdRewards()*(-1))
                                        .status(Constants.ACTIVE)
                                        .createdAt(LocalDateTime.now())
                                        .build();
                            });
                })
                .flatMap(exchangeHistoryEntity -> exchangeHistoryRepository.save(exchangeHistoryEntity))
                .as(transactionalOperator::transactional)
                .doOnError(e -> System.err.println("Error creating exchange history: " + e.getMessage()))
                .doOnSuccess(v -> System.out.println("Exchange history created successfully: " + v.getId()));
    }
}
