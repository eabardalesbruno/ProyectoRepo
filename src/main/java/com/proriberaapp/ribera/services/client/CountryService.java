package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.CountryEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CountryService {

    Mono<CountryEntity> getCountryById(Integer id);

    Flux<CountryEntity> getAllCountries();

    Mono<CountryEntity> saveCountry(CountryEntity country);

    Mono<Void> deleteCountry(Integer id);
}