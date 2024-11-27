package com.proriberaapp.ribera.services.client.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.MembershipDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseDataMembershipDto;
import com.proriberaapp.ribera.Infraestructure.repository.DiscountRepository;
import com.proriberaapp.ribera.services.client.VerifiedDiscountService;

import reactor.core.publisher.Mono;

@Service
public class MembershipValidateDiscountService implements VerifiedDiscountService {

    @Value("${inclub.api.url.subscriptions}")
    private String URL_MEMBERSHIPS;
    @Autowired
    private DiscountRepository discountRepository;

    private Mono<List<MembershipDto>> loadMemberships(int userId) {
        String uri = URL_MEMBERSHIPS.concat("/").concat(String.valueOf(userId));
        WebClient webClient = WebClient.create(uri);
        return webClient.get()
                .retrieve()
                .bodyToMono(ResponseDataMembershipDto.class)
                .flatMap(response -> {
                    return Mono.just(response.getData().stream().filter(p -> p.getIdFamilyPackage() == 2).toList());
                });

    }

    @Override
    public Mono<Float> verifiedPercentajeDiscount(int userId) {
        Mono<Float> discount = loadMemberships(userId)
                .flatMap(memberships -> {
                    return this.discountRepository
                            .getPercentajeWithItemsDiscount(
                                    memberships.stream().map(d -> d.getIdFamilyPackage()).toList());
                }).map(discountBd -> discountBd.getPercentage());

        return discount;
    }

}
