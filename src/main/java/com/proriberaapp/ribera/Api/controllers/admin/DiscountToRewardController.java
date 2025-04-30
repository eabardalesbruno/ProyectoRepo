package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.DiscountToRewardEntity;
import com.proriberaapp.ribera.services.admin.DiscountToRewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/maintenance/discountToReward")
@RequiredArgsConstructor
public class DiscountToRewardController {

    private final DiscountToRewardService discountToRewardService;

    @GetMapping("/all")
    public Flux<DiscountToRewardEntity> getAllDiscounts() {
        return discountToRewardService.getAllDiscount();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<DiscountToRewardEntity>> getDiscountById(@PathVariable Integer id) {
        return discountToRewardService.getDiscountById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
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

    @DeleteMapping("/delete/{id}")
    public Mono<Void> deleteDiscount(@PathVariable Integer id) {
        return discountToRewardService.deleteDiscount(id);
    }
}
