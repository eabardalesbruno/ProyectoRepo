package com.proriberaapp.ribera.Infraestructure.externalService.controller;

import com.proriberaapp.ribera.Infraestructure.externalService.service.promotion.IPromotionValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class PromotionValidationController {

    private final IPromotionValidationService promotionValidationService;

    /**
     * Endpoint para validar tokens desde microservicio de promociones
     * @param authHeader Token a validar (viene en el header Authorization)
     * @return Información del usuario si el token es válido
     */
    @PostMapping("/validate-promotions")
    public Mono<ResponseEntity<Object>> validatePromotions(
            @RequestHeader("Authorization") String authHeader) {
        
        log.info("Solicitud de validación de token para promociones");
        log.debug("Validando token desde microservicio de promociones: {}", 
                authHeader != null ? authHeader.substring(0, Math.min(authHeader.length(), 20)) + "..." : "null");

        return promotionValidationService.validateTokenForPromotions(authHeader)
                .map(userContext -> {
                    log.info("Token válido para usuario: {} con rol: {}", 
                            userContext.getUserId(), userContext.getRole());
                    return ResponseEntity.ok((Object) userContext);
                })
                .onErrorResume(error -> {
                    log.warn("Error al validar token para promociones: {}", error.getMessage());
                    
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("valid", false);
                    errorResponse.put("message", error.getMessage());
                    errorResponse.put("timestamp", System.currentTimeMillis());
                    
                    HttpStatus status;
                    if (error instanceof IllegalArgumentException) {
                        status = HttpStatus.UNAUTHORIZED;
                    } else if (error instanceof ResponseStatusException rse) {
                        status = rse.getStatusCode().is4xxClientError() ? HttpStatus.NOT_FOUND : HttpStatus.INTERNAL_SERVER_ERROR;
                    } else {
                        status = HttpStatus.INTERNAL_SERVER_ERROR;
                    }
                    
                    return Mono.just(ResponseEntity.status(status).body((Object) errorResponse));
                });
    }

    /**
     * Endpoint de health check para el microservicio de promociones
     * @return Estado del servicio
     */
    @GetMapping("/promotions/health")
    public Mono<ResponseEntity<Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "promotion-validation");
        health.put("timestamp", System.currentTimeMillis());
        
        return Mono.just(ResponseEntity.ok((Object) health));
    }
}
