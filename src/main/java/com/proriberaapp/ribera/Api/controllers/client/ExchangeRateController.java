package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.response.ExchangeRateResponse;
import com.proriberaapp.ribera.services.client.ExchangeRateService;
import com.proriberaapp.ribera.utils.constants.ExchangeRateCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/exchange-rate")
@RequiredArgsConstructor
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    @GetMapping
    public Mono<ExchangeRateResponse> getExchangeRate() {
        return exchangeRateService.getExchangeRateByCode(ExchangeRateCode.USD);
    }
}
