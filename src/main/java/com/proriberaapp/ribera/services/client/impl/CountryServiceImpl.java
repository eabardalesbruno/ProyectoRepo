package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.CountryEntity;
import com.proriberaapp.ribera.Infraestructure.repository.CountryRepository;
import com.proriberaapp.ribera.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Mono<CountryEntity> getCountryById(Integer id) {
        return countryRepository.findById(id);
    }

    @Override
    public Flux<CountryEntity> getAllCountries() {
        return countryRepository.findAllOrderedByCountryId();
    }

    @Override
    public Mono<CountryEntity> saveCountry(CountryEntity country) {
        return countryRepository.save(country);
    }

    @Override
    public Mono<Void> deleteCountry(Integer id) {
        return countryRepository.deleteById(id);
    }
}