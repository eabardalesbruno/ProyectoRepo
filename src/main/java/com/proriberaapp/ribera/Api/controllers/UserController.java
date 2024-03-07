package com.proriberaapp.ribera.Api.controllers;
import com.proriberaapp.ribera.Api.controllers.dto.*;
import com.proriberaapp.ribera.Domain.entities.UserEntity;
import com.proriberaapp.ribera.Infraestructure.services.UserService;
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
    public Mono<ResponseEntity<RegisterResponse>> registerUser(@RequestBody RegisterRequest request) {
        UserEntity user = UserEntity.builder()
                .email(request.email())
                .password(request.password())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();

        return userService.registerUser(user)
                .map(savedUser -> new ResponseEntity<>(
                        new RegisterResponse(savedUser.getUserId(), savedUser.getFirstName(), savedUser.getLastName()),
                        HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PostMapping("/register/google")
    public Mono<ResponseEntity<GoogleRegisterResponse>> registerWithGoogle(@RequestBody GoogleRegisterRequest request) {
        return userService.registerWithGoogle(request.googleId(), request.email(), request.name())
                .map(savedUser -> new ResponseEntity<>(
                        new GoogleRegisterResponse(savedUser.getUserId(), savedUser.getEmail()),
                        HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> loginUser(@RequestBody LoginRequest request) {
        return userService.login(request.email(), request.password())
                .map(token -> new ResponseEntity<>(new LoginResponse(token), HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @PostMapping("/login/google")
    public Mono<ResponseEntity<GoogleLoginResponse>> loginWithGoogle(@RequestBody GoogleLoginRequest request) {
        return userService.loginWithGoogle(request.googleId())
                .map(token -> new ResponseEntity<>(new GoogleLoginResponse(token), HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }
}