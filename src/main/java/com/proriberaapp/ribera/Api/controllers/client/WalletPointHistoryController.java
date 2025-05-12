package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.response.WalletPointsHistoryResponse;
import com.proriberaapp.ribera.services.client.WalletPointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/wallet-points-history")
@RequiredArgsConstructor
public class WalletPointHistoryController {

    private final WalletPointHistoryService walletPointHistoryService;

    @GetMapping("/find/user/{userId}")
    public Mono<ResponseEntity<WalletPointsHistoryResponse>> findPointsHistoryByUserId(
            @PathVariable Integer userId, @RequestParam String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam Integer limit, @RequestParam Integer offset) {
        return walletPointHistoryService.findPointsHistoryByUserId(userId, startDate, endDate, limit, offset)
                .map(ResponseEntity::ok);
    }
}
