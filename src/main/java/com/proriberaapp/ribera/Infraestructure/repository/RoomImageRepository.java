package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.RoomImagesEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RoomImageRepository extends R2dbcRepository<RoomImagesEntity, Integer> {
    Flux<RoomImagesEntity> findAllByRoomId(Integer roomId);

    Mono<Void> deleteAllByImagePath(String imagePath);
}
