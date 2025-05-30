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
    /*
    @GetMapping("/findAllClients")
    public Mono<UserClientPageDto> getAllClients(@RequestParam Integer indice, @RequestParam(required = false) Integer statusId, @RequestParam(required = false) String fecha, @RequestParam(required = false) String filter) {
        return clientManagerService.getAllClients(indice, statusId, fecha, filter);
    }
    */

    @GetMapping("/findAllClients")
    public Mono<UserClientPageDto> getAllClients(@RequestParam Integer indice,
                                                 @RequestParam(required = false) Integer statusId,
                                                 @RequestParam(required = false) Integer userLevelId,
                                                 @RequestParam(required = false) String fechaInicio,
                                                 @RequestParam(required = false) String fechaFin,
                                                 @RequestParam(required = false) String filter) {
        return clientManagerService.getAllClientsWithParams(indice, statusId,userLevelId, fechaInicio,fechaFin, filter);
    }

    @PostMapping("/updateClient")
    public Mono<Void> updateClient(@RequestBody UserClientDto entity) {
        return clientManagerService.saveClient(entity);
    }

}
