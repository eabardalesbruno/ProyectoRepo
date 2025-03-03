package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.request.WalletPointRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.WalletPointResponse;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.services.client.WalletPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/wallet-points")
@RequiredArgsConstructor
public class WalletPointController {
    private final WalletPointService walletPointService;
    private final JwtProvider jtp;

    @PostMapping
    public Mono<ResponseEntity<WalletPointResponse>> createWalletPoint(
            @RequestBody WalletPointRequest walletPointRequest, @RequestHeader("Authorization") String token) {
        System.out.println(walletPointRequest);
        walletPointRequest.setUserId(jtp.getIdFromToken(token));
        return walletPointService.createWalletPoint(walletPointRequest)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }
}

