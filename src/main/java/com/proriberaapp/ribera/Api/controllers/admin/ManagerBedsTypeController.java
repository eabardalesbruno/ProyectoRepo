package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.BedsTypeEntity;
import com.proriberaapp.ribera.Infraestructure.services.BedsTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/admin/manager/beds-type")
@RequiredArgsConstructor
public class ManagerBedsTypeController {
    private final BedsTypeService bedsTypeService;

    @GetMapping("/find/all")
    public Flux<BedsTypeEntity> getAllBedsType() {
        return bedsTypeService.findAll();
    }

    @GetMapping("/find")
    public Mono<BedsTypeEntity> getBedsTypeById(Integer id) {
        return bedsTypeService.findById(id);
    }

    @PostMapping("/register")
    public Mono<BedsTypeEntity> registerBedsType() {
        return bedsTypeService.save(null);
    }

    @PostMapping("/register/all")
    public Flux<BedsTypeEntity> registerAllBedsType() {
        return bedsTypeService.saveAll(null);
    }

    @PatchMapping("/update")
    public Mono<BedsTypeEntity> updateBedsType() {
        return bedsTypeService.update(null);
    }

    @DeleteMapping("/delete")
    public Mono<Void> deleteBedsType(Integer id) {
        return bedsTypeService.deleteById(id);
    }


}
