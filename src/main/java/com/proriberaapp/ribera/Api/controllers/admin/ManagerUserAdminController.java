package com.proriberaapp.ribera.Api.controllers.admin;
import com.proriberaapp.ribera.Api.controllers.admin.dto.*;
import com.proriberaapp.ribera.Crosscutting.security.JwtTokenProvider;
import com.proriberaapp.ribera.Infraestructure.services.admin.UserAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/admin/manager/user")
@RequiredArgsConstructor
@Slf4j
public class ManagerUserAdminController {

    private final UserAdminService userAdminService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public Mono<UserAdminResponse> register(
            @RequestBody RegisterRequest registerRequest,
            @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jwtTokenProvider.getIdFromToken(token.substring(7));
        log.info("idUserAdmin: " + idUserAdmin);
        return userAdminService.register(idUserAdmin, registerRequest);
    }

    @PatchMapping("/update/password")
    public Mono<UserAdminResponse> updatePassword(
            @RequestParam Integer idUserAdminUpdatePassword,
            @RequestParam String newPassword,
            @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jwtTokenProvider.getIdFromToken(token.substring(7));
        return userAdminService.updatePassword(idUserAdmin,idUserAdminUpdatePassword, newPassword);
    }

    @PatchMapping("/update")
    public Mono<UserAdminResponse> update(
            @RequestParam Integer idUserAdminUpdate,
            @RequestBody UpdateUserAdminRequest updateRequest,
            @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jwtTokenProvider.getIdFromToken(token.substring(7));
        return userAdminService.update(idUserAdmin, idUserAdminUpdate, updateRequest);
    }

    @DeleteMapping("/delete")
    public Mono<Void> delete(@RequestParam Integer idUserAdminDelete,
                             @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jwtTokenProvider.getIdFromToken(token.substring(7));
        return userAdminService.delete(idUserAdmin, idUserAdminDelete);
    }

    @GetMapping("/find/id")
    public Mono<UserAdminResponse> findById(@RequestParam Integer idUserAdmin) {
        return userAdminService.findById(idUserAdmin);
    }

    @GetMapping("/find/email")
    public Mono<UserAdminResponse> findByEmail(@RequestParam String email) {
        return userAdminService.findByEmail(email);
    }

    @GetMapping("/find/all")
    public Flux<UserAdminResponse> findAll(@RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jwtTokenProvider.getIdFromToken(token.substring(7));
        return userAdminService.findAll(idUserAdmin);
    }

    @PatchMapping("/update/status/enable")
    public Mono<UserAdminResponse> updateStatusEnable(
            @RequestParam Integer idUserAdminUpdateStatus,
            @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jwtTokenProvider.getIdFromToken(token.substring(7));
        return userAdminService.enable(idUserAdmin, idUserAdminUpdateStatus);
    }

    @PatchMapping("/update/status/disable")
    public Mono<UserAdminResponse> updateStatusDisable(
            @RequestParam Integer idUserAdminUpdateStatus,
            @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jwtTokenProvider.getIdFromToken(token.substring(7));
        return userAdminService.disable(idUserAdmin, idUserAdminUpdateStatus);
    }

}
