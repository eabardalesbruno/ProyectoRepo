package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.request.WalletPointRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.WalletPointResponse;
import reactor.core.publisher.Mono;

import java.nio.channels.FileChannel;

public interface WalletPointService {

    Mono<WalletPointResponse> createWalletPoint(WalletPointRequest walletPointRequest);

    Mono<WalletPointResponse> getWalletByUserId(Integer userId);

    Mono<WalletPointResponse> updateWalletPoints(Integer userId, WalletPointRequest walletPointRequest);

    Mono<WalletPointResponse>  buyPoints(Integer userId, WalletPointRequest walletPointRequest);
}
