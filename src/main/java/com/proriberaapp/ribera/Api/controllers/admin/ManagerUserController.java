package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.UserAdminResponse;
import com.proriberaapp.ribera.Crosscutting.security.JwtTokenProvider;
import com.proriberaapp.ribera.Infraestructure.services.admin.UserAdminManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/admin/manager/user")
@RequiredArgsConstructor
@Slf4j
public class ManagerUserController {

    private final UserAdminManagerService userAdminManagerService;
    private final JwtTokenProvider jwtTokenProvider;

    @PatchMapping("/update/password")
    public Mono<UserAdminResponse> updatePassword(
            @RequestParam String code,
            @RequestParam String newPassword,
            @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jwtTokenProvider.getIdFromToken(token.substring(7));
        return userAdminManagerService.updatePasswordByVerificationCode(idUserAdmin, code, newPassword);
    }
}
