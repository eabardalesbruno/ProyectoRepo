package com.proriberaapp.ribera.Api.controllers.client;
import com.proriberaapp.ribera.Domain.entities.RestaurantEntity;
import com.proriberaapp.ribera.services.client.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public Mono<ResponseEntity<RestaurantEntity>> createRestaurant(@RequestBody RestaurantEntity restaurant) {
        return restaurantService.createRestaurant(restaurant)
                .map(createdRestaurant -> new ResponseEntity<>(createdRestaurant, HttpStatus.CREATED));
    }

    @GetMapping("/{restaurantId}")
    public Mono<ResponseEntity<RestaurantEntity>> getRestaurant(@PathVariable Integer restaurantId) {
        return restaurantService.getRestaurant(restaurantId)
                .map(restaurant -> ResponseEntity.ok(restaurant))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<RestaurantEntity> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @DeleteMapping("/{restaurantId}")
    public Mono<ResponseEntity<Void>> deleteRestaurant(@PathVariable Integer restaurantId) {
        return restaurantService.deleteRestaurant(restaurantId)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
