package com.proriberaapp.ribera.Api.controllers.admin;
import com.proriberaapp.ribera.Api.controllers.admin.dto.*;
import com.proriberaapp.ribera.services.admin.UserAdminManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("${url.admin}")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminManagerService userAdminManagerService;

    @PostMapping("/login")
    public Mono<TokenDto> login(@RequestBody LoginRequest loginRequest) {
        return userAdminManagerService.login(loginRequest);
    }

    @PostMapping("/request/update/password")
    public Mono<UserResponse> requestUpdatePassword(
            @RequestBody RequestUpdateUserAdminRequest requestUpdateRequest) {
        return userAdminManagerService.requestUpdatePassword(requestUpdateRequest);
    }

    @PostMapping("/update/password")
    public Mono<UserResponse> updatePassword(
            @RequestParam String verificationCode,
            @RequestParam String newPassword) {
        return userAdminManagerService.updatePasswordByVerificationCode(verificationCode, newPassword);
    }
}
