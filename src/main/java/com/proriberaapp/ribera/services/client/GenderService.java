package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.GenderEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenderService {
    Mono<GenderEntity> createGender(GenderEntity genderEntity);
    Mono<GenderEntity> updateGender(Integer genderId, GenderEntity genderEntity);
    Mono<GenderEntity> getGenderById(Integer genderId);
    Flux<GenderEntity> getAllGenders();
    Mono<Void> deleteGender(Integer genderId);
}