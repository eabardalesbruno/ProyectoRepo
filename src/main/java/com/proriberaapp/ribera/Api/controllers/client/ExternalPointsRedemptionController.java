package com.proriberaapp.ribera.Api.controllers.client;


import com.proriberaapp.ribera.Api.controllers.client.dto.request.PointsRedemptionHistoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import com.proriberaapp.ribera.services.client.impl.MembershipInclubValidateDiscountService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/external-redemption")
public class ExternalPointsRedemptionController {

    private final MembershipInclubValidateDiscountService membershipInclubValidateDiscountService;

    @GetMapping("/user/{idUser}")
    public Mono<Object> getRedemptionHistoryByUser(
            @PathVariable int idUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return membershipInclubValidateDiscountService.getPointsRedemptionHistoryByUser(idUser, page, size);
    }


    @PostMapping
    public Mono<Object> createPointsRedemptionHistory(@RequestBody PointsRedemptionHistoryRequest request) {
        return membershipInclubValidateDiscountService.createPointsRedemptionHistory(request);
    }
}