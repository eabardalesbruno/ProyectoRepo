package com.proriberaapp.ribera.Infraestructure.repository.Invoice;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proriberaapp.ribera.Domain.entities.CompanyEntity;

import reactor.core.publisher.Mono;

public interface CompanyRepository extends R2dbcRepository<CompanyEntity, Integer> {
    Mono<CompanyEntity> findByRuc(String ruc);

}
