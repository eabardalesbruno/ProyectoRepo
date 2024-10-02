package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.RoomImagesEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoomImageService {
    Mono<RoomImagesEntity> saveRoomImages(Integer roomId, String filePath);

    Mono<Void> deleteByImagePath(String imagePath);

    Flux<RoomImagesEntity> findAll();

    Flux<RoomImagesEntity> findAllByRoomId(Integer roomId);
}
