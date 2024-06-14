package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.TermsVersionEntity;
import com.proriberaapp.ribera.services.TermsVersionService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api/termsversions")
public class TermsVersionController {
    private final TermsVersionService termsVersionService;

    public TermsVersionController(TermsVersionService termsVersionService) {
        this.termsVersionService = termsVersionService;
    }

    @PostMapping
    public Mono<TermsVersionEntity> createTermsVersion(@RequestBody TermsVersionEntity termsVersion) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        termsVersion.setCreatedAt(timestamp);
        return termsVersionService.createTermsVersion(termsVersion);
    }

    @GetMapping
    public Flux<TermsVersionEntity> getAllTermsVersions() {
        return termsVersionService.getAllTermsVersions();
    }

    @GetMapping("/{id}")
    public Mono<TermsVersionEntity> getTermsVersionById(@PathVariable Integer id) {
        return termsVersionService.getTermsVersionById(id);
    }
}
