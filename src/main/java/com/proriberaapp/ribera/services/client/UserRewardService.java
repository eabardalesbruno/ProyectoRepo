package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.GroupedSubscriptionFamilyRewardResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.GroupedSubscriptionRewardResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.request.RewardReleaseRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.HistoricalRewardResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.UserRewardResponse;
import com.proriberaapp.ribera.Domain.dto.PercentageDto;
import com.proriberaapp.ribera.Domain.enums.RewardType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.proriberaapp.ribera.Api.controllers.client.dto.request.UserRewardRequest;

import java.util.List;
public interface UserRewardService {

    Flux<UserRewardResponse> findAll();

    Mono<UserRewardResponse> findById(Long id);

    Flux<UserRewardResponse> findByUserId(Long userId);

    Flux<UserRewardResponse> findByActive(Boolean active);

    Flux<UserRewardResponse> findByType(RewardType type);

    Mono<UserRewardResponse> create(UserRewardRequest userRewardRequest,String type,Double totalCost);

    Mono<Double> updateStatusRewardsAndGetTotal(Integer bookingId,Integer userId);

    Mono<HistoricalRewardResponse> getHistoricalRewardsByUsernameAndPagination(
            String username,Integer page, Integer size,String status,String membership,String startDate,String endDate);

    Mono<GroupedSubscriptionRewardResponse> getUserSubscriptionsByUsername(String username);
    Mono<GroupedSubscriptionFamilyRewardResponse> getGroupedRewardsByUsername(String username);
    Mono<List<PercentageDto>> getRandomSubscriptionPercentages(String username);
    Mono<Void> releaseUserReward(RewardReleaseRequest request);
    Mono<Integer> getUserIdByUsername(String username);
}