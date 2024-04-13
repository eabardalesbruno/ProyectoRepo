package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.ServicesEntity;
import com.proriberaapp.ribera.services.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/services")
public class ServicesController {
    private final ServicesService servicesService;

    @Autowired
    public ServicesController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }

    @PostMapping
    public Mono<ResponseEntity<ServicesEntity>> createService(@RequestBody ServicesEntity service) {
        return servicesService.createService(service)
                .map(savedService -> ResponseEntity.status(HttpStatus.CREATED).body(savedService));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ServicesEntity>> getServiceById(@PathVariable Integer id) {
        return servicesService.getServiceById(id)
                .map(service -> ResponseEntity.ok(service))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<ServicesEntity> getAllServices() {
        return servicesService.getAllServices();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ServicesEntity>> updateService(@PathVariable Integer id, @RequestBody ServicesEntity service) {
        return servicesService.updateService(id, service)
                .map(updatedService -> ResponseEntity.ok(updatedService))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteService(@PathVariable Integer id) {
        return servicesService.deleteService(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
