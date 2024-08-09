package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.SendAndReceiveEntity;
import com.proriberaapp.ribera.Infraestructure.repository.SendAndReceiveRepository;
import com.proriberaapp.ribera.services.client.SendAndReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SendAndReceiveServiceImpl implements SendAndReceiveService {
    private final SendAndReceiveRepository sendAndReceiveRepository;

    @Autowired
    public SendAndReceiveServiceImpl(SendAndReceiveRepository sendAndReceiveRepository) {
        this.sendAndReceiveRepository = sendAndReceiveRepository;
    }

    @Override
    public Mono<SendAndReceiveEntity> createSendAndReceive(SendAndReceiveEntity sendAndReceive) {
        return sendAndReceiveRepository.save(sendAndReceive);
    }

    @Override
    public Mono<SendAndReceiveEntity> getSendAndReceiveById(Integer id) {
        return sendAndReceiveRepository.findById(id);
    }

    @Override
    public Flux<SendAndReceiveEntity> getAllSendAndReceives() {
        return sendAndReceiveRepository.findAll();
    }

    @Override
    public Mono<SendAndReceiveEntity> updateSendAndReceive(Integer id, SendAndReceiveEntity sendAndReceive) {
        return sendAndReceiveRepository.findById(id)
                .flatMap(existingSendAndReceive -> {
                    existingSendAndReceive.setSendandreceivedesc(sendAndReceive.getSendandreceivedesc());
                    return sendAndReceiveRepository.save(existingSendAndReceive);
                });
    }

    @Override
    public Mono<Void> deleteSendAndReceive(Integer id) {
        return sendAndReceiveRepository.deleteById(id);
    }
}