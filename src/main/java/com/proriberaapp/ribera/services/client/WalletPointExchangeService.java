package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.PointRedemptionHistoryDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.ExchangeHistoryResponse;
import reactor.core.publisher.Mono;

import java.nio.channels.FileChannel;
import java.util.List;

public interface WalletPointExchangeService {

    Mono<List<PointRedemptionHistoryDto>> getExchangeHistoryByUserId(Integer userId);

    Mono<List<ExchangeHistoryResponse>>  getExchangeHistoryByUsername(String username);
}
