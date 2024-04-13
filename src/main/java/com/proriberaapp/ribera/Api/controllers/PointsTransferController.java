package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.PointsTransferEntity;
import com.proriberaapp.ribera.services.PointsTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/points-transfers")
public class PointsTransferController {
    private final PointsTransferService pointsTransferService;

    @Autowired
    public PointsTransferController(PointsTransferService pointsTransferService) {
        this.pointsTransferService = pointsTransferService;
    }

    @PostMapping
    public Mono<ResponseEntity<PointsTransferEntity>> createPointsTransfer(@RequestBody PointsTransferEntity pointsTransfer) {
        return pointsTransferService.createPointsTransfer(pointsTransfer)
                .map(savedPointsTransfer -> ResponseEntity.status(HttpStatus.CREATED).body(savedPointsTransfer));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<PointsTransferEntity>> getPointsTransferById(@PathVariable Integer id) {
        return pointsTransferService.getPointsTransferById(id)
                .map(pointsTransfer -> ResponseEntity.ok(pointsTransfer))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<PointsTransferEntity> getAllPointsTransfers() {
        return pointsTransferService.getAllPointsTransfers();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<PointsTransferEntity>> updatePointsTransfer(@PathVariable Integer id, @RequestBody PointsTransferEntity pointsTransfer) {
        return pointsTransferService.updatePointsTransfer(id, pointsTransfer)
                .map(updatedPointsTransfer -> ResponseEntity.ok(updatedPointsTransfer))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePointsTransfer(@PathVariable Integer id) {
        return pointsTransferService.deletePointsTransfer(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}