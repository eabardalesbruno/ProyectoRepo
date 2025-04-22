package com.proriberaapp.ribera.services.admin.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.ExternalAuthService;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.MembershipDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseDataMembershipDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseInclubLoginDto;
import com.proriberaapp.ribera.Domain.dto.DiscountDto;
import com.proriberaapp.ribera.Domain.dto.UserNameAndDiscountDto;
import com.proriberaapp.ribera.Domain.entities.DiscountEntity;
import com.proriberaapp.ribera.Infraestructure.repository.DiscountRepository;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.services.admin.VerifiedDiscountAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerifiedDiscountAdminServiceImpl implements VerifiedDiscountAdminService {
    @Value("${inclub.api.url.subscriptions}")
    private String URL_MEMBERSHIPS;
    @Value("${inclub.api.url.user}")
    private String URL_DATA_USER;
    @Value("${inclub.api.url.promotialGuest}")
    private String URL_PROMOTIONALGUESTS;

    @Value("${backoffice.api.url}")
    private String urlBackOffice;
    @Value("${inclub.api.url.user}")
    private String urlBackofficeUser;

    private final UserClientRepository userClientRepository;
    private final ExternalAuthService externalAuthService;
    private final WebClient webClient;
    private final DiscountRepository discountRepository;


    @Override
    public Mono<UserNameAndDiscountDto> verifiedPercentajeDiscountAdmin(Integer userId, BigDecimal costTotal) {
        log.info("Start the service verifiedPercentajeDiscountAdmin with parameters: userId :{}, costTotal: {}",
                userId, costTotal);
        return userClientRepository.findById(userId)
                .flatMap(userData -> loadMembershipsInsortInclub(userData.getUsername())
                        .flatMap(memberships ->
                                calculateDiscounts(memberships, userData.getUsername(), costTotal)));
    }

    private Mono<List<MembershipDto>> loadMembershipsInsortInclub(String username) {
        return loadDataUserRiber(username)
                .flatMap(user -> externalAuthService.getExternalToken()
                        .flatMap(externalToken -> {
                            String uri = URL_MEMBERSHIPS.concat("/").concat(String.valueOf(user.getData().getId()));
                            WebClient webClient = WebClient.builder()
                                    .baseUrl(uri)
                                    .defaultHeader("Authorization", "Bearer " + externalToken)
                                    .build();
                            return webClient.get()
                                    .retrieve()
                                    .bodyToMono(ResponseDataMembershipDto.class)
                                    .map(ResponseDataMembershipDto::getData);
                        }))
                .flatMapMany(Flux::fromIterable)
                .flatMap(membership -> getPromotionalGuestById(membership.getId())
                        .map(promotionalGuest -> {
                            membership.setData(promotionalGuest.getData());
                            return membership;
                        })
                        .defaultIfEmpty(membership))
                .collectList();
    }

    private Mono<ResponseInclubLoginDto> loadDataUserRiber(String username) {
        String uri = URL_DATA_USER.concat("/").concat(username);
        WebClient webClient = WebClient.create(uri);
        return webClient.get()
                .retrieve()
                .bodyToMono(
                        ResponseInclubLoginDto.class)
                .onErrorResume(e -> Mono.empty());
    }

    private Mono<MembershipDto> getPromotionalGuestById(Integer id) {
        String uri = URL_PROMOTIONALGUESTS.concat("/").concat(String.valueOf(id));
        WebClient webClient = WebClient.create(uri);
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(MembershipDto.class)
                .onErrorResume(e -> Mono.empty());
    }

    private Mono<UserNameAndDiscountDto> calculateDiscounts(
            List<MembershipDto> memberships, String username, BigDecimal costTotal) {
        if (memberships.isEmpty()) {
            return Mono.just(UserNameAndDiscountDto.empty());
        }

        List<Integer> applicablePackageIds = filterApplicablePackages(memberships);

        if (applicablePackageIds.isEmpty()) {
            return Mono.just(UserNameAndDiscountDto.empty());
        }

        return discountRepository.getDiscountWithItemsAndCurrentYear(applicablePackageIds).collectList()
                .flatMap(discounts -> buildDiscountResponse(discounts, username, costTotal));
    }

    private List<Integer> filterApplicablePackages(List<MembershipDto> memberships) {
        return memberships.stream()
                .filter(d -> List.of(2, 29, 43).contains(d.getIdFamilyPackage()))
                .map(MembershipDto::getIdPackage)
                .toList();
    }

    private Mono<UserNameAndDiscountDto> buildDiscountResponse(
            List<DiscountEntity> discounts, String username, BigDecimal costTotal) {
        float totalPercentage = calculateTotalPercentage(discounts);
        List<DiscountDto> discountDtos = mapToDiscountDtos(discounts, costTotal);

        return Mono.just(UserNameAndDiscountDto.builder()
                .username(username)
                .percentage(totalPercentage)
                .discounts(discountDtos)
                .build());
    }

    private float calculateTotalPercentage(List<DiscountEntity> discounts) {
        return discounts.stream()
                .map(DiscountEntity::getPercentage)
                .reduce(0.0f, Float::sum);
    }

    private List<DiscountDto> mapToDiscountDtos(List<DiscountEntity> discounts, BigDecimal costTotal) {
        return discounts.stream()
                .map(p -> createDiscountDto(p, costTotal))
                .toList();
    }

    private DiscountDto createDiscountDto(DiscountEntity discount, BigDecimal costTotal) {
        return DiscountDto.builder()
                .id(discount.getId())
                .name(discount.getName())
                .amount(discount.getPercentage() * costTotal.floatValue() / 100)
                .percentage(discount.getPercentage())
                .applyToReservation(discount.isApplyToReservation())
                .applyToFood(discount.isApplyToFood())
                .build();
    }
}
