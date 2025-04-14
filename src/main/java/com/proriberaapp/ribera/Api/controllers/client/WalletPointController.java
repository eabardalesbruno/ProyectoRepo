package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.request.WalletPointRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.WalletPointResponse;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.enums.Role;
import com.proriberaapp.ribera.services.client.WalletPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/wallet-points")
@RequiredArgsConstructor
public class WalletPointController {
    private final WalletPointService walletPointService;
    private final JwtProvider jtp;

    @PostMapping
    public Mono<ResponseEntity<WalletPointResponse>> createWalletPoint(
            @RequestBody WalletPointRequest walletPointRequest, @RequestHeader("Authorization") String token) {
        walletPointRequest.setUserId(jtp.getIdFromToken(token));
        return walletPointService.createWalletPoint(walletPointRequest)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/family/{idFamily}")
    public Mono<ResponseEntity<WalletPointResponse>> getWalletPointByFamily(
            @RequestHeader("Authorization") String token,
            @RequestHeader(value = "key-inclub-ribera", defaultValue = "") String tokenBackOffice,
            @PathVariable Integer idFamily,
            @RequestParam Role role ) {

        String identifier = Objects.isNull(jtp.getUsernameFromToken(token))
                ? String.valueOf(jtp.getIdFromToken(token))
                : jtp.getUsernameFromToken(token);

        return walletPointService.getWalletByIdentifier(identifier, role, idFamily, tokenBackOffice)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @GetMapping
    public Mono<ResponseEntity<WalletPointResponse>> getWalletPoint(
            @RequestHeader("Authorization") String token) {
        Integer userId = jtp.getIdFromToken(token);
        return walletPointService.getWalletByUserId(userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @GetMapping("/user/{id}")
    public Mono<ResponseEntity<WalletPointResponse>> getWalletPointByUserId(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer id) {
        return walletPointService.getWalletByUserId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @PutMapping
    public Mono<ResponseEntity<WalletPointResponse>> updateWalletPoints(
            @RequestBody WalletPointRequest request, @RequestHeader("Authorization") String token) {
        Integer userId = jtp.getIdFromToken(token);
        return walletPointService.updateWalletPoints(userId, request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("/buy")
    public Mono<ResponseEntity<WalletPointResponse>> buyPoints(@RequestBody WalletPointRequest request, @RequestHeader("Authorization") String token) {
        Integer userId = jtp.getIdFromToken(token);
        return walletPointService.
                buyPoints(userId, request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("/convert-points")
    public Mono<Void> convertPoints(@RequestBody WalletPointRequest request,
                                    @RequestHeader("Authorization") String token) {

        String username = jtp.getUsernameFromToken(token);
        Integer userId = jtp.getIdFromToken(token);
        System.out.println(userId);
        return walletPointService.convertPoints(userId, username, request);
    }
}


