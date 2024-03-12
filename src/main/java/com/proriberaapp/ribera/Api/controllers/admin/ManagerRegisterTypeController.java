package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.RegisterTypeEntity;
import com.proriberaapp.ribera.Infraestructure.services.RegisterTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/admin/manager/register-type")
@RequiredArgsConstructor
public class ManagerRegisterTypeController {
    private final RegisterTypeService registerTypeService;

    @PostMapping("/register")
    public Mono<RegisterTypeEntity> registerRegisterType(
            @RequestBody RegisterTypeEntity registerTypeEntity) {
        return registerTypeService.save(registerTypeEntity);
    }

    @PostMapping("/register/all")
    public Flux<RegisterTypeEntity> registerAllRegisterTypes(
            @RequestBody Flux<RegisterTypeEntity> registerTypeEntity) {
        return registerTypeService.saveAll(registerTypeEntity);
    }

    @PostMapping("/update")
    public Mono<RegisterTypeEntity> updateRegisterType(
            @RequestBody RegisterTypeEntity registerTypeEntity
    ) {
        return registerTypeService.update(registerTypeEntity);
    }

    @PostMapping("/delete")
    public Mono<Void> deleteRegisterType(
            @RequestParam String id
    ) {
        return registerTypeService.deleteById(id);
    }

    @GetMapping("/find")
    public Mono<RegisterTypeEntity> findRegisterType(
            @RequestParam String id
    ) {
        return registerTypeService.findById(id);
    }

    @GetMapping("/find/all")
    public Flux<RegisterTypeEntity> findAllRegisterTypes() {
        return registerTypeService.findAll();
    }
}
