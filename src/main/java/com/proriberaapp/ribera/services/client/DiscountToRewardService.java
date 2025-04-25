package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.DiscountToRewardEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DiscountToRewardService {

    Mono<DiscountToRewardEntity> getDiscountById(Integer id);

    Mono<DiscountToRewardEntity> getDiscountByName(String name);

    Flux<DiscountToRewardEntity> getAllDiscount();

    Mono<DiscountToRewardEntity> saveDiscount(DiscountToRewardEntity discountToReward);

    Mono<DiscountToRewardEntity> updateDiscount(Integer id, DiscountToRewardEntity discountToReward);
}
