package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.request.WalletPointRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.WalletPointResponse;
import reactor.core.publisher.Mono;

public interface WalletPointService {

    Mono<WalletPointResponse> createWalletPoint(WalletPointRequest walletPointRequest);

    Mono<WalletPointResponse> getWalletByUsername(String username, String tokenBackOffice);

    Mono<WalletPointResponse> getWalletByUserId(Integer userId);

    Mono<WalletPointResponse> updateWalletPoints(Integer userId, WalletPointRequest walletPointRequest);

    Mono<WalletPointResponse>  buyPoints(Integer userId, WalletPointRequest walletPointRequest);

    //Mono<WalletPointResponse>  convertPoints(WalletPointRequest walletPointRequest);
    Mono<Void>  convertPoints(Integer userId,WalletPointRequest walletPointRequest);
}
