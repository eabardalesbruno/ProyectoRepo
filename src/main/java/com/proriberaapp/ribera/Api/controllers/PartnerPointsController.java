package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.entities.PartnerPointsEntity;
import com.proriberaapp.ribera.services.PartnerPointsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/partner-points")
@RequiredArgsConstructor
@Slf4j
public class PartnerPointsController {
    private final JwtProvider jtp;
    private final PartnerPointsService partnerPointsService;
    @GetMapping("/find")
    public Mono<PartnerPointsEntity> getAllPartnerPoints(
            @RequestHeader("Authorization") String token) {
        Integer userClientId = jtp.getIdFromToken(token);
        log.info("Getting all partner points");
        return partnerPointsService.getPartnerPointsByUserClientId(userClientId);
    }
}
