package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.CountryEntity;
import com.proriberaapp.ribera.services.client.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CountryEntity>> getCountryById(@PathVariable Integer id) {
        return countryService.getCountryById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<CountryEntity> getAllCountries() {
        return countryService.getAllCountries();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CountryEntity> createCountry(@RequestBody CountryEntity country) {
        return countryService.saveCountry(country);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCountry(@PathVariable Integer id) {
        return countryService.deleteCountry(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}