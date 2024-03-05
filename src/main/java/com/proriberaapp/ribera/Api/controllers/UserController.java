package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.UserEntity;
import com.proriberaapp.ribera.Infraestructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/simple")
    public void createUser(@RequestBody UserEntity user) {
        userRepository.insert(user);
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
}