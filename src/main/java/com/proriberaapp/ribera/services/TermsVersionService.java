package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Domain.entities.TermsVersionEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TermsVersionService {
    Mono<TermsVersionEntity> createTermsVersion(TermsVersionEntity termsVersion);
    Flux<TermsVersionEntity> getAllTermsVersions();
    Mono<TermsVersionEntity> getTermsVersionById(Integer id);
    // Otros métodos CRUD según sea necesario
}
