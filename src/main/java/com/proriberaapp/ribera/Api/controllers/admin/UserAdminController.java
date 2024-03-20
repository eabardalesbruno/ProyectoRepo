package com.proriberaapp.ribera.Api.controllers.admin;
import com.proriberaapp.ribera.Api.controllers.admin.dto.*;
import com.proriberaapp.ribera.services.admin.UserAdminManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminManagerService userAdminManagerService;

    @GetMapping("/login")
    public Mono<TokenDto> login(@RequestBody LoginRequest loginRequest) {
        return userAdminManagerService.login(loginRequest);
    }

    @PostMapping("/request/update/password")
    public Mono<UserAdminResponse> requestUpdatePassword(
            @RequestBody RequestUpdateUserAdminRequest requestUpdateRequest) {
        return userAdminManagerService.requestUpdatePassword(requestUpdateRequest);
    }

    @PostMapping("/update/password")
    public Mono<UserAdminResponse> updatePassword(
            @RequestParam String verificationCode,
            @RequestParam String newPassword) {
        return userAdminManagerService.updatePasswordByVerificationCode(verificationCode, newPassword);
    }
}
