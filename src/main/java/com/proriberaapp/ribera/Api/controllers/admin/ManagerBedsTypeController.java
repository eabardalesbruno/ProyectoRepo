package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.BedsTypeEntity;
import com.proriberaapp.ribera.services.BedsTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
    public Mono<BedsTypeEntity> registerBedsType(
            @RequestBody BedsTypeEntity bedsTypeEntity
    ) {
        return bedsTypeService.save(bedsTypeEntity);
    }

    @PostMapping("/register/all")
    public Flux<BedsTypeEntity> registerAllBedsType(
            @RequestBody List<BedsTypeEntity> bedsTypeEntity) {
        return bedsTypeService.saveAll(bedsTypeEntity);
    }

    @PatchMapping("/update")
    public Mono<BedsTypeEntity> updateBedsType(
            @RequestBody BedsTypeEntity bedsTypeEntity
    ) {
        return bedsTypeService.update(bedsTypeEntity);
    }

    @DeleteMapping("/delete")
    public Mono<Void> deleteBedsType(Integer id) {
        return bedsTypeService.deleteById(id);
    }


}
