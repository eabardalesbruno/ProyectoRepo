package com.proriberaapp.ribera.Api.controllers;
import com.proriberaapp.ribera.Domain.entities.UserEntity;
import com.proriberaapp.ribera.Infraestructure.services.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Mono<UserEntity> registerUser(@RequestBody UserEntity user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public Mono<String> login(@RequestParam String email, @RequestParam String password) {
        return userService.login(email, password);
    }

    @PostMapping("/register/google")
    public Mono<UserEntity> registerWithGoogle(@RequestParam String googleId, @RequestParam String email, @RequestParam String name) {
        return userService.registerWithGoogle(googleId, email, name);
    }

    @PostMapping("/login/google")
    public Mono<String> loginWithGoogle(@RequestParam String googleId) {
        return userService.loginWithGoogle(googleId);
    }
}