package com.proriberaapp.ribera.Api.controllers.promoters;

import com.proriberaapp.ribera.Api.controllers.admin.dto.*;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.PromotorDataDTO;
import com.proriberaapp.ribera.Api.controllers.client.dto.UserDataDTO;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.entities.CommissionEntity;
import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Domain.entities.UserPromoterEntity;
import com.proriberaapp.ribera.Domain.enums.StatesUser;
import com.proriberaapp.ribera.services.client.CommissionService;
import com.proriberaapp.ribera.services.promoters.UserPromoterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static com.proriberaapp.ribera.utils.GeneralMethods.generatePassword;

@RestController
@RequestMapping("/api/v1/promoter")
@RequiredArgsConstructor
public class UserPromotersController {

    @Autowired
    private final JwtProvider jwtProvider;
    @Autowired
    private final UserPromoterService userPromoterService;

    @Autowired
    private final CommissionService commissionService;

    @PostMapping("/register")
    public Mono<Object> register( @RequestBody RegisterRequest registerRequest) {
        //Integer idUser = jwtProvider.getIdFromToken(token.substring(7));
        if (registerRequest.email() == null || registerRequest.firstName() == null || registerRequest.lastName() == null) {
            return Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
        return userPromoterService.findByEmail(registerRequest.email())
                .flatMap(existingUser -> Mono.error(new RuntimeException("El correo electrónico ya está registrado")))
                .switchIfEmpty(Mono.defer(() -> {
                    String finalPassword = registerRequest.password() == null || registerRequest.password().isEmpty()
                            ? generatePassword(registerRequest.firstName(), registerRequest.lastName())
                            : registerRequest.password();

                    UserPromoterEntity promoter = UserPromoterEntity.builder()
                            .email(registerRequest.email())
                            .password(registerRequest.password())
                            .username(registerRequest.username())
                            .firstName(registerRequest.firstName())
                            .lastName(registerRequest.lastName())
                            .documenttypeId(registerRequest.typeDocument())
                            .documentNumber(registerRequest.document())
                            .genderId(registerRequest.genderId())
                            .address(registerRequest.address())
                            .phone(registerRequest.phone())
                            .role(registerRequest.role())
                            .googleAuth(registerRequest.googleAuth())
                            .googleId(registerRequest.googleId())
                            .status(StatesUser.ACTIVE)
                            .googleEmail(registerRequest.googleEmail())
                            .username(registerRequest.username())
                            .createdAt(registerRequest.createdat())
                            .build();
                    LoginRequest loginRequest = new LoginRequest(registerRequest.firstName(),registerRequest.email(),finalPassword);
                return userPromoterService.register(promoter,finalPassword)
                .flatMap(savedUser -> {
                    if ("1".equals(registerRequest.googleAuth()) && (registerRequest.password() == null || registerRequest.password().isEmpty())) {
                        return userPromoterService.loginWithGoogle(registerRequest.email())
                                .map(token -> new ResponseEntity<>(new LoginResponse(token, ""), HttpStatus.OK));
                    } else {
                        return userPromoterService.login(loginRequest)
                                .map(token -> new ResponseEntity<>(new LoginResponse(token.token(), savedUser.getUserPromoterId().toString()), HttpStatus.OK));
                    }
                });
                }))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST)));
    }

    @GetMapping("/check-email")
    public Mono<ResponseEntity<LoginResponse>> checkEmail(@RequestParam String email) {
        return userPromoterService.checkAndGenerateToken(email)
                .map(tokenResult -> {
                    String message = tokenResult.getToken().isEmpty() ? "sin token" : "tokenizado";
                    HttpStatus status = tokenResult.getToken().isEmpty() ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
                    return new ResponseEntity<>(new LoginResponse(tokenResult.getToken(), message), status);
                })
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(new LoginResponse("", "sin token"), HttpStatus.BAD_REQUEST)));
    }

    @PostMapping("/login")
    public Mono<TokenDto> login(@RequestBody LoginRequest loginRequest) {
        return userPromoterService.login(loginRequest);
    }

    @GetMapping("/find/my-data")
    public Mono<PromotorDataDTO> findMyData(
            @RequestHeader("Authorization") String token) {
        Integer idUserClient = jwtProvider.getIdFromToken(token);
        return userPromoterService.findPromotorDTOById(idUserClient);
    }

    @GetMapping("/findAllPromoters")
    public Mono<UserPromoterPageDto> getAllClients(@RequestParam Integer indice, @RequestParam(required = false) String status, @RequestParam(required = false) String fecha, @RequestParam(required = false) String filter) {
        return userPromoterService.getAllPromoters(indice, status, fecha, filter);
    }

    @PostMapping("/updatePromoter")
    public Mono<Void> updateClient(@RequestBody UserPromoterDto entity) {
        return userPromoterService.savePromoter(entity);
    }

    @GetMapping("/getStatus")
    public Mono<List<String>> getStatus() {
        return userPromoterService.getStatus();
    }

    @PatchMapping("/update/password")
    public Mono<UserPromoterEntity> updatePassword(@RequestParam Integer id, @RequestParam String newPassword) {
        return userPromoterService.updatePassword(id, newPassword);
    }


    @PostMapping("/calulatecommision")
    public Mono<ResponseEntity<CommissionEntity>> calculateCommission(@RequestBody PaymentBookEntity paymentBook, @RequestParam Integer caseType) {
        return commissionService.calculateAndSaveCommission(paymentBook,caseType)
                .map(commissionEntity -> new ResponseEntity<>(commissionEntity, HttpStatus.OK));
    }

    @GetMapping("/total-commission")
    public Mono<ResponseEntity<BigDecimal>> getTotalCommissionByPromterId(@RequestParam Integer promoterId) {
        return commissionService.getTotalCommissionByPromoterId(promoterId)
                .map(totalCommission -> ResponseEntity.ok(totalCommission))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

