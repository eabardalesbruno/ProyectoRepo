package com.proriberaapp.ribera.Api.controllers;
import com.proriberaapp.ribera.Api.controllers.dto.*;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Infraestructure.services.UserApiClient;
import com.proriberaapp.ribera.Infraestructure.services.UserRegistrationService;
import com.proriberaapp.ribera.Infraestructure.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserApiClient userApiClient;
    private final UserRegistrationService userRegistrationService;

    @Autowired
    public UserController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<RegisterResponse>> registerUser(@RequestBody RegisterRequest request) {

        if (request.email() == null || request.password() == null ||
                request.firstName() == null || request.lastName() == null) {
            return Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }

        UserClientEntity user = UserClientEntity.builder().build();
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setRegisterTypeId(request.registerTypeId());
        user.setUserLevelId(request.userLevelId());
        user.setCodeUser(request.codeUser());
        user.setNationality(request.nationality());
        user.setDocumentType(request.documentType());
        user.setDocumentNumber(request.documentNumber());
        user.setBirthDate(request.birthDate());
        user.setSex(request.sex());
        user.setRole(request.role());
        user.setCivilStatus(request.civilStatus());
        user.setCity(request.city());
        user.setAddress(request.address());
        user.setCellNumber(request.cellNumber());
        user.setGoogleAuth(request.googleAuth());
        user.setGoogleId(request.googleId());
        user.setGoogleEmail(request.googleEmail());
        user.setUsername(request.username());

        return userService.registerUser(user)
                .map(savedUser -> new ResponseEntity<>(
                        new RegisterResponse(
                                savedUser.getUserClientId(),
                                savedUser.getFirstName(),
                                savedUser.getLastName(),
                                savedUser.getRegisterTypeId(),
                                savedUser.getUserLevelId(),
                                savedUser.getCodeUser(),
                                savedUser.getNationality(),
                                savedUser.getDocumentType(),
                                savedUser.getDocumentNumber(),
                                savedUser.getBirthDate(),
                                savedUser.getSex(),
                                savedUser.getRole(),
                                savedUser.getCivilStatus(),
                                savedUser.getCity(),
                                savedUser.getAddress(),
                                savedUser.getCellNumber(),
                                savedUser.getEmail(),
                                savedUser.getGoogleAuth(),
                                savedUser.getGoogleId(),
                                savedUser.getGoogleEmail(),
                                savedUser.getUsername()
                        ),
                        HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> loginUser(@RequestBody LoginRequest request) {
        return userService.login(request.email(), request.password())
                .map(token -> new ResponseEntity<>(new LoginResponse(token), HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @PostMapping("/registerAndLogin")
    public Mono<ResponseEntity<String>> registerAndLoginUser(@RequestBody RegisterAndLoginRequest request) {
        return userRegistrationService.loginAndRegisterUser(request.username(), request.password())
                .flatMap(token -> {
                    UserClientEntity newUser = UserClientEntity.builder()
                            .email(request.email())
                            .password(request.password())
                            .username(request.username())
                            .build();
                    return userService.saveUser(newUser)
                            .thenReturn(ResponseEntity.ok("Usuario registrado y logueado exitosamente"));
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(e.getMessage())));
    }
}