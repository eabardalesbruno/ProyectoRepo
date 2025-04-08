package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.ExternalAuthService;
import com.proriberaapp.ribera.Api.controllers.client.dto.PointRedemptionHistoryDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.PointRedemptionResponse;
import com.proriberaapp.ribera.services.client.WalletPointExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletPointExchangeServiceImpl implements WalletPointExchangeService {

    @Value("${backoffice.api.url}")
    private String urlBackOffice;

    private final WebClient webClient;
    private final ExternalAuthService externalAuthService;

    @Override
    public Mono<List<PointRedemptionHistoryDto>> getExchangeHistoryByUserId(Integer userId) {
        //String urlBackOffice
        String baseUrl = urlBackOffice + "/user-points-released/points-redemption-history/user/" + userId;
        System.out.println("URL Backoffice: " + baseUrl);
        return externalAuthService.getExternalToken()
                .flatMap(tokenExternal ->  WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + tokenExternal)
                .build()
                .get()
                .retrieve()
                .bodyToMono(PointRedemptionResponse.class)
                .map(PointRedemptionResponse::getData)

        );
    }
}
