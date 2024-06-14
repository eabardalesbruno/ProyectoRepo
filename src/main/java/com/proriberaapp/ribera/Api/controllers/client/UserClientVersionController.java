package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.UserClientVersionEntity;
import com.proriberaapp.ribera.services.client.UserClientVersionService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/userclientversions")
public class UserClientVersionController {
    private final UserClientVersionService userClientVersionService;

    public UserClientVersionController(UserClientVersionService userClientVersionService) {
        this.userClientVersionService = userClientVersionService;
    }

    @PostMapping
    public Mono<UserClientVersionEntity> createUserClientVersion(@RequestBody UserClientVersionEntity userClientVersion) {
        return userClientVersionService.createUserClientVersion(userClientVersion);
    }

    @GetMapping
    public Flux<UserClientVersionEntity> getAllUserClientVersions() {
        return userClientVersionService.getAllUserClientVersions();
    }

    @GetMapping("/{id}")
    public Mono<UserClientVersionEntity> getUserClientVersionById(@PathVariable Integer id) {
        return userClientVersionService.getUserClientVersionById(id);
    }
}
