package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.response.WalletPointsHistoryResponse;
import reactor.core.publisher.Mono;

public interface WalletPointHistoryService {
    /*
        Flux<WalletPointHistoryDto> findPointsHistoryByUserId(
                Integer userId, String startDate, String endDate, Integer limit, Integer offset);
        */
    Mono<WalletPointsHistoryResponse> findPointsHistoryByUserId(
            Integer userId, String startDate, String endDate, Integer limit, Integer offset);
}
