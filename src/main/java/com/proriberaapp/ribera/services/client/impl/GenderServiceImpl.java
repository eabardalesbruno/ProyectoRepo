package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.GenderEntity;
import com.proriberaapp.ribera.Infraestructure.repository.GenderRepository;
import com.proriberaapp.ribera.services.client.GenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GenderServiceImpl implements GenderService {

    private final GenderRepository genderRepository;

    @Autowired
    public GenderServiceImpl(GenderRepository genderRepository) {
        this.genderRepository = genderRepository;
    }

    @Override
    public Mono<GenderEntity> createGender(GenderEntity genderEntity) {
        return genderRepository.save(genderEntity);
    }

    @Override
    public Mono<GenderEntity> updateGender(Integer genderId, GenderEntity genderEntity) {
        return genderRepository.findById(genderId)
                .flatMap(existingGender -> {
                    existingGender.setGenderDesc(genderEntity.getGenderDesc());
                    return genderRepository.save(existingGender);
                });
    }

    @Override
    public Mono<GenderEntity> getGenderById(Integer genderId) {
        return genderRepository.findById(genderId);
    }

    @Override
    public Flux<GenderEntity> getAllGenders() {
        return genderRepository.findAll();
    }

    @Override
    public Mono<Void> deleteGender(Integer genderId) {
        return genderRepository.deleteById(genderId);
    }
}
