package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.PointRedemptionHistoryDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.ExchangeHistoryResponse;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.services.client.WalletPointExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wallet-points-exchange")
@RequiredArgsConstructor
public class WalletPointExchangeController {
    private final JwtProvider jtp;
    private final WalletPointExchangeService pointExchangeService;

    @GetMapping("/redemption-history-points/{userId}")
    public Mono<ResponseEntity<List<PointRedemptionHistoryDto>>> getInclubRedemptionsHistory(@PathVariable Integer userId){
        return pointExchangeService.getExchangeHistoryByUserId(userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/history")
    public Mono<ResponseEntity<List<ExchangeHistoryResponse>>> getExchangeHistoryByIdentifier(@RequestHeader("Authorization") String token) {
        String username = jtp.getUsernameFromToken(token);
        return pointExchangeService.getExchangeHistoryByUsername(username)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
