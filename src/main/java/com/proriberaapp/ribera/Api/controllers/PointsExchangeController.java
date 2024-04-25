package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.PointsExchangeEntity;
import com.proriberaapp.ribera.services.PointsExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/points-exchanges")
public class PointsExchangeController {
    private final PointsExchangeService pointsExchangeService;

    @Autowired
    public PointsExchangeController(PointsExchangeService pointsExchangeService) {
        this.pointsExchangeService = pointsExchangeService;
    }

    @PostMapping
    public Mono<ResponseEntity<PointsExchangeEntity>> createPointsExchange(@RequestBody PointsExchangeEntity pointsExchange) {
        return pointsExchangeService.createPointsExchange(pointsExchange)
                .map(savedPointsExchange -> ResponseEntity.status(HttpStatus.CREATED).body(savedPointsExchange));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<PointsExchangeEntity>> getPointsExchangeById(@PathVariable Integer id) {
        return pointsExchangeService.getPointsExchangeById(id)
                .map(pointsExchange -> ResponseEntity.ok(pointsExchange))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<PointsExchangeEntity> getAllPointsExchanges() {
        return pointsExchangeService.getAllPointsExchanges();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<PointsExchangeEntity>> updatePointsExchange(@PathVariable Integer id, @RequestBody PointsExchangeEntity pointsExchange) {
        return pointsExchangeService.updatePointsExchange(id, pointsExchange)
                .map(updatedPointsExchange -> ResponseEntity.ok(updatedPointsExchange))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePointsExchange(@PathVariable Integer id) {
        return pointsExchangeService.deletePointsExchange(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}