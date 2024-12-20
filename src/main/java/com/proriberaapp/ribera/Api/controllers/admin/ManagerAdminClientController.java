package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.UserClientDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.UserClientPageDto;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.services.admin.ClientManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("${url.api}/maintenance/user-client")
@RequiredArgsConstructor
@Slf4j
public class ManagerAdminClientController {

    private final ClientManagerService clientManagerService;

    @PatchMapping("/update/password")
    public Mono<UserClientEntity> updatePassword(@RequestParam Integer id, @RequestParam String newPassword) {
        return clientManagerService.updatePassword(id, newPassword);
    }

    @GetMapping("/find/{id}")
    public Mono<UserClientEntity> findById(@PathVariable Integer id) {
        return clientManagerService.findById(id);
    }

    @GetMapping("/find/all")
    public Flux<UserClientEntity> findAll() {
        return clientManagerService.findAll();
    }

    @PatchMapping("/disable")
    public Mono<UserClientEntity> disable(@RequestParam Integer id) {
        return clientManagerService.disable(id);
    }

    @PatchMapping("/enable")
    public Mono<UserClientEntity> enable(@RequestParam Integer id) {
        return clientManagerService.enable(id);
    }

    @DeleteMapping("/delete")
    public Mono<UserClientEntity> delete(@RequestParam Integer id) {
        return clientManagerService.delete(id);
    }

    @GetMapping("/find/by-email")
    public Mono<UserClientEntity> findByEmail(@RequestParam String email) {
        return clientManagerService.findByEmail(email);
    }

    @GetMapping("/findAllClients")
    public Mono<UserClientPageDto> getAllClients(@RequestParam Integer indice, @RequestParam(required = false) Integer statusId, @RequestParam(required = false) String fecha, @RequestParam(required = false) String filter) {
        return clientManagerService.getAllClients(indice, statusId, fecha, filter);
    }

}
