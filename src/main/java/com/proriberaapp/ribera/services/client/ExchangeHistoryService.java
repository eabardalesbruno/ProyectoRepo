package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.request.ExchangeHistoryRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.HistoricalExchangeResponse;
import com.proriberaapp.ribera.Domain.entities.ExchangeHistoryEntity;
import reactor.core.publisher.Mono;

public interface ExchangeHistoryService {

    Mono<HistoricalExchangeResponse> findByUserId(Integer userId, String startDate, String endDate, String exchangeType,
                                                  String serviceType, Integer size, Integer page);

    Mono<HistoricalExchangeResponse> findByUsername(String username, String startDate, String endDate,
                                                    String exchangeType, String serviceType, Integer size, Integer page);

    Mono<ExchangeHistoryEntity> createExchangeHistory(ExchangeHistoryRequest request);
}
