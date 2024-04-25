package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.BenefitEntity;
import com.proriberaapp.ribera.services.BenefitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/benefit")
@RequiredArgsConstructor
public class BenefitController {
    private final BenefitService benefitService;

    @GetMapping("/find")
    public Mono<BenefitEntity> findBenefit(@RequestParam Integer id) {
        return benefitService.findById(id);
    }

    @GetMapping("/find/all")
    public Flux<BenefitEntity> findAllBenefits() {
        return benefitService.findAll();
    }
}
