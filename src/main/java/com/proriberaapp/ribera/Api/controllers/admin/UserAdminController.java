package com.proriberaapp.ribera.Api.controllers.admin;
import com.proriberaapp.ribera.Api.controllers.admin.dto.*;
import com.proriberaapp.ribera.Infraestructure.services.admin.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user/admin")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminService userAdminService;

    @PostMapping("/login")
    public Mono<UserAdminResponse> login(@RequestBody LoginRequest loginRequest) {
        return userAdminService.login(loginRequest);
    }

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
            @RequestParam Integer idUserAdmin, @RequestBody UpdateUserAdminRequest updateRequest) {
        return userAdminService.update(idUserAdmin, updateRequest);
    }

}
