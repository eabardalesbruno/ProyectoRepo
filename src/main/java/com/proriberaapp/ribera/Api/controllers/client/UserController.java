package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.*;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.services.client.UserApiClient;
import com.proriberaapp.ribera.services.client.UserClientService;
import com.proriberaapp.ribera.services.client.UserRegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.proriberaapp.ribera.utils.GeneralMethods.generatePassword;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {
    @Autowired
    private UserClientService userClientService;
    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserApiClient userApiClient;
    private final UserRegistrationService userRegistrationService;

    @Autowired
    public UserController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

/*
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
        user.setCreatedat(request.createdat());

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
                                savedUser.getUsername(),
                                savedUser.getCreatedat()
                        ),
                        HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }
 */

    /*02072024
    @PostMapping("/register")
    public Mono<Object> registerUser(@RequestBody RegisterRequest request) {
        if (request.email() == null || request.password() == null ||
            request.firstName() == null || request.lastName() == null) {
        return Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
     }

    return userClientService.findByEmail(request.email())
            .flatMap(existingUser -> {
                return Mono.error(new RuntimeException("El correo electrónico ya está registrado"));
            })
            .switchIfEmpty(Mono.defer(() -> {
                UserClientEntity user = UserClientEntity.builder().build();
                user.setEmail(request.email());
                user.setPassword(request.password());
                user.setFirstName(request.firstName());
                user.setLastName(request.lastName());
                user.setRegisterTypeId(request.registerTypeId());
                user.setUserLevelId(request.userLevelId());
                user.setCodeUser(request.codeUser());
                user.setCountryId(request.countryId());
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
                user.setCreatedat(request.createdat());

                return userClientService.registerUser(user)
                        .flatMap(savedUser -> {
                            return userClientService.login(request.email(), request.password())
                                    .map(token -> {
                                        return new ResponseEntity<>(new LoginResponse(token), HttpStatus.OK);
                                    });
                        });
            }))
            .onErrorResume(e -> Mono.just(new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST)));
    }

     */

    @PostMapping("/register")
    public Mono<Object> registerUser(@RequestBody RegisterRequest request) {
        if (request.email() == null || request.firstName() == null || request.lastName() == null) {
            return Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
        String randomPassword = generatePassword(request.firstName(), request.lastName());
        return userClientService.findByEmail(request.email())
                .flatMap(existingUser -> Mono.error(new RuntimeException("El correo electrónico ya está registrado")))
                .switchIfEmpty(Mono.defer(() -> {
                    UserClientEntity user = UserClientEntity.builder()
                            .email(request.email())
                            .password(request.password())
                            .firstName(request.firstName())
                            .lastName(request.lastName())
                            .registerTypeId(request.registerTypeId())
                            .userLevelId(request.userLevelId())
                            .codeUser(request.codeUser())
                            .countryId(request.countryId())
                            .documenttypeId(request.documenttypeId())
                            .documentNumber(request.documentNumber())
                            .birthDate(request.birthDate())
                            .genderId(request.genderId())
                            .role(request.role())
                            .civilStatus(request.civilStatus())
                            .city(request.city())
                            .address(request.address())
                            .cellNumber(request.cellNumber())
                            .googleAuth(request.googleAuth())
                            .googleId(request.googleId())
                            .googleEmail(request.googleEmail())
                            .username(request.username())
                            .createdat(request.createdat())
                            .build();

                    return userClientService.registerUser(user, randomPassword)
                            .flatMap(savedUser -> {
                                if ("1".equals(request.googleAuth()) && (request.password() == null || request.password().isEmpty())) {
                                    return userClientService.loginWithGoogle(request.email())
                                            .map(token -> new ResponseEntity<>(new LoginResponse(token, ""), HttpStatus.OK));
                                } else {
                                    return userClientService.login(request.email(), randomPassword)
                                            .map(token -> new ResponseEntity<>(new LoginResponse(token, ""), HttpStatus.OK));
                                }
                            });
                }))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST)));
    }


    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> loginUser(@RequestBody LoginRequest request) {
        return userClientService.login(request.email(), request.password())
                .map(token -> new ResponseEntity<>(new LoginResponse(token, "tokenizado"), HttpStatus.OK))
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

    /*
    @GetMapping("/check-email")
    public Mono<ResponseEntity<LoginResponse>> checkEmail(@RequestParam String email) {
        return userClientService.checkAndGenerateToken(email)
                .map(token -> new ResponseEntity<>(new LoginResponse(token), HttpStatus.OK))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(new LoginResponse(e.getMessage()), HttpStatus.BAD_REQUEST)));
    }
     */

    @GetMapping("/check-email")
    public Mono<ResponseEntity<LoginResponse>> checkEmail(@RequestParam String email) {
        return userClientService.checkAndGenerateToken(email)
                .map(tokenResult -> {
                    String message = tokenResult.getToken().isEmpty() ? "sin token" : "tokenizado";
                    HttpStatus status = tokenResult.getToken().isEmpty() ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
                    return new ResponseEntity<>(new LoginResponse(tokenResult.getToken(), message), status);
                })
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(new LoginResponse("", "sin token"), HttpStatus.BAD_REQUEST)));
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

    @GetMapping("/find/my-data")
    public Mono<UserDataDTO> findMyData(
            @RequestHeader("Authorization") String token) {
        Integer idUserClient = jwtProvider.getIdFromToken(token);
        return userClientService.findUserDTOById(idUserClient);
    }

}