package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.UserLevelEntity;
import com.proriberaapp.ribera.services.UserLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manager/user-level")
@RequiredArgsConstructor
public class ManagerUserLevelController {
    private final UserLevelService userLevelService;

    @GetMapping("/find/all")
    public Flux<UserLevelEntity> findAllUserLevels() {
        return userLevelService.findAll();
    }

    @GetMapping("/find")
    public Mono<UserLevelEntity> findUserLevel(Integer id) {
        return userLevelService.findById(id);
    }

    @DeleteMapping("/delete")
    public Mono<Void> deleteUserLevel(Integer id) {
        return userLevelService.deleteById(id);
    }

    @PostMapping("/register")
    public Mono<UserLevelEntity> registerUserLevel(@RequestBody UserLevelEntity userLevelEntity) {
        return userLevelService.save(userLevelEntity);
    }

    @PostMapping("/register/all")
    public Flux<UserLevelEntity> registerAllUserLevels(@RequestBody List<UserLevelEntity> userLevelEntity) {
        return userLevelService.saveAll(userLevelEntity);
    }

    @PatchMapping("/update")
    public Mono<UserLevelEntity> updateUserLevel(@RequestBody UserLevelEntity userLevelEntity) {
        return userLevelService.update(userLevelEntity);
    }
}
