package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.ExchangeTypeEntity;
import com.proriberaapp.ribera.services.client.ExchangeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/exchange-types")
public class ExchangeTypeController {
    private final ExchangeTypeService exchangeTypeService;

    @Autowired
    public ExchangeTypeController(ExchangeTypeService exchangeTypeService) {
        this.exchangeTypeService = exchangeTypeService;
    }

    @PostMapping
    public Mono<ResponseEntity<ExchangeTypeEntity>> createExchangeType(@RequestBody ExchangeTypeEntity exchangeType) {
        return exchangeTypeService.createExchangeType(exchangeType)
                .map(savedExchangeType -> ResponseEntity.status(HttpStatus.CREATED).body(savedExchangeType));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ExchangeTypeEntity>> getExchangeTypeById(@PathVariable Integer id) {
        return exchangeTypeService.getExchangeTypeById(id)
                .map(exchangeType -> ResponseEntity.ok(exchangeType))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<ExchangeTypeEntity> getAllExchangeTypes() {
        return exchangeTypeService.getAllExchangeTypes();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ExchangeTypeEntity>> updateExchangeType(@PathVariable Integer id, @RequestBody ExchangeTypeEntity exchangeType) {
        return exchangeTypeService.updateExchangeType(id, exchangeType)
                .map(updatedExchangeType -> ResponseEntity.ok(updatedExchangeType))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteExchangeType(@PathVariable Integer id) {
        return exchangeTypeService.deleteExchangeType(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
