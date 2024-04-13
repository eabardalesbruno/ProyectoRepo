package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.RegisterTypeEntity;
import com.proriberaapp.ribera.services.RegisterTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/register-type")
@RequiredArgsConstructor
public class RegisterTypeController {
    private final RegisterTypeService registerTypeService;
    @GetMapping("/find/all")
    public Flux<RegisterTypeEntity> findAllRegisterTypes() {
        return registerTypeService.findAll();
    }
}
