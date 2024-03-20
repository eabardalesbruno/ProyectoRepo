package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.ComfortTypeEntity;
import com.proriberaapp.ribera.services.ComfortTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
    public Mono<ComfortTypeEntity> getComfortTypeById(
            @RequestParam Integer id) {
        return comfortTypeService.findById(id);
    }

    @PostMapping("/register")
    public Mono<ComfortTypeEntity> registerComfortType(
            @RequestBody ComfortTypeEntity comfortTypeEntity
    ) {
        return comfortTypeService.save(comfortTypeEntity);
    }

    @PostMapping("/register/all")
    public Flux<ComfortTypeEntity> registerAllComfortTypes(
            @RequestBody List<ComfortTypeEntity> comfortTypeEntity
    ) {
        return comfortTypeService.saveAll(comfortTypeEntity);
    }

    @PatchMapping("/update")
    public Mono<ComfortTypeEntity> updateComfortType(
            @RequestBody ComfortTypeEntity comfortTypeEntity
    ) {
        return comfortTypeService.update(comfortTypeEntity);
    }

    @DeleteMapping("/delete")
    public Mono<Void> deleteComfortType(@RequestParam Integer id) {
        return comfortTypeService.deleteById(id);
    }

}
