package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.RestaurantEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RestaurantService {
    Mono<RestaurantEntity> createRestaurant(RestaurantEntity restaurant);
    Mono<RestaurantEntity> getRestaurant(Integer restaurantId);
    Flux<RestaurantEntity> getAllRestaurants();
    Mono<Void> deleteRestaurant(Integer restaurantId);
}