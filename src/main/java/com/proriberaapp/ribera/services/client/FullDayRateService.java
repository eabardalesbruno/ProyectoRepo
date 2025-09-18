package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.FullDayRateDto;
import com.proriberaapp.ribera.Domain.entities.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FullDayRateService {

    Mono<Long> countAll();
    Flux<FullDayRateDto> getRatesAll();
    Flux<FullDayRateDto> searchRates(String title, Boolean rate_status, String rateType);
    Mono<FullDayRateDto> save(FullDayRateDto fullDayRateDto);
    Mono<FullDayRateDto> update(FullDayRateDto fullDayRateDto, Integer id);
    Mono<Boolean> deleteById(Integer id);
}
