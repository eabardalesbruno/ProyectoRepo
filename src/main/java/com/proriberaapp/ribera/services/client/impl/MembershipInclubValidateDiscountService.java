package com.proriberaapp.ribera.services.client.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.MembershipDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseDataMembershipDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseInclubLoginDto;
import com.proriberaapp.ribera.Domain.dto.DiscountDto;
import com.proriberaapp.ribera.Domain.dto.UserNameAndDiscountDto;
import com.proriberaapp.ribera.Infraestructure.repository.DiscountRepository;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.services.client.MembershipsService;
import com.proriberaapp.ribera.services.client.VerifiedDiscountService;

import reactor.core.publisher.Mono;

@Service
public class MembershipInclubValidateDiscountService implements VerifiedDiscountService, MembershipsService {

    @Value("${inclub.api.url.subscriptions}")
    private String URL_MEMBERSHIPS;
    @Value("${inclub.api.url.user}")
    private String URL_DATA_USER;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private UserClientRepository userClientRepository;

    @Override
    public Mono<List<MembershipDto>> loadMembershipsActives(String username) {
        return this.loadMembershipsInsortInclub(username)
                .flatMap(s -> {
                    List<MembershipDto> activeMemberships = s.stream().filter(p -> p.getIdStatus() == 1).toList();
                    return activeMemberships.isEmpty() ? Mono.just(null) : Mono.just(activeMemberships);
                });

    }

    @Override
    public Mono<List<MembershipDto>> loadMembershipsInsortInclub(String username) {
        return this.loadDataUserRiber(username)
                .flatMap(user -> {
                    String uri = URL_MEMBERSHIPS.concat("/").concat(String.valueOf(user.getData().getId()));
                    return Mono.just(uri);
                }).flatMap(uri -> {
                    WebClient webClient = WebClient.create(uri);
                    return webClient.get()
                            .retrieve()
                            .bodyToMono(ResponseDataMembershipDto.class)
                            .flatMap(response -> {
                                List<MembershipDto> data = response.getData().stream().toList();
                                return data.size() > 0 ? Mono.just(data) : Mono.empty();
                            }).onErrorResume(e -> {
                                return Mono.just(List.of());
                            });
                });

    }

    private Mono<ResponseInclubLoginDto> loadDataUserRiber(String username) {
        String uri = URL_DATA_USER.concat("/").concat(username);
        WebClient webClient = WebClient.create(uri);
        return webClient.get()
                .retrieve()
                .bodyToMono(
                        ResponseInclubLoginDto.class)
                .onErrorResume(e -> {
                    return Mono.empty();
                });
    }

    @Override
    public Mono<UserNameAndDiscountDto> verifiedPercentajeDiscount(int userId) {
        return this.userClientRepository.findById(userId)
                .flatMap(userData -> {
                    return this.loadMembershipsInsortInclub(
                            userData.getUsername())
                            .flatMap(memberships -> {
                                if(memberships.size()==0){
                                    return Mono.just(UserNameAndDiscountDto.empty());
                                }
                                return this.discountRepository
                                        .getDiscountWithItemsAndCurrentYear(userId,
                                                memberships.stream().filter(d->d.getIdStatus()==1 && d.getIdFamilyPackage()==2).map(d -> d.getIdPackage()).toList())
                                        .collectList().flatMap(listPercentage -> {
                                            return Mono.just(UserNameAndDiscountDto.builder()
                                                    .username(userData.getUsername())
                                                    .percentage(listPercentage.stream().map(p -> p.getPercentage()).reduce(0.0f,
                                                            Float::sum))
                                                    .discounts(listPercentage.stream()
                                                            .map(p -> new DiscountDto(p.getId(), p.getName(), p.getPercentage(),
                                                                    p.isApplyToReservation(), p.isApplyToFood()))
                                                            .toList())
                                                    .build());
                                        });
                            });
                           

                });
    }

    @Override
    public Mono<List<MembershipDto>> loadAllMemberships(int userId) {
        return this.userClientRepository.findById(userId)
                .flatMap(userData -> this.loadMembershipsInsortInclub(userData.getUsername()));

    }

}
