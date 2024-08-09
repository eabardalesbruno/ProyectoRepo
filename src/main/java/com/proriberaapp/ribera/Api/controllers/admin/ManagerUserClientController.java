package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.services.admin.ClientManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("${url.manager}/user-client")
@RequiredArgsConstructor
@Slf4j
public class ManagerUserClientController {

    private final ClientManagerService clientManagerService;
    private final JwtProvider jwtProvider;

    @PatchMapping("/update/password")
    public Mono<UserClientEntity> updatePassword(
            @RequestHeader("Authorization") String token,
            @RequestParam Integer id,
            @RequestParam String newPassword) {
        Integer idUserAdmin = jwtProvider.getIdFromToken(token);
        return clientManagerService.updatePassword(id, newPassword);
    }

    @GetMapping("/find/{id}")
    public Mono<UserClientEntity> findById(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer id) {
        Integer idUserAdmin = jwtProvider.getIdFromToken(token);
        return clientManagerService.findById(id);
    }

    @GetMapping("/find/all")
    public Flux<UserClientEntity> findAll(@RequestHeader("Authorization") String token) {
        //Integer idUserAdmin = jwtTokenProvider.getIdFromToken(token);
        return clientManagerService.findAll();
    }

    @PatchMapping("/disable")
    public Mono<UserClientEntity> disable(@RequestHeader("Authorization") String token, @RequestParam Integer id) {
        Integer idUserAdmin = jwtProvider.getIdFromToken(token);
        return clientManagerService.disable(id);
    }

    @PatchMapping("/enable")
    public Mono<UserClientEntity> enable(@RequestHeader("Authorization") String token, @RequestParam Integer id) {
        Integer idUserAdmin = jwtProvider.getIdFromToken(token);
        return clientManagerService.enable(id);
    }

    @DeleteMapping("/delete")
    public Mono<UserClientEntity> delete(@RequestHeader("Authorization") String token, @RequestParam Integer id) {
        Integer idUserAdmin = jwtProvider.getIdFromToken(token);
        return clientManagerService.delete(id);
    }

    @GetMapping("/find/by-email")
    public Mono<UserClientEntity> findByEmail(@RequestHeader("Authorization") String token, @RequestParam String email) {
        Integer idUserAdmin = jwtProvider.getIdFromToken(token);
        return clientManagerService.findByEmail(email);
    }

}
