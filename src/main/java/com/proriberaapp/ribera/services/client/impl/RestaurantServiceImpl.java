package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.RestaurantEntity;
import com.proriberaapp.ribera.Infraestructure.repository.RestaurantRepository;
import com.proriberaapp.ribera.services.client.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Mono<RestaurantEntity> createRestaurant(RestaurantEntity restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Mono<RestaurantEntity> getRestaurant(Integer restaurantId) {
        return restaurantRepository.findById(restaurantId);
    }

    @Override
    public Flux<RestaurantEntity> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Mono<Void> deleteRestaurant(Integer restaurantId) {
        return restaurantRepository.deleteById(restaurantId);
    }
}
