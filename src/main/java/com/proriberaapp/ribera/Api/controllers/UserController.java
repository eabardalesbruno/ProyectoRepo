package com.proriberaapp.ribera.Api.controllers;
import com.proriberaapp.ribera.Api.controllers.dto.*;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.services.UserApiClient;
import com.proriberaapp.ribera.services.UserRegistrationService;
import com.proriberaapp.ribera.services.UserClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserClientService userClientService;

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
        user.setCountryId(request.countryId());
        user.setNationalityId(request.nationalityId());
        user.setAreazoneId(request.areazoneId());
        user.setDocumenttypeId(request.documenttypeId());
        user.setDocumentNumber(request.documentNumber());
        user.setBirthDate(request.birthDate());
        user.setGenderId(request.genderId());
        user.setRole(request.role());
        user.setCivilStatus(request.civilStatus());
        user.setCity(request.city());
        user.setAddress(request.address());
        user.setCellNumber(request.cellNumber());
        user.setGoogleAuth(request.googleAuth());
        user.setGoogleId(request.googleId());
        user.setGoogleEmail(request.googleEmail());
        user.setUsername(request.username());

        return userClientService.registerUser(user)
                .map(savedUser -> new ResponseEntity<>(
                        new RegisterResponse(
                                savedUser.getUserClientId(),
                                savedUser.getFirstName(),
                                savedUser.getLastName(),
                                savedUser.getRegisterTypeId(),
                                savedUser.getUserLevelId(),
                                savedUser.getCountryId(),
                                savedUser.getCodeUser(),
                                savedUser.getGenderId(),
                                savedUser.getNationalityId(),
                                savedUser.getAreazoneId(),
                                savedUser.getDocumenttypeId(),
                                savedUser.getDocumentNumber(),
                                savedUser.getBirthDate(),
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

    @PostMapping("/logino")
    public Mono<ResponseEntity<LoginResponse>> loginUser(@RequestBody LoginRequest request) {
        return userClientService.login(request.email(), request.password())
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
                    return userClientService.saveUser(newUser)
                            .thenReturn(ResponseEntity.ok("Usuario registrado y logueado exitosamente"));
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(e.getMessage())));
    }

    @GetMapping
    public Flux<UserClientEntity> getAllUserClients() {
        return userClientService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserClientEntity>> getUserClientById(@PathVariable Integer id) {
        return userClientService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUserClient(@PathVariable Integer id) {
        return userClientService.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}