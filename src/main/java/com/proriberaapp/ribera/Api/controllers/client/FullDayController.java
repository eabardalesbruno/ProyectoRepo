package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.FullDayRequest;
import com.proriberaapp.ribera.Domain.entities.FullDayEntity;
import com.proriberaapp.ribera.Domain.entities.FullDayTypeFoodEntity;
import com.proriberaapp.ribera.services.client.FullDayService;
import com.proriberaapp.ribera.services.client.FullDayTypeFoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/fullday")
@RequiredArgsConstructor
public class FullDayController {

    private final FullDayService fullDayService;

    private final FullDayTypeFoodService fullDayTypeFoodService;

    //FULLDAY CONTROLLER
    @PostMapping("/register")
    public Mono<ResponseEntity<FullDayEntity>> registerFullDay(@RequestBody FullDayRequest request) {
        return fullDayService.registerFullDay(
                        request.getReceptionistId(),
                        request.getUserPromoterId(),
                        request.getUserClientId(),
                        request.getType(),
                        request.getDetails(),
                        request.getFoods()
                )
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    //FULLDAY TYPE FOOD CONTROLLER

    @GetMapping("/typeFoodall")
    public Flux<FullDayTypeFoodEntity> getAllFullDayTypeFood() {
        return fullDayTypeFoodService.getAllFullDayTypeFood();
    }

    @GetMapping("/typeFoodTypes")
    public Mono<Map<String, Object>> searchByTypeAndName(@RequestParam(required = false) String type, @RequestParam(required = false) String name, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        Flux<FullDayTypeFoodEntity> dataFlux = fullDayTypeFoodService.getFullDayTypeFoodByType(type, name, page, size);
        Mono<Integer> totalMono = fullDayTypeFoodService.getTotalFullDayTypeFood(type, name);

        return totalMono.zipWith(dataFlux.collectList())
                .map(tuple -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("total", tuple.getT1());
                    response.put("page", page);
                    response.put("size", size);
                    response.put("data", tuple.getT2());
                    return response;
                });
    }

    @GetMapping("/typeFood/{id}")
    public Mono<FullDayTypeFoodEntity> getFullDayTypeFoodById(@PathVariable Integer id) {
        return fullDayTypeFoodService.getFullDayTypeFoodById(id);
    }

    @PostMapping("/typeFoodSave")
    public Mono<FullDayTypeFoodEntity> createFullDayTypeFood(@RequestBody FullDayTypeFoodEntity fullDayTypeFoodEntity) {
        return fullDayTypeFoodService.saveFullDayTypeFood(fullDayTypeFoodEntity);
    }

    @PutMapping("/typeFoodUp/{id}")
    public Mono<FullDayTypeFoodEntity> updateFullDayTypeFood(@PathVariable Integer id, @RequestBody FullDayTypeFoodEntity fullDayTypeFoodEntity) {
        return fullDayTypeFoodService.updateFullDayTypeFood(id, fullDayTypeFoodEntity);
    }

    @DeleteMapping("/typeFoodDl/{id}")
    public Mono<Void> deleteFullDayTypeFood(@PathVariable Integer id) {
        return fullDayTypeFoodService.deleteFullDayTypeFood(id);
    }

}
