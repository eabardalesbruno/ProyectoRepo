package com.proriberaapp.ribera.Infraestructure.externalService.service.promotion;

import com.proriberaapp.ribera.Infraestructure.externalService.dtos.response.UserContextResponse;
import reactor.core.publisher.Mono;

public interface IPromotionValidationService {
    
    /**
     * Valida un token JWT y retorna el contexto del usuario para promociones
     * @param authHeader Header de autorización con formato "Bearer {token}"
     * @return UserContextResponse con información del usuario
     */
    Mono<UserContextResponse> validateTokenForPromotions(String authHeader);
}
