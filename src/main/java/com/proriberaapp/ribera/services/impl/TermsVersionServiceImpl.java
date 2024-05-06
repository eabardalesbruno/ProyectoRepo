package com.proriberaapp.ribera.services.impl;

import com.proriberaapp.ribera.Domain.entities.TermsVersionEntity;
import com.proriberaapp.ribera.Infraestructure.repository.TermsVersionRepository;
import com.proriberaapp.ribera.services.TermsVersionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TermsVersionServiceImpl implements TermsVersionService {

    private final TermsVersionRepository repository;

    public TermsVersionServiceImpl(TermsVersionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<TermsVersionEntity> createTermsVersion(TermsVersionEntity termsVersion) {
        return repository.save(termsVersion);
    }

    @Override
    public Mono<TermsVersionEntity> getTermsVersion(Integer versionId) {
        return repository.findById(versionId);
    }

    @Override
    public Flux<TermsVersionEntity> getAllTermsVersions() {
        return repository.findAll();
    }

    @Override
    public Mono<TermsVersionEntity> updateTermsVersion(Integer versionId, TermsVersionEntity termsVersion) {
        return repository.findById(versionId)
                .flatMap(existingVersion -> {
                    existingVersion.setUserClientId(termsVersion.getUserClientId());
                    existingVersion.setS3Url(termsVersion.getS3Url());
                    existingVersion.setActive(termsVersion.getActive());
                    return repository.save(existingVersion);
                });
    }

    @Override
    public Mono<Void> deleteTermsVersion(Integer versionId) {
        return repository.deleteById(versionId);
    }
}
