package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.response.ExchangeRateResponse;
import com.proriberaapp.ribera.utils.constants.ExchangeRateCode;
import reactor.core.publisher.Mono;

public interface ExchangeRateService {
    Mono<ExchangeRateResponse> getExchangeRateByCode(ExchangeRateCode exchangeRateCode);
}
