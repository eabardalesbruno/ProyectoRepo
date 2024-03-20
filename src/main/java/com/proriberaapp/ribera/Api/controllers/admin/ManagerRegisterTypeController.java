package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.RegisterTypeRequest;
import com.proriberaapp.ribera.Domain.entities.RegisterTypeEntity;
import com.proriberaapp.ribera.services.RegisterTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manager/register-type")
@RequiredArgsConstructor
public class ManagerRegisterTypeController {
    private final RegisterTypeService registerTypeService;

    @PostMapping("/register")
    public Mono<RegisterTypeEntity> registerRegisterType(
            @RequestBody RegisterTypeRequest registerTypeEntity) {
        return registerTypeService.save(registerTypeEntity);
    }

    @PostMapping("/register/all")
    public Flux<RegisterTypeEntity> registerAllRegisterTypes(
            @RequestBody List<RegisterTypeRequest> registerTypeRequest) {
        return registerTypeService.saveAll(registerTypeRequest);
    }

    @PostMapping("/update")
    public Mono<RegisterTypeEntity> updateRegisterType(
            @RequestBody RegisterTypeRequest registerTypeRequest
    ) {
        return registerTypeService.update(registerTypeRequest);
    }

    @PostMapping("/delete")
    public Mono<Void> deleteRegisterType(
            @RequestParam Integer id
    ) {
        return registerTypeService.deleteById(id);
    }

    @GetMapping("/find")
    public Mono<RegisterTypeEntity> findRegisterType(
            @RequestParam Integer id
    ) {
        return registerTypeService.findById(id);
    }

    @GetMapping("/find/all")
    public Flux<RegisterTypeEntity> findAllRegisterTypes() {
        return registerTypeService.findAll();
    }
}
