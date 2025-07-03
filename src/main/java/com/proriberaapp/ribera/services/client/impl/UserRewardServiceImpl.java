package com.proriberaapp.ribera.services.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proriberaapp.ribera.Api.controllers.admin.dto.ExternalAuthService;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.*;
import com.proriberaapp.ribera.Api.controllers.client.dto.request.RewardReleaseRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.request.UserRewardRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.HistoricalRewardResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.SubscriptionRewardResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.UserRewardResponse;
import com.proriberaapp.ribera.Domain.dto.PercentageDto;
import com.proriberaapp.ribera.Domain.dto.RetrieveFamilyPackageResponseDto;
import com.proriberaapp.ribera.Domain.dto.RewardSubscriptionDto;
import com.proriberaapp.ribera.Domain.entities.UserRewardEntity;
import com.proriberaapp.ribera.Domain.enums.RewardType;
import com.proriberaapp.ribera.Domain.mapper.UserRewardMapper;
import com.proriberaapp.ribera.Infraestructure.repository.UserRewardRepository;
import com.proriberaapp.ribera.services.admin.DiscountToRewardService;
import com.proriberaapp.ribera.services.client.UserRewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

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

    @Override
    public Mono<Double> updateStatusRewardsAndGetTotal(Integer bookingId, Integer userId) {
        return userRewardRepository.updateStatusByBookingIdAndUserId(Constants.ACTIVE, bookingId, userId)
                .then(userRewardRepository.sumPointsByBookingIdAndUserId(bookingId, userId));
    }

    @Override
    public Mono<HistoricalRewardResponse> getHistoricalRewardsByUsernameAndPagination(
            String username, Integer page,Integer size,String status,String membership,String startDate,String endDate) {
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

                    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(urlBoRewards)
                            .pathSegment(String.valueOf(responseInclub.getData().getId()), "historical-rewards")
                            .queryParam("page", page)
                            .queryParam("size", size);

                    Optional.ofNullable(status)
                            .filter(s -> !s.trim().isEmpty())
                            .ifPresent(s -> uriBuilder.queryParam("status", s.trim()));

                    Optional.ofNullable(membership)
                            .filter(m -> !m.trim().isEmpty())
                            .ifPresent(m -> uriBuilder.queryParam("membership", m.trim()));

                    Optional.ofNullable(startDate)
                            .filter(sd -> !sd.trim().isEmpty())
                            .ifPresent(sd -> uriBuilder.queryParam("startDate", sd.trim()));

                    Optional.ofNullable(endDate)
                            .filter(ed -> !ed.trim().isEmpty())
                            .ifPresent(ed -> uriBuilder.queryParam("endDate", ed.trim()));

                    String finalUri = uriBuilder.build().toUriString();
                    return webClient.get()
                            .uri(finalUri)
                            .header("Authorization", "Bearer " + tokenBackOffice)
                            .retrieve()
                            .bodyToMono(HistoricalRewardResponse.class);
                });
    }
    @Override
    public Mono<GroupedSubscriptionRewardResponse> getUserSubscriptionsByUsername(String username) {
        return getUserIdByUsername(username)
                .zipWith(externalAuthService.getExternalToken())
                .flatMap(tuple -> {
                    int userId = tuple.getT1();
                    String token = tuple.getT2();
                    String uri = urlBoRewards + "/" + userId + "/rewards/subscriptions";

                    return webClient.get().uri(uri)
                            .header("Authorization", "Bearer " + token)
                            .retrieve()
                            .bodyToMono(SubscriptionRewardResponse.class)
                            .map(this::groupResponseByFamilyPackage);
                });
    }

    @Override
    public Mono<Integer> getUserIdByUsername(String username) {
        return webClient.get().uri(urlLoginUserInclub + "/" + username)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(json -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        UserLoginResponse response = mapper.readValue(json, UserLoginResponse.class);
                        int userId = response.getData().getId();
                        if (userId <= 0) {
                            return Mono.error(new RuntimeException("User ID inválido para username: " + username));
                        }
                        return Mono.just(userId);
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Error al parsear UserDto: " + e.getMessage()));
                    }
                });
    }

    private GroupedSubscriptionRewardResponse groupResponseByFamilyPackage(SubscriptionRewardResponse originalResponse) {
        Map<String, List<RewardSubscriptionDto>> grouped = originalResponse.getData().stream()
                .sorted(Comparator.comparing(RewardSubscriptionDto::getPackageName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.groupingBy(
                        RewardSubscriptionDto::getFamilyPackageName,
                        TreeMap::new,
                        Collectors.toList()
                ));

        GroupedSubscriptionRewardResponse response = new GroupedSubscriptionRewardResponse();
        response.setResult(true);
        response.setData(grouped);
        return response;
    }

    @Override
    public Mono<GroupedSubscriptionFamilyRewardResponse> getGroupedRewardsByUsername(String username) {
        return getUserIdByUsername(username)
                .flatMap(userId -> webClient.get()
                        .uri(urlBoRewards + "/" + userId + "/rewards/grouped")
                        .retrieve()
                        .bodyToMono(GroupedSubscriptionFamilyRewardResponse.class));
    }

    public Mono<List<PercentageDto>> getRandomSubscriptionPercentages(String username) {
        return getUserSubscriptionsByUsername(username)
                .flatMap(groupedResponse -> {
                    List<RewardSubscriptionDto> all = groupedResponse.getData().values().stream()
                            .flatMap(List::stream)
                            .collect(Collectors.toList());

                    if (all.isEmpty()) {
                        return Mono.error(new RuntimeException("No se encontraron suscripciones para el usuario."));
                    }
                    RewardSubscriptionDto selected = all.get(new Random().nextInt(all.size()));
                    Long subscriptionId = selected.getSubscriptionId();

                    String uri = urlBoRewards + "subscriptions/" + subscriptionId + "/percentages";

                    return webClient.get().uri(uri)
                            .retrieve()
                            .bodyToMono(PackageDetailRewardsResponse.class)
                            .map(resp -> {
                                if (resp.getData() == null || resp.getData().getPackageDetailRewards() == null) {
                                    throw new RuntimeException("No se encontró información de porcentajes.");
                                }
                                return resp.getData().getPackageDetailRewards();
                            });
                });
    }

    @Override
    public Mono<Void> releaseUserReward(RewardReleaseRequest request) {
        return externalAuthService.getExternalToken()
                .flatMap(token -> webClient.post()
                        .uri(urlBoRewards + "/rewards/release")
                        .header("Authorization", "Bearer " + token)
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(Void.class));
    }
}