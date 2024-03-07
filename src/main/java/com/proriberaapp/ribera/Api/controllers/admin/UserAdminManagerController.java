package com.proriberaapp.ribera.Api.controllers.admin;
import com.proriberaapp.ribera.Api.controllers.admin.dto.*;
import com.proriberaapp.ribera.Infraestructure.services.admin.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/admin/user/manager")
@RequiredArgsConstructor
public class UserAdminManagerController {

    private final UserAdminService userAdminService;

    @PostMapping("/register")
    public Mono<UserAdminResponse> register(@RequestBody RegisterRequest registerRequest) {
        return userAdminService.register(registerRequest);
    }

    @PatchMapping("/update/password")
    public Mono<UserAdminResponse> updatePassword(
            @RequestParam Integer idUserAdmin,
            @RequestParam String newPassword) {
        return userAdminService.updatePassword(idUserAdmin, newPassword);
    }

    @PatchMapping("/update")
    public Mono<UserAdminResponse> update(
            @RequestParam Integer idUserAdmin,
            @RequestBody UpdateUserAdminRequest updateRequest
    ) {
        return userAdminService.update(idUserAdmin, updateRequest);
    }

    @DeleteMapping("/delete")
    public Mono<Void> delete(@RequestParam Integer idUserAdmin) {
        return userAdminService.delete(idUserAdmin);
    }

    @GetMapping("/find")
    public Mono<UserAdminResponse> findById(@RequestParam Integer idUserAdmin) {
        return userAdminService.findById(idUserAdmin);
    }

    @GetMapping("/find/email")
    public Mono<UserAdminResponse> findByEmail(@RequestParam String email) {
        return userAdminService.findByEmail(email);
    }

    @GetMapping("/find/all")
    public Flux<UserAdminResponse> findAll() {
        return userAdminService.findAll();
    }
}
