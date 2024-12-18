package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.CompanionsEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BookingRepository;
import com.proriberaapp.ribera.Infraestructure.repository.CompanionsRepository;
import com.proriberaapp.ribera.services.client.CompanionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CompanionServiceImpl implements CompanionsService {

    private final CompanionsRepository companionsRepository;
    private final BookingRepository bookingRepository;


    @Override
    public Flux<CompanionsEntity> getCompanionsByBookingId(Integer bookingId) {
        return companionsRepository.findByBookingId(bookingId);
    }

    @Override
    public Mono<CompanionsEntity> addCompanionBooking(CompanionsEntity companionsEntity) {
        return companionsRepository.save(companionsEntity);
    }

    @Override
    public Mono<Void> validateTotalCompanions(Integer bookingId, Flux<CompanionsEntity> companionsEntity) {
        return bookingRepository.findByBookingId(bookingId)
                .flatMap(booking -> {
                    int totalPersons = booking.getNumberAdults() +
                            booking.getNumberChildren() +
                            booking.getNumberBabies() +
                            booking.getNumberAdultsExtra() +
                            booking.getNumberAdultsMayor();

                    return companionsEntity.count()
                            .flatMap(count ->{
                                if(count != totalPersons){
                                    return Mono.error(new IllegalArgumentException(
                                            "El total de acompañantes no coincide con el total de personas en la reserva"));
                                }
                                return Mono.empty();
                            });
                });
    }
}
