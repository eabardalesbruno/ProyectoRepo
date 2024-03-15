package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Infraestructure.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BaseController<R,T> {
    private final BaseService service;

    @GetMapping("find/all")
    public Flux<R> getAll() {
        return service.findAll();
    }

    @GetMapping("find")
    public Mono<R> getById(Integer id) {
        return service.findById(id);
    }
}
