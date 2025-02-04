package com.proriberaapp.ribera.services.admin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proriberaapp.ribera.Domain.entities.FeedingTypeEntity;
import com.proriberaapp.ribera.Infraestructure.repository.FeedingTypeRepository;

import reactor.core.publisher.Flux;

@Service
public class FeedingTypeManagerServiceImpl {

    @Autowired
    private FeedingTypeRepository feedingTypeRepository;

    public Flux<FeedingTypeEntity> getActive() {
        return feedingTypeRepository.findAllByStatus(1);
    }

}
