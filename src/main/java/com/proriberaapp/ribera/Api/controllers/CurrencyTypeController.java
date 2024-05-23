package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.CurrencyTypeEntity;
import com.proriberaapp.ribera.services.CurrencyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/currencytype")
public class CurrencyTypeController {
    private final CurrencyTypeService currencyTypeService;

    @Autowired
    public CurrencyTypeController(CurrencyTypeService currencyTypeService) {
        this.currencyTypeService = currencyTypeService;
    }

    @PostMapping
    public Mono<ResponseEntity<CurrencyTypeEntity>> createCurrencyType(@RequestBody CurrencyTypeEntity currencyType) {
        return currencyTypeService.createCurrencyType(currencyType)
                .map(createdCurrencyType -> new ResponseEntity<>(createdCurrencyType, HttpStatus.CREATED));
    }

    @GetMapping("/{currencyTypeId}")
    public Mono<ResponseEntity<CurrencyTypeEntity>> getCurrencyType(@PathVariable Integer currencyTypeId) {
        return currencyTypeService.getCurrencyType(currencyTypeId)
                .map(currencyType -> ResponseEntity.ok(currencyType))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<CurrencyTypeEntity> getAllCurrencyTypes() {
        return currencyTypeService.getAllCurrencyTypes();
    }

    @DeleteMapping("/{currencyTypeId}")
    public Mono<ResponseEntity<Void>> deleteCurrencyType(@PathVariable Integer currencyTypeId) {
        return currencyTypeService.deleteCurrencyType(currencyTypeId)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}