package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.RoomImagesEntity;
import com.proriberaapp.ribera.Infraestructure.repository.RoomImageRepository;
import com.proriberaapp.ribera.services.client.RoomImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoomImageServiceImpl implements RoomImageService {

    private final RoomImageRepository roomImageRepository;

    @Override
    public Mono<RoomImagesEntity> saveRoomImages(Integer roomId, String filePath) {
        RoomImagesEntity roomImagesEntity = new RoomImagesEntity();
        roomImagesEntity.setRoomId(roomId);
        roomImagesEntity.setImagePath(filePath);
        return roomImageRepository.save(roomImagesEntity);
    }

    @Override
    public Mono<Void> deleteByImagePath(String imagePath) {
        return roomImageRepository.deleteAllByImagePath(imagePath);
    }

    @Override
    public Flux<RoomImagesEntity> findAll() {
        return roomImageRepository.findAll();
    }

    @Override
    public Flux<RoomImagesEntity> findAllByRoomId(Integer roomId) {
        return roomImageRepository.findAllByRoomId(roomId);
    }

}
