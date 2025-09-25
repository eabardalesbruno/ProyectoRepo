package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.RoomImagesEntity;

import java.util.List;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RoomImageRepository extends R2dbcRepository<RoomImagesEntity, Integer> {
    Flux<RoomImagesEntity> findAllByRoomId(Integer roomId);

    Mono<Void> deleteAllByRoomId(Integer roomId);

    Mono<Void> deleteAllByImagePath(String imagePath);

    @Query("DELETE FROM roomimages WHERE id IN (:roomImagenId)")
    Mono<Void> deleteAllById(List<Integer> roomImagenId);

}
