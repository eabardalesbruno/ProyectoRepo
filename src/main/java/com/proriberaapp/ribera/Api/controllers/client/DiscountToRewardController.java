package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.DiscountToRewardEntity;
import com.proriberaapp.ribera.services.client.DiscountToRewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/discountToReward")
@RequiredArgsConstructor
public class DiscountToRewardController {
    //ESTO ES EL CRUD PARA IDENTIFICAR QUE PUNTOS REWARD CON RESPECTO A UN PORCENTAJE DETERMINADO DEL MONTO PAGADO DEL CLIENTE TIENEN
    private final DiscountToRewardService discountToRewardService;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<DiscountToRewardEntity>> getDiscountById(@PathVariable Integer id) {
        return discountToRewardService.getDiscountById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public Flux<DiscountToRewardEntity> getAllDiscounts() {
        return discountToRewardService.getAllDiscount();
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<DiscountToRewardEntity> createDiscount(@RequestBody DiscountToRewardEntity discountToReward) {
        return discountToRewardService.saveDiscount(discountToReward);
    }

    @PatchMapping("/update/{id}")
    public Mono<ResponseEntity<DiscountToRewardEntity>> updateDiscount(
            @PathVariable Integer id, @RequestBody DiscountToRewardEntity discountToReward) {
        return discountToRewardService.updateDiscount(id, discountToReward)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
