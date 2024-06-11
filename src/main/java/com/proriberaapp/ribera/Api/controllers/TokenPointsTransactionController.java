package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.TokenPointsTransaction;
import com.proriberaapp.ribera.services.TokenPointsTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/points")
public class TokenPointsTransactionController {

    private final TokenPointsTransactionService tokenPointsTransactionService;

    @Autowired
    public TokenPointsTransactionController(TokenPointsTransactionService tokenPointsTransactionService) {
        this.tokenPointsTransactionService = tokenPointsTransactionService;
    }

    @PostMapping("/create")
    public Mono<TokenPointsTransaction> createToken(@RequestParam Integer partnerPointId, @RequestParam Integer bookingId) {
        return tokenPointsTransactionService.createToken(partnerPointId, bookingId);
    }
}
