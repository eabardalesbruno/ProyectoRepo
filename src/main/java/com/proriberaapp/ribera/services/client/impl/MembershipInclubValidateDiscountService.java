package com.proriberaapp.ribera.services.client.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.MembershipDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseDataMembershipDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseInclubLoginDto;
import com.proriberaapp.ribera.Infraestructure.repository.DiscountRepository;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.services.client.LoadMembershipsService;
import com.proriberaapp.ribera.services.client.VerifiedDiscountService;

import reactor.core.publisher.Mono;

@Service
public class MembershipInclubValidateDiscountService implements VerifiedDiscountService, LoadMembershipsService {

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
                .flatMap(s -> Mono.just(s.stream().filter(p -> p.getIdStatus() == 1).toList()));

    }

    private Mono<List<MembershipDto>> loadMembershipsInsortInclub(String username) {
        return this.loadDataUserRiber(username).flatMap(user -> {
            String uri = URL_MEMBERSHIPS.concat("/").concat(String.valueOf(user.getData().getId()));
            return Mono.just(uri);
        }).flatMap(uri -> {
            WebClient webClient = WebClient.create(uri);
            return webClient.get()
                    .retrieve()
                    .bodyToMono(ResponseDataMembershipDto.class)
                    .flatMap(response -> {
                        List<MembershipDto> data = response.getData().stream()
                                .filter(p -> p.getIdFamilyPackage() == 2).toList();
                        return data.size() > 0 ? Mono.just(data) : Mono.empty();
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
                .switchIfEmpty(Mono.error(new RuntimeException("Error al cargar datos de usuario")));

    }

    @Override
    public Mono<Float> verifiedPercentajeDiscount(int userId) {
        return this.userClientRepository.findById(userId)
                .flatMap(userData -> {
                    return this.loadMembershipsInsortInclub(
                            userData.getUsername())
                            .flatMap(memberships -> {
                                return this.discountRepository
                                        .getDiscountWithItemsAndCurrentYear(userId,
                                                memberships.stream().map(d -> d.getIdPackage()).toList());
                            }).map(discountBd -> discountBd.getPercentage())
                            .switchIfEmpty(Mono.just(0f));

                });
    }

    @Override
    public Mono<List<MembershipDto>> loadAllMemberships(int userId) {
        return this.userClientRepository.findById(userId)
                .flatMap(userData -> this.loadMembershipsInsortInclub(userData.getUsername()));

    }

}
