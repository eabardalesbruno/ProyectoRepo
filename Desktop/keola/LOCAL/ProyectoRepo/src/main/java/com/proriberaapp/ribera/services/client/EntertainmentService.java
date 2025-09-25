package com.proriberaapp.ribera.services.client;
import com.proriberaapp.ribera.Domain.entities.EntertainmentEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EntertainmentService {
    Mono<EntertainmentEntity> createEntertainment(EntertainmentEntity entertainment);
    Mono<EntertainmentEntity> getEntertainment(Integer entertainmentId);
    Flux<EntertainmentEntity> getAllEntertainments();
    Mono<Void> deleteEntertainment(Integer entertainmentId);
}