package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.FinalCostumerEntity;
import com.proriberaapp.ribera.services.FinalCostumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manager/final-costumer")
@RequiredArgsConstructor
public class ManagerFinalCostumerController {
    private final FinalCostumerService finalCostumerService;

    @GetMapping("/find/all")
    public Flux<FinalCostumerEntity> getAllFinalCostumers() {
        return finalCostumerService.findAll();
    }

    @GetMapping("/find")
    public Mono<FinalCostumerEntity> getFinalCostumerById(
            @RequestParam Integer id) {
        return finalCostumerService.findById(id);
    }

    @PostMapping("/register")
    public Mono<FinalCostumerEntity> registerFinalCostumer(
            @RequestBody FinalCostumerEntity finalCostumerEntity
    ) {
        return finalCostumerService.save(finalCostumerEntity);
    }

    @PostMapping("/register/all")
    public Flux<FinalCostumerEntity> registerAllFinalCostumers(
            @RequestBody List<FinalCostumerEntity> finalCostumerEntity
    ) {
        return finalCostumerService.saveAll(finalCostumerEntity);
    }

    @PatchMapping("/update")
    public Mono<FinalCostumerEntity> updateFinalCostumer(
            @RequestBody FinalCostumerEntity finalCostumerEntity
    ) {
        return finalCostumerService.update(finalCostumerEntity);
    }

    @DeleteMapping("/delete")
    public Mono<Void> deleteFinalCostumer(@RequestParam Integer id) {
        return finalCostumerService.deleteById(id);
    }
}
