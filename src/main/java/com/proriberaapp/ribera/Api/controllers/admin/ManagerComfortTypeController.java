package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.ComfortTypeEntity;
import com.proriberaapp.ribera.Infraestructure.services.ComfortTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/admin/manager/comfort-type")
@RequiredArgsConstructor
public class ManagerComfortTypeController {
    private final ComfortTypeService comfortTypeService;

    @GetMapping("/find/all")
    public Flux<ComfortTypeEntity> getAllComfortTypes() {
        return comfortTypeService.findAll();
    }

    @GetMapping("/find")
    public Mono<ComfortTypeEntity> getComfortTypeById(Integer id) {
        return comfortTypeService.findById(id);
    }

    @PostMapping("/register")
    public Mono<ComfortTypeEntity> registerComfortType() {
        return comfortTypeService.save(null);
    }

    @PostMapping("/register/all")
    public Flux<ComfortTypeEntity> registerAllComfortTypes() {
        return comfortTypeService.saveAll(null);
    }

    @PatchMapping("/update")
    public Mono<ComfortTypeEntity> updateComfortType() {
        return comfortTypeService.update(null);
    }

    @DeleteMapping("/delete")
    public Mono<Void> deleteComfortType(Integer id) {
        return comfortTypeService.deleteById(id);
    }

}
