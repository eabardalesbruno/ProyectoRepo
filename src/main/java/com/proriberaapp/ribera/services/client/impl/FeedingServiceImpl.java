package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.dto.FeedingDto;
import com.proriberaapp.ribera.Domain.entities.FeedingEntity;
import com.proriberaapp.ribera.Domain.entities.FeedingTypeFeedingGroupAndFeedingEntity;
import com.proriberaapp.ribera.Domain.entities.RoomOfferFeedingEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BookingRepository;
import com.proriberaapp.ribera.Infraestructure.repository.FeedingRepository;
import com.proriberaapp.ribera.Infraestructure.repository.FeedingTypeFamilyGroupAndFeedingRepository;
import com.proriberaapp.ribera.Infraestructure.repository.RoomOfferFeedingRepository;
import com.proriberaapp.ribera.services.client.FeedingService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FeedingServiceImpl implements FeedingService {

    private final FeedingRepository feedingRepository;
    private final BookingRepository bookingRepository;
    private final RoomOfferFeedingRepository roomOfferFeedingRepository;
    private final FeedingTypeFamilyGroupAndFeedingRepository feedingTypeFamilyGroupAndFeedingRepository;

    @Override
    public Flux<FeedingEntity> findAllFeeding() {
        return feedingRepository.findAllState(1);
    }

    @Override
    public Mono<FeedingEntity> findFeedingById(Integer id) {
        return feedingRepository.findById(id);
    }

    @Override
    public Mono<FeedingEntity> saveFeeding(FeedingDto feedingDTO) {
        return feedingRepository.save(feedingDTO.getFeedingEntity())
                .flatMap(savedFeeding -> {
                    List<FeedingTypeFeedingGroupAndFeedingEntity> entities = feedingDTO
                            .getItems()
                            .stream().map(d -> {
                                FeedingTypeFeedingGroupAndFeedingEntity feedingTypeFeedingGroupAndFeedingEntity = FeedingTypeFeedingGroupAndFeedingEntity
                                        .builder()
                                        .idfamilygroup(d.getFamilyGroupId())
                                        .idfeedingtype(d.getFeedingTypeId())
                                        .value(d.getValue())
                                        .idfeeding(
                                                savedFeeding.getId())
                                        .build();
                                return feedingTypeFeedingGroupAndFeedingEntity;
                            }).collect(Collectors.toList());
                    /*
                     * return Flux.zip(this.feedingTypeFamilyGroupAndFeedingRepository
                     * .saveAll(feedingTypeFeedingGroupAndFeedingEntities),
                     * Flux.fromIterable(feedingDTO.getRoomOfferIds())
                     * .flatMap(roomOfferId -> {
                     * RoomOfferFeedingEntity roomOfferFeedingEntity =
                     * RoomOfferFeedingEntity.builder()
                     * .roomOfferId(roomOfferId)
                     * .feedingId(savedFeeding.getId())
                     * .build();
                     * return roomOfferFeedingRepository.save(roomOfferFeedingEntity);
                     * 
                     * }))
                     * .then(Mono.just(savedFeeding));
                     */
                    return Flux.fromIterable(entities)
                            .flatMap(entity -> this.feedingTypeFamilyGroupAndFeedingRepository.save(entity))
                            .collectList()
                            .flatMap(savedItems -> {
                                return Flux.fromIterable(feedingDTO.getRoomOfferIds())
                                        .flatMap(roomOfferId -> {
                                            RoomOfferFeedingEntity roomOffer = RoomOfferFeedingEntity.builder()
                                                    .roomOfferId(roomOfferId)
                                                    .feedingId(savedFeeding.getId())
                                                    .build();
                                            return roomOfferFeedingRepository.save(roomOffer);
                                        })
                                        .collectList()
                                        .thenReturn(savedFeeding);
                            });
                });
    }

    @Override
    public Mono<FeedingEntity> updateFeeding(FeedingDto feedingDTO) {
        return feedingRepository.save(feedingDTO.getFeedingEntity())
                .flatMap(updatedFeeding -> {
                    if (feedingDTO.getRoomOfferIds() == null || feedingDTO.getRoomOfferIds().isEmpty()) {
                        return Mono.just(updatedFeeding);
                    }
                    return roomOfferFeedingRepository.findByFeedingId(updatedFeeding.getId())
                            .collectList()
                            .flatMap(existingRelations -> {
                                return Flux.fromIterable(existingRelations)
                                        .flatMap(roomOfferFeeding -> {
                                            if (!feedingDTO.getRoomOfferIds()
                                                    .contains(roomOfferFeeding.getRoomOfferId())) {
                                                return bookingRepository
                                                        .existsByRoomOfferId(roomOfferFeeding.getRoomOfferId())
                                                        .flatMap(hasBooking -> {
                                                            if (!hasBooking) {
                                                                return roomOfferFeedingRepository
                                                                        .delete(roomOfferFeeding);
                                                            } else {
                                                                return Mono.empty();
                                                            }
                                                        });
                                            } else {
                                                return Mono.empty();
                                            }
                                        })
                                        .thenMany(Flux.fromIterable(feedingDTO.getRoomOfferIds())
                                                .flatMap(roomOfferId -> {
                                                    return roomOfferFeedingRepository
                                                            .existsByFeedingIdAndRoomOfferId(updatedFeeding.getId(),
                                                                    roomOfferId)
                                                            .flatMap(exists -> {
                                                                if (!exists) {
                                                                    RoomOfferFeedingEntity newRelation = RoomOfferFeedingEntity
                                                                            .builder()
                                                                            .roomOfferId(roomOfferId)
                                                                            .feedingId(updatedFeeding.getId())
                                                                            .build();
                                                                    return roomOfferFeedingRepository.save(newRelation);
                                                                } else {
                                                                    return Mono.empty();
                                                                }
                                                            });
                                                }))
                                        .then(Mono.just(updatedFeeding));
                            });
                });
    }

    @Override
    public Mono<Void> deleteFeeding(Integer feedingId) {
        System.out.println(feedingId);
        return feedingRepository.deleteById(feedingId); // Obtener los roomOfferIds asociados
        // .flatMap(roomOfferFeeding -> {
        // Verificar si existe una reserva con bookingStateId = 3 para cada roomOfferId
        // return
        // bookingRepository.existsByRoomOfferIdAndBookingStateId(roomOfferFeeding.getRoomOfferId(),
        // 3)
        // .filter(exists -> !exists) // Solo continuar si no existe una reserva con
        // bookingStateId = 3
        // .switchIfEmpty(Mono.error(new RuntimeException("No se puede eliminar la
        // Alimentación porque existe una reserva asociada!")))
        // .then(); // Continuar si la condición se cumple
        // })
        // .then(feedingRepository.deleteById(feedingId)); // Si no se encontró ninguna
        // reserva, eliminar el FeedingEntity
    }

    @Override
    public Flux<RoomOfferFeedingEntity> findRoomOfferByFeedingId(Integer feedingId) {
        return roomOfferFeedingRepository.findByFeedingId(feedingId);
    }
}
