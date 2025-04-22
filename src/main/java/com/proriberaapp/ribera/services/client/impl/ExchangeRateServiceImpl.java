package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.ExternalAuthService;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseInclubLoginDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.BoResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.ExchangeRateResponse;
import com.proriberaapp.ribera.services.client.ExchangeRateService;
import com.proriberaapp.ribera.utils.constants.ExchangeRateCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final WebClient webClient;

    @Value("${inclub.api.url.admin}")
    private String urlAdminInclub;

    @Override
    public Mono<ExchangeRateResponse> getExchangeRateByCode(ExchangeRateCode exchangeRateCode) {
        return webClient.get()
                .uri(urlAdminInclub + "/exchangerate/")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BoResponse<ExchangeRateResponse>>(){})
                .map(BoResponse::getData);
    }
}
