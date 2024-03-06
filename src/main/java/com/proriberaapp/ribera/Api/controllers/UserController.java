package com.proriberaapp.ribera.Api.controllers;
import com.proriberaapp.ribera.Domain.entities.UserEntity;
import com.proriberaapp.ribera.Infraestructure.services.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Mono<ResponseEntity<UserEntity>> registerUser(@RequestBody UserEntity user) {
        return userService.registerUser(user)
                .map(savedUser -> new ResponseEntity<>(savedUser, HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PostMapping("/register/google")
    public Mono<ResponseEntity<UserEntity>> registerWithGoogle(@RequestParam String googleId,
                                                               @RequestParam String email,
                                                               @RequestParam String name) {
        return userService.registerWithGoogle(googleId, email, name)
                .map(savedUser -> new ResponseEntity<>(savedUser, HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> loginUser(@RequestParam String email,
                                                  @RequestParam String password) {
        return userService.login(email, password)
                .map(token -> new ResponseEntity<>(token, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @PostMapping("/login/google")
    public Mono<ResponseEntity<String>> loginWithGoogle(@RequestParam String googleId) {
        return userService.loginWithGoogle(googleId)
                .map(token -> new ResponseEntity<>(token, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }
}