package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.RoomOfferFeedingEntity;
import com.proriberaapp.ribera.Infraestructure.repository.RoomOfferFeedingRepository;
import com.proriberaapp.ribera.services.client.RoomOfferFeedingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoomOfferFeedingServiceImpl implements RoomOfferFeedingService {

    private final RoomOfferFeedingRepository RoomOfferFeedingRepository;

    @Override
    public Flux<RoomOfferFeedingEntity> findAllRoomOfferFeeding() {
        return RoomOfferFeedingRepository.findAll();
    }

    @Override
    public Mono<RoomOfferFeedingEntity> findRoomOfferFeedingById(Integer id) {
        return RoomOfferFeedingRepository.findById(id);
    }

    @Override
    public Mono<RoomOfferFeedingEntity> saveRoomOfferFeeding(RoomOfferFeedingEntity roomOfferFeeding) {
        return RoomOfferFeedingRepository.save(roomOfferFeeding);
    }


    @Override
    public Mono<Void> deleteRoomOfferFeeding(Integer id) {
        return RoomOfferFeedingRepository.deleteById(id);
    }
}
