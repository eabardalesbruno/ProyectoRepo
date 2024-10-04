package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.ComfortTypeEntity;
import com.proriberaapp.ribera.services.client.ComfortTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/comfort-type")
@RequiredArgsConstructor
public class ManagerComfortTypeController extends BaseManagerController<ComfortTypeEntity, ComfortTypeEntity> {
    private final ComfortTypeService comfortTypeService;

    @GetMapping
    public Flux<ComfortTypeEntity> findAll() {
        return comfortTypeService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ComfortTypeEntity> findById(@PathVariable Integer id) {
        return comfortTypeService.findById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable Integer id) {
        return comfortTypeService.deleteComforTypeById(id);
    }

    @PostMapping
    public Mono<ComfortTypeEntity> save(@RequestBody ComfortTypeEntity comfortTypeEntity) {
        return comfortTypeService.save(comfortTypeEntity);
    }

    @PutMapping("/{id}")
    public Mono<ComfortTypeEntity> update(@PathVariable Integer id, @RequestBody ComfortTypeEntity comfortTypeEntity) {
        comfortTypeEntity.setComfortTypeId(id);
        return comfortTypeService.update(comfortTypeEntity);
    }
}
