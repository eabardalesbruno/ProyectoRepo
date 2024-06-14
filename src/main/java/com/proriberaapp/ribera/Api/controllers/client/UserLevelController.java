package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.UserLevelEntity;
import com.proriberaapp.ribera.services.UserLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/user-level")
@RequiredArgsConstructor
public class UserLevelController {
    private final UserLevelService userLevelService;
    @GetMapping("/find")
    public Mono<UserLevelEntity> findUserLevel(@RequestParam Integer id) {
        return userLevelService.findById(id);
    }
    @GetMapping("/find/all")
    public Flux<UserLevelEntity> findAllUserLevels() {
        return userLevelService.findAll();
    }
}
