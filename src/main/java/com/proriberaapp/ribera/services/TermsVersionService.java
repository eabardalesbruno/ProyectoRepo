package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Domain.entities.TermsVersionEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TermsVersionService {
    Mono<TermsVersionEntity> createTermsVersion(TermsVersionEntity termsVersion);
    Mono<TermsVersionEntity> getTermsVersion(Integer versionId);
    Flux<TermsVersionEntity> getAllTermsVersions();
    Mono<TermsVersionEntity> updateTermsVersion(Integer versionId, TermsVersionEntity termsVersion);
    Mono<Void> deleteTermsVersion(Integer versionId);
}
