package com.proriberaapp.ribera.Api.controllers;
import com.proriberaapp.ribera.Domain.entities.UserEntity;
import com.proriberaapp.ribera.Infraestructure.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Flux<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Mono<UserEntity> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public Mono<UserEntity> createUser(@RequestBody UserEntity user) {
        return userService.createUser(user);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteUser(@PathVariable Integer id) {
        return userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public Mono<UserEntity> updateUser(@PathVariable Integer id, @RequestBody UserEntity user) {
        return userService.updateUser(id, user);
    }
}
