package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.dto.FeedingDto;
import com.proriberaapp.ribera.Domain.entities.FeedingEntity;
import com.proriberaapp.ribera.Domain.entities.RoomOfferFeedingEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BookingRepository;
import com.proriberaapp.ribera.Infraestructure.repository.FeedingRepository;

import com.proriberaapp.ribera.Infraestructure.repository.RoomOfferFeedingRepository;
import com.proriberaapp.ribera.services.client.FeedingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FeedingServiceImpl implements FeedingService {

    private final FeedingRepository feedingRepository;
    private final BookingRepository bookingRepository;
    private final RoomOfferFeedingRepository roomOfferFeedingRepository;

    @Override
    public Flux<FeedingEntity> findAllFeeding() {
        return feedingRepository.findAll();
    }

    @Override
    public Mono<FeedingEntity> findFeedingById(Integer id) {
        return feedingRepository.findById(id);
    }

    @Override
    public Mono<FeedingEntity> saveFeeding(FeedingDto feedingDTO) {
        // Guardar el FeedingEntity
        return feedingRepository.save(feedingDTO.getFeedingEntity())
                .flatMap(savedFeeding -> {
                    // Crear entradas en la tabla intermedia para cada roomOfferId
                    return Flux.fromIterable(feedingDTO.getRoomOfferIds())
                            .flatMap(roomOfferId -> {
                                RoomOfferFeedingEntity roomOfferFeedingEntity = RoomOfferFeedingEntity.builder()
                                        .roomOfferId(roomOfferId)
                                        .feedingId(savedFeeding.getId())
                                        .build();

                                // Guardar en la tabla intermedia
                                return roomOfferFeedingRepository.save(roomOfferFeedingEntity);
                            })
                            .then(Mono.just(savedFeeding));  // Devolver el FeedingEntity guardado después de guardar todas las relaciones
                });
    }

    @Override
    public Mono<FeedingEntity> updateFeeding(FeedingDto feedingDTO) {
        // Guardar siempre el FeedingEntity
        return feedingRepository.save(feedingDTO.getFeedingEntity())
                .flatMap(updatedFeeding -> {
                    // Iterar sobre la lista de roomOfferIds
                    return Flux.fromIterable(feedingDTO.getRoomOfferIds())
                            .flatMap(roomOfferId -> {
                                // Verificar si existe una reserva asociada al roomOfferId
                                return bookingRepository.existsByRoomOfferId(roomOfferId)
                                        .flatMap(exists -> {
                                            // Si no existe reserva, actualizar la tabla intermedia
                                            if (!exists) {
                                                RoomOfferFeedingEntity roomOfferFeedingEntity = RoomOfferFeedingEntity.builder()
                                                        .roomOfferId(roomOfferId)
                                                        .feedingId(updatedFeeding.getId())
                                                        .build();
                                                return roomOfferFeedingRepository.save(roomOfferFeedingEntity);
                                            } else {
                                                return Mono.empty(); // No hacer nada si hay una reserva asociada
                                            }
                                        });
                            })
                            .then(Mono.just(updatedFeeding)); // Devolver el FeedingEntity actualizado
                });
    }

    @Override
    public Mono<Void> deleteFeeding(Integer feedingId) {
        return roomOfferFeedingRepository.findByFeedingId(feedingId)  // Obtener los roomOfferIds asociados
                .flatMap(roomOfferFeeding -> {
                    // Verificar si existe una reserva con bookingStateId = 2 para cada roomOfferId
                    return bookingRepository.existsByRoomOfferIdAndBookingStateId(roomOfferFeeding.getRoomOfferId(), 2)
                            .filter(exists -> !exists)  // Solo continuar si no existe una reserva con bookingStateId = 2
                            .then();  // Continuar si la condición se cumple
                })
                .then(feedingRepository.deleteById(feedingId));  // Si no se encontró ninguna reserva, eliminar el FeedingEntity
    }

}
