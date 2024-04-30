package com.proriberaapp.ribera.Api.controllers.admin;
import com.proriberaapp.ribera.Api.controllers.admin.dto.*;
import com.proriberaapp.ribera.Crosscutting.security.JwtTokenProvider;
import com.proriberaapp.ribera.services.admin.UserAdminManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/admin/manager/user-admin")
@RequiredArgsConstructor
@Slf4j
public class ManagerUserAdminController {

    private final UserAdminManagerService userAdminManagerService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public Mono<UserAdminResponse> register(
            @RequestBody RegisterRequest registerRequest,
            @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jwtTokenProvider.getIdFromToken(token.substring(7));
        log.info("idUserAdmin: " + idUserAdmin);
        return userAdminManagerService.register(idUserAdmin, registerRequest);
    }

    @PatchMapping("/update")
    public Mono<UserAdminResponse> update(
            @RequestParam Integer idUserAdminUpdate,
            @RequestBody UpdateUserAdminRequest updateRequest,
            @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jwtTokenProvider.getIdFromToken(token.substring(7));
        return userAdminManagerService.update(idUserAdmin, idUserAdminUpdate, updateRequest);
    }

    @PatchMapping("/update/password")
    public Mono<UserAdminResponse> updatePassword(
            @RequestParam Integer idUserAdminUpdatePassword,
            @RequestParam String newPassword,
            @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jwtTokenProvider.getIdFromToken(token.substring(7));
        return userAdminManagerService.updatePassword(idUserAdmin, idUserAdminUpdatePassword, newPassword);
    }

    @PatchMapping("/update/status/enable")
    public Mono<UserAdminResponse> updateStatusEnable(
            @RequestParam Integer idUserAdminUpdateStatus,
            @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jwtTokenProvider.getIdFromToken(token.substring(7));
        return userAdminManagerService.enable(idUserAdmin, idUserAdminUpdateStatus);
    }

    @PatchMapping("/update/status/disable")
    public Mono<UserAdminResponse> updateStatusDisable(
            @RequestParam Integer idUserAdminUpdateStatus,
            @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jwtTokenProvider.getIdFromToken(token.substring(7));
        return userAdminManagerService.disable(idUserAdmin, idUserAdminUpdateStatus);
    }

    @GetMapping("/find/id")
    public Mono<UserAdminResponse> findById(@RequestParam Integer idUserAdmin) {
        return userAdminManagerService.findById(idUserAdmin);
    }

    @GetMapping("/find/my-data")
    public Mono<UserAdminResponse> findMyData(
            @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jwtTokenProvider.getIdFromToken(token);
        log.info("idUserAdmin: " + idUserAdmin);
        return userAdminManagerService.findById(idUserAdmin);
    }

    @GetMapping("/find/email")
    public Mono<UserAdminResponse> findByEmail(@RequestParam String email) {
        return userAdminManagerService.findByEmail(email);
    }

    @GetMapping("/find/all")
    public Flux<UserAdminResponse> findAll() {
        return userAdminManagerService.findAll();
    }

    @DeleteMapping("/delete")
    public Mono<UserAdminResponse> delete(
            @RequestParam Integer idUserAdminDelete,
            @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jwtTokenProvider.getIdFromToken(token.substring(7));
        return userAdminManagerService.delete(idUserAdmin, idUserAdminDelete);
    }

}
