package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.FullDayRequest;
import com.proriberaapp.ribera.Domain.entities.FullDayEntity;
import com.proriberaapp.ribera.Domain.entities.FullDayTypeFoodEntity;
import com.proriberaapp.ribera.services.client.FullDayService;
import com.proriberaapp.ribera.services.client.FullDayTypeFoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
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
    public Mono<FullDayTypeFoodEntity> createFullDayTypeFood(@RequestPart("file") Mono<FilePart> file, @RequestParam("foodName") String foodName,
                                                             @RequestParam("type") String type, @RequestParam("price") BigDecimal price, @RequestParam("entry") String entry,
                                                             @RequestParam("background") String background, @RequestParam("drink") String drink, @RequestParam("dessert") String dessert,
                                                             @RequestParam("quantity") Integer quantity, @RequestParam("currencyTypeId") Integer currencyTypeId, @RequestParam("folderNumber") Integer folderNumber) {
        FullDayTypeFoodEntity fullDayTypeFoodEntity = FullDayTypeFoodEntity.builder()
                .FoodName(foodName)
                .type(type)
                .price(price)
                .Entry(entry)
                .Background(background)
                .Drink(drink)
                .Dessert(dessert)
                .quantity(quantity)
                .currencyTypeId(currencyTypeId)
                .build();

        return fullDayTypeFoodService.saveFullDayTypeFood(fullDayTypeFoodEntity, file, folderNumber);
    }

    @PutMapping("/typeFoodUp/{fullDayTypeFoodId}")
    public Mono<ResponseEntity<FullDayTypeFoodEntity>> updateFullDayTypeFood(@PathVariable Integer fullDayTypeFoodId,
            @RequestParam String foodName,
            @RequestParam String background,
            @RequestParam String drink,
            @RequestParam String dessert,
            @RequestParam String entry,
            @RequestParam BigDecimal price,
            @RequestParam Integer quantity,
            @RequestParam Integer currencyTypeId,
            @RequestPart(value = "file") Mono<FilePart> file,
            @RequestParam Integer folderNumber) {

        FullDayTypeFoodEntity fullDayTypeFoodEntity = new FullDayTypeFoodEntity();
        fullDayTypeFoodEntity.setFoodName(foodName);
        fullDayTypeFoodEntity.setBackground(background);
        fullDayTypeFoodEntity.setDrink(drink);
        fullDayTypeFoodEntity.setDessert(dessert);
        fullDayTypeFoodEntity.setEntry(entry);
        fullDayTypeFoodEntity.setPrice(price);
        fullDayTypeFoodEntity.setQuantity(quantity);
        fullDayTypeFoodEntity.setCurrencyTypeId(currencyTypeId);

        return fullDayTypeFoodService.updateFullDayTypeFood(fullDayTypeFoodId, fullDayTypeFoodEntity, file, folderNumber)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/typeFoodDl/{id}")
    public Mono<Void> deleteFullDayTypeFood(@PathVariable("id") Integer fullDayTypeFoodId) {
        return fullDayTypeFoodService.deleteFullDayTypeFood(fullDayTypeFoodId);
    }

}
