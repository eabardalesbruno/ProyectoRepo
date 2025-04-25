package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.response.UserRewardResponse;
import com.proriberaapp.ribera.Domain.enums.RewardType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.proriberaapp.ribera.Api.controllers.client.dto.request.UserRewardRequest;
public interface UserRewardService {

    Flux<UserRewardResponse> findAll();

    Mono<UserRewardResponse> findById(Long id);

    Flux<UserRewardResponse> findByUserId(Long userId);

    Flux<UserRewardResponse> findByActive(Boolean active);

    Flux<UserRewardResponse> findByType(RewardType type);

    Mono<UserRewardResponse> create(UserRewardRequest userRewardRequest,String type,Double totalCost);
}