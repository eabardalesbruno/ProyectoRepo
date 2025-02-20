package com.proriberaapp.ribera.Api.controllers.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.dto.PointTransactionExchangeDto;
import com.proriberaapp.ribera.services.client.impl.PointTransactionQueryService;

import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/v1/point-transactions")
public class PointTransactionQueryController {
    @Autowired
    private PointTransactionQueryService pointTransactionQueryService;
    @Autowired
    private JwtProvider jtp;

    @GetMapping("/transfer")
    public Flux<?> getTransfer(@RequestHeader("Authorization") String token) {
        Integer userClientId = jtp.getIdFromToken(token);
        return this.pointTransactionQueryService.getPointTransfer(userClientId);
    }

    @GetMapping("/exchange")
    public Flux<PointTransactionExchangeDto> getExchange(@RequestHeader("Authorization") String token) {
        Integer userClientId = jtp.getIdFromToken(token);
        return this.pointTransactionQueryService.getPointExchangeByUserId(userClientId);
    }
}
