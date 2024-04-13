package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Domain.entities.SendAndReceiveEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SendAndReceiveService {
    Mono<SendAndReceiveEntity> createSendAndReceive(SendAndReceiveEntity sendAndReceive);
    Mono<SendAndReceiveEntity> getSendAndReceiveById(Integer id);
    Flux<SendAndReceiveEntity> getAllSendAndReceives();
    Mono<SendAndReceiveEntity> updateSendAndReceive(Integer id, SendAndReceiveEntity sendAndReceive);
    Mono<Void> deleteSendAndReceive(Integer id);
}