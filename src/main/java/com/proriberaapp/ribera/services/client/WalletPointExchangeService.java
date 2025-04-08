package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.PointRedemptionHistoryDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface WalletPointExchangeService {

    Mono<List<PointRedemptionHistoryDto>> getExchangeHistoryByUserId(Integer userId);
}
