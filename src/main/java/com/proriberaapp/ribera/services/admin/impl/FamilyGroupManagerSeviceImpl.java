package com.proriberaapp.ribera.services.admin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proriberaapp.ribera.Domain.entities.FamilyGroupEntity;
import com.proriberaapp.ribera.Infraestructure.repository.FamilyGroupRepository;

import reactor.core.publisher.Flux;

@Service
public class FamilyGroupManagerSeviceImpl {
    @Autowired
    private FamilyGroupRepository familyGroupRepository;


    public Flux<FamilyGroupEntity> getActive() {
        return familyGroupRepository.findAllByStatus(1);
    }
    
}
