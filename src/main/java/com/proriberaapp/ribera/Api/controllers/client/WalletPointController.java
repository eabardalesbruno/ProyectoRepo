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
        walletPointRequest.setUserId(jtp.getIdFromToken(token));
        return walletPointService.createWalletPoint(walletPointRequest)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping
    public Mono<ResponseEntity<WalletPointResponse>> getWalletPoints(@RequestHeader("Authorization") String token) {
        Integer userId = jtp.getIdFromToken(token);
        return walletPointService.getWalletByUserId(userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping
    public Mono<ResponseEntity<WalletPointResponse>> updateWalletPoints(
            @RequestBody WalletPointRequest request, @RequestHeader("Authorization") String token) {
        Integer userId = jtp.getIdFromToken(token);
        return walletPointService.updateWalletPoints(userId, request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("/buy")
    public Mono<ResponseEntity<WalletPointResponse>> buyPoints(@RequestBody WalletPointRequest request, @RequestHeader("Authorization") String token) {
        Integer userId = jtp.getIdFromToken(token);
        return walletPointService.buyPoints(userId, request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }
}


