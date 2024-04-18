package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.CountryEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CountryRepository extends ReactiveCrudRepository<CountryEntity, Integer> {

    @Query("SELECT * FROM country ORDER BY countryid ASC")
    Flux<CountryEntity> findAllOrderedByCountryId();
}