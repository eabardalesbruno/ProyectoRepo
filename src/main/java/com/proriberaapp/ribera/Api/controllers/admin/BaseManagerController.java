package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.enums.Permission;
import com.proriberaapp.ribera.services.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
abstract class BaseManagerController<R,T> {
    @Autowired
    private BaseService<R,T> service;
    @Autowired
    private JwtProvider jtp;

    @GetMapping("find/all")
    public Flux<R> getAllEntity(
            @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jtp.getIdFromToken(token);
        List<Permission> permissions = jtp.getPermissionsFromToken(token);
        return permissions.contains(Permission.READ.name()) ? service.findAll() : Flux.error(new Exception("No tienes permisos para leer"));
    }

    @GetMapping("find")
    public Mono<R> getEntityById(@RequestParam Integer id,
            @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jtp.getIdFromToken(token);
        List<Permission> permissions = jtp.getPermissionsFromToken(token);
        return permissions.contains(Permission.READ.name()) ? service.findById(id) : Mono.error(new Exception("No tienes permisos para leer"));
    }

    @PostMapping("register")
    public Mono<R> registerEntity(@RequestBody T entity,
                                  @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jtp.getIdFromToken(token);
        List<Permission> permissions = jtp.getPermissionsFromToken(token);
        return permissions.contains(Permission.CREATE.name()) ? service.save(entity) : Mono.error(new Exception("No tienes permisos para crear"));
    }

    @PostMapping("register/all")
    public Flux<R> registerAllEntity(@RequestBody List<T> entity,
                                     @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jtp.getIdFromToken(token);
        List<Permission> permissions = jtp.getPermissionsFromToken(token);
        return permissions.contains(Permission.CREATE.name()) ? service.saveAll(entity) : Flux.error(new Exception("No tienes permisos para crear"));
    }

    @PatchMapping("update")
    public Mono<R> updateEntity(@RequestBody T bedroomEntity,
                                @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jtp.getIdFromToken(token);
        List<Permission> permissions = jtp.getPermissionsFromToken(token);
        return permissions.contains(Permission.UPDATE.name()) ? service.update(bedroomEntity) : Mono.error(new Exception("No tienes permisos para actualizar"));
    }

    @DeleteMapping("delete")
    public Mono<Void> deleteEntity(@RequestParam Integer id,
                                   @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jtp.getIdFromToken(token);
        List<Permission> permissions = jtp.getPermissionsFromToken(token);
        return permissions.contains(Permission.DELETE.name()) ? service.deleteById(id) : Mono.error(new Exception("No tienes permisos para eliminar"));
    }
}
