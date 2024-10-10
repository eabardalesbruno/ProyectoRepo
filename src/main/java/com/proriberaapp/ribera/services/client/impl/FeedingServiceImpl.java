package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.FeedingEntity;
import com.proriberaapp.ribera.Infraestructure.repository.FeedingRepository;
import com.proriberaapp.ribera.services.client.FeedingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FeedingServiceImpl implements FeedingService {

    private final FeedingRepository feedingRepository;

    @Override
    public Flux<FeedingEntity> findAllFeeding() {
        return feedingRepository.findAll();
    }

    @Override
    public Mono<FeedingEntity> findFeedingById(Integer id) {
        return feedingRepository.findById(id);
    }

    @Override
    public Mono<FeedingEntity> saveFeeding(FeedingEntity feeding) {
        return feedingRepository.save(feeding);
    }

    @Override
    public Mono<FeedingEntity> updateFeeding(FeedingEntity feeding) {
        return feedingRepository.findById(feeding.getId())
                .flatMap(feedingEntity -> {
                    feedingEntity.setName(feeding.getName());
                    feedingEntity.setDescription(feeding.getDescription());
                    feedingEntity.setCost(feeding.getCost());
                    feedingEntity.setState(feeding.getState());
                    return feedingRepository.save(feedingEntity);
                });
    }

    @Override
    public Mono<Void> deleteFeeding(Integer id) {
        return feedingRepository.deleteById(id);
    }
}
