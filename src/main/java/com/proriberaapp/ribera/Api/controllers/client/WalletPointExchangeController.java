package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.PointRedemptionHistoryDto;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.services.client.WalletPointExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wallet-points-exchange")
@RequiredArgsConstructor
public class WalletPointExchangeController {

    private final JwtProvider jtp;

    private final WalletPointExchangeService pointExchangeService;

    //Backoffice endpoint external
    @GetMapping("/redemption-history-points/{userId}")
    public Mono<ResponseEntity<List<PointRedemptionHistoryDto>>> getInclubRedemptionsHistory(@PathVariable Integer userId){
        //return null;
        return pointExchangeService.getExchangeHistoryByUserId(userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
