package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.UserEntity;
import com.proriberaapp.ribera.Infraestructure.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserControllerBk {
    /*
    @Autowired
    private UserService userService;

    @GetMapping
    public Flux<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public Mono<UserEntity> getUserById(@PathVariable Integer userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public Mono<UserEntity> createUser(@RequestBody UserEntity user) {
        return userService.createUser(user);
    }

    @DeleteMapping("/{userId}")
    public Mono<Void> deleteUser(@PathVariable Integer userId) {
        return userService.deleteUser(userId);
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> registerUser(@RequestBody UserEntity user) {
        return userService.saveUser(user)
                .map(savedUser -> ResponseEntity.ok("User registered successfully"))
                .defaultIfEmpty(ResponseEntity.badRequest().body("Failed to register user"));
    }
    @PostMapping("/insertUser")
    public String insertUser(@RequestBody String userData) {
        String apiUrl = "https://servicios.inclubtest.online:2053/api/User/insert";

        RestTemplate restTemplate = new RestTemplate();

        try {
            String response = restTemplate.postForObject(apiUrl, userData, String.class);
            return "Respuesta de BackOffice: " + response;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al consumir API BackOffice: " + e.getMessage();
        }
    }

     */
}