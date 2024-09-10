package com.proriberaapp.ribera.services.client.impl;
import com.proriberaapp.ribera.Domain.entities.EntertainmentEntity;
import com.proriberaapp.ribera.Infraestructure.repository.EntertainmentRepository;
import com.proriberaapp.ribera.services.client.EntertainmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EntertainmentServiceImpl implements EntertainmentService {
    private final EntertainmentRepository entertainmentRepository;

    @Autowired
    public EntertainmentServiceImpl(EntertainmentRepository entertainmentRepository) {
        this.entertainmentRepository = entertainmentRepository;
    }

    @Override
    public Mono<EntertainmentEntity> createEntertainment(EntertainmentEntity entertainment) {
        return entertainmentRepository.save(entertainment);
    }

    @Override
    public Mono<EntertainmentEntity> getEntertainment(Integer entertainmentId) {
        return entertainmentRepository.findById(entertainmentId);
    }

    @Override
    public Flux<EntertainmentEntity> getAllEntertainments() {
        return entertainmentRepository.findAll();
    }

    @Override
    public Mono<Void> deleteEntertainment(Integer entertainmentId) {
        return entertainmentRepository.deleteById(entertainmentId);
    }
}