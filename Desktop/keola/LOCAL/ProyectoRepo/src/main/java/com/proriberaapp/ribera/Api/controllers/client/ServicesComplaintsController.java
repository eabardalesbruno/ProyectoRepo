package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.ServicesComplaintsEntity;
import com.proriberaapp.ribera.services.client.ServicesComplaintsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/services-complaints")
public class ServicesComplaintsController {

    private final ServicesComplaintsService servicesComplaintsService;

    @Autowired
    public ServicesComplaintsController(ServicesComplaintsService servicesComplaintsService) {
        this.servicesComplaintsService = servicesComplaintsService;
    }

    @PostMapping
    public Mono<ServicesComplaintsEntity> create(@RequestBody ServicesComplaintsEntity entity) {
        return servicesComplaintsService.create(entity);
    }

    @PutMapping("/{id}")
    public Mono<ServicesComplaintsEntity> update(@PathVariable Integer id, @RequestBody ServicesComplaintsEntity entity) {
        entity.setServicesComplaintsId(id);
        return servicesComplaintsService.update(entity);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Integer id) {
        return servicesComplaintsService.deleteById(id);
    }

    @GetMapping("/{id}")
    public Mono<ServicesComplaintsEntity> findById(@PathVariable Integer id) {
        return servicesComplaintsService.findById(id);
    }

    @GetMapping
    public Flux<ServicesComplaintsEntity> findAll() {
        return servicesComplaintsService.findAll();
    }
}
