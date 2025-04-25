package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.client.dto.request.UserRewardRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.UserRewardResponse;
import com.proriberaapp.ribera.Domain.entities.UserRewardEntity;
import com.proriberaapp.ribera.Domain.enums.RewardType;
import com.proriberaapp.ribera.Domain.mapper.UserRewardMapper;
import com.proriberaapp.ribera.Infraestructure.repository.UserRewardRepository;
import com.proriberaapp.ribera.services.client.DiscountToRewardService;
import com.proriberaapp.ribera.services.client.UserRewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserRewardServiceImpl implements UserRewardService {
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
        return userRewardRepository.findByUserId(userId)
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
                    var rewardReq = UserRewardRequest.builder()
                            .userId(userRewardDTO.getUserId())
                            .points(BigDecimal.valueOf(discountToReward.getDiscountValue() * totalCost)
                                    .setScale(2, RoundingMode.HALF_UP)
                                    .doubleValue())
                            .expirationDate(LocalDateTime.now().plusYears(1))
                            .type(RewardType.BOOKING)
                            .build();

                    UserRewardEntity entity = userRewardMapper.toEntity(rewardReq);
                    entity.setDate(LocalDateTime.now());
                    entity.setStatus(1);
                    return userRewardRepository.save(entity)
                            .map(userRewardMapper::UserRewardEntitytoDto);
                });

    }

}