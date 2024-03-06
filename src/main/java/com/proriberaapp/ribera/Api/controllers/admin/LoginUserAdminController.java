package com.proriberaapp.ribera.Api.controllers.admin;
import com.proriberaapp.ribera.Api.controllers.admin.dto.*;
import com.proriberaapp.ribera.Infraestructure.services.admin.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/user/admin")
@RequiredArgsConstructor
public class LoginUserAdminController {

    private final UserAdminService userAdminService;

    @GetMapping("/login")
    public Mono<TokenDto> login(@RequestBody LoginRequest loginRequest) {
        return userAdminService.login(loginRequest);
    }

}
