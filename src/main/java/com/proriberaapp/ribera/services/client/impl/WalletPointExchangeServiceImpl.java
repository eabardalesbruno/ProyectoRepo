package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.ExternalAuthService;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseInclubLoginDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.PointRedemptionHistoryDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.BoResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.ExchangeHistoryResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.PointRedemptionResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.UserPointDataResponse;
import com.proriberaapp.ribera.services.client.WalletPointExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletPointExchangeServiceImpl implements WalletPointExchangeService {
    @Value("${backoffice.api.url}")
    private String urlBackOffice;

    @Value("${inclub.api.url.user}")
    private String urlLoginUserInclub;

    private final WebClient webClient;
    private final ExternalAuthService externalAuthService;

    @Override
    public Mono<List<PointRedemptionHistoryDto>> getExchangeHistoryByUserId(Integer userId) {
        String baseUrl = urlBackOffice + "/user-points-released/points-redemption-history/user/" + userId;
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

    @Override
    public Mono<List<ExchangeHistoryResponse>> getExchangeHistoryByUsername(String username) {
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
                            .uri(urlBackOffice + "/user-points-released/points-exchange-history/user/" + responseInclub.getData().getId()+"?page=0&size=100")
                            .header("Authorization", "Bearer " + tokenBackOffice)
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<BoResponse<List<ExchangeHistoryResponse>>>() {})
                            .map(BoResponse::getData);
                });



    }
}
