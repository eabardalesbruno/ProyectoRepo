package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.TermsVersionEntity;
import com.proriberaapp.ribera.Infraestructure.repository.TermsVersionRepository;
import com.proriberaapp.ribera.services.client.TermsVersionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TermsVersionServiceImpl implements TermsVersionService {
    private final TermsVersionRepository termsVersionRepository;

    public TermsVersionServiceImpl(TermsVersionRepository termsVersionRepository) {
        this.termsVersionRepository = termsVersionRepository;
    }

    @Override
    public Mono<TermsVersionEntity> createTermsVersion(TermsVersionEntity termsVersion) {
        return termsVersionRepository.save(termsVersion);
    }

    @Override
    public Flux<TermsVersionEntity> getAllTermsVersions() {
        return termsVersionRepository.findAll();
    }

    @Override
    public Mono<TermsVersionEntity> getTermsVersionById(Integer id) {
        return termsVersionRepository.findById(id);
    }
}
