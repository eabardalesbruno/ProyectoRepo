package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.TermsVersionEntity;
import com.proriberaapp.ribera.services.TermsVersionService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/termsversions")
public class TermsVersionController {

    private final TermsVersionService service;

    public TermsVersionController(TermsVersionService service) {
        this.service = service;
    }

    @PostMapping
    public Mono<TermsVersionEntity> createTermsVersion(@RequestBody TermsVersionEntity termsVersion) {
        return service.createTermsVersion(termsVersion);
    }

    @GetMapping("/{versionId}")
    public Mono<TermsVersionEntity> getTermsVersion(@PathVariable Integer versionId) {
        return service.getTermsVersion(versionId);
    }

    @GetMapping
    public Flux<TermsVersionEntity> getAllTermsVersions() {
        return service.getAllTermsVersions();
    }

    @PutMapping("/{versionId}")
    public Mono<TermsVersionEntity> updateTermsVersion(@PathVariable Integer versionId, @RequestBody TermsVersionEntity termsVersion) {
        return service.updateTermsVersion(versionId, termsVersion);
    }

    @DeleteMapping("/{versionId}")
    public Mono<Void> deleteTermsVersion(@PathVariable Integer versionId) {
        return service.deleteTermsVersion(versionId);
    }
}
