package com.proriberaapp.ribera.services.client;

import reactor.core.publisher.Mono;

public interface UserRewardTrasferHistoryService {

    Mono<Void> transferRewards(String fromInput, String toInput, Double amount, String subCategory);

}