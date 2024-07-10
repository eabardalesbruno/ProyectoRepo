package com.proriberaapp.ribera.Api.controllers.payme;

import com.proriberaapp.ribera.Api.controllers.payme.dto.NonceResponse;
import com.proriberaapp.ribera.Api.controllers.payme.dto.PaymentEntity;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.services.PayMeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("${url.client}/payme")
@RequiredArgsConstructor
@Slf4j
public class PayMeController {

    private final PayMeService paymeService;
    private final JwtProvider jtp;

    @PostMapping("create-nonce")
    public Mono<NonceResponse> nonce(
            @RequestHeader("Authorization") String token
    ) {
        Integer idUser = jtp.getIdFromToken(token);
        return paymeService.getNonce();
    }

    @GetMapping
    public Flux<PaymentEntity> getPayments(
            @RequestHeader("Authorization") String token
    ) {
        Integer idUser = jtp.getIdFromToken(token);
        return paymeService.getPayments(idUser);
    }
}
