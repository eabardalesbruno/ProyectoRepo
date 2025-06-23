package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.ExternalAuthService;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseInclubLoginDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.request.UserRewardRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.HistoricalRewardResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.UserRewardResponse;
import com.proriberaapp.ribera.Domain.dto.RetrieveFamilyPackageResponseDto;
import com.proriberaapp.ribera.Domain.entities.UserRewardEntity;
import com.proriberaapp.ribera.Domain.enums.RewardType;
import com.proriberaapp.ribera.Domain.mapper.UserRewardMapper;
import com.proriberaapp.ribera.Infraestructure.repository.UserRewardRepository;
import com.proriberaapp.ribera.services.admin.DiscountToRewardService;
import com.proriberaapp.ribera.services.client.UserRewardService;
import com.proriberaapp.ribera.utils.constants.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserRewardServiceImpl implements UserRewardService {

    @Value("${inclub.api.url.user}")
    private String urlLoginUserInclub;
    @Value("${inclub.api.url.bo.rewards}")
    private String urlBoRewards;

    private final ExternalAuthService externalAuthService;
    private final WebClient webClient;

    private final UserRewardRepository userRewardRepository;
    private final DiscountToRewardService discountToRewardService;
    private final UserRewardMapper userRewardMapper;

    @Override
    public Flux<UserRewardResponse> findAll() {
        return userRewardRepository.findAll()
                .map(userRewardMapper::toDto);
    }

    @Override
    public Mono<UserRewardResponse> findById(Long id) {
        return userRewardRepository.findById(id)
                .map(userRewardMapper::toDto);
    }

    @Override
    public Flux<UserRewardResponse> findByUserId(Long userId) {
        return userRewardRepository.findByUserIdAndStatus(userId,Constants.ACTIVE)
                .map(userRewardMapper::toDto);
    }

    @Override
    public Flux<UserRewardResponse> findByActive(Boolean active) {
        int status = active ? 1 : 0;
        return userRewardRepository.findByStatus(status)
                .map(userRewardMapper::toDto);
    }

    @Override
    public Flux<UserRewardResponse> findByType(RewardType type) {
        return userRewardRepository.findByType(type.name())
                .map(userRewardMapper::toDto);
    }

    @Override
    public Mono<UserRewardResponse> create(UserRewardRequest userRewardDTO, String type, Double totalCost) {

        return discountToRewardService.getDiscountByName(type)
                .flatMap(discountToReward -> {
                    RewardType rewardTypeValue;
                    if (Constants.REWARD_TYPE_BOOKING.equalsIgnoreCase(type)) {
                        rewardTypeValue = RewardType.BOOKING;
                    } else if (Constants.REWARD_TYPE_FEEDING.equalsIgnoreCase(type)) {
                        rewardTypeValue = RewardType.FEEDING;
                    } else {
                        rewardTypeValue = RewardType.OTHER;
                    }
                    var rewardReq = UserRewardRequest.builder()
                            .userId(userRewardDTO.getUserId())
                            .points(BigDecimal.valueOf(discountToReward.getDiscountValue() * totalCost)
                                    .setScale(2, RoundingMode.HALF_UP)
                                    .doubleValue())
                            .expirationDate(LocalDateTime.now().plusYears(1))
                            .type(rewardTypeValue)
                            .bookingId(userRewardDTO.getBookingId())
                            .build();

                    UserRewardEntity entity = userRewardMapper.toEntity(rewardReq);
                    entity.setDate(LocalDateTime.now());
                    entity.setStatus(0);
                    entity.setBookingId(userRewardDTO.getBookingId());
                    return userRewardRepository.save(entity)
                            .map(userRewardMapper::UserRewardEntitytoDto);
                });
    }

    @Override
    public Mono<Double> updateStatusRewardsAndGetTotal(Integer bookingId, Integer userId) {
        return userRewardRepository.updateStatusByBookingIdAndUserId(Constants.ACTIVE, bookingId, userId)
                .then(userRewardRepository.sumPointsByBookingIdAndUserId(bookingId, userId));
    }

    @Override
    public Mono<HistoricalRewardResponse> getHistoricalRewardsByUsernameAndPagination(String username, Integer page, Integer size) {
        return externalAuthService.getExternalToken()
                .zipWith(
                        webClient.get()
                                .uri(urlLoginUserInclub + "/" + username)
                                .retrieve()
                                .bodyToMono(ResponseInclubLoginDto.class)
                )
                .flatMap(tuple -> {
                    String tokenBackOffice = tuple.getT1();
                    ResponseInclubLoginDto responseInclub = tuple.getT2();
                    return webClient.get()
                            .uri(urlBoRewards + responseInclub.getData().getId() + "/historical-rewards" + "?page=" + page + "&size=" + size)
                            .header("Authorization", "Bearer " + tokenBackOffice)
                            .retrieve()
                            .bodyToMono(HistoricalRewardResponse.class);
                });
    }
}