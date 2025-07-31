package com.proriberaapp.ribera.Api.controllers.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para la validación de tipo de usuario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateUserTypeResponse {
    
    /**
     * ID del usuario
     */
    private Long id;
    
    /**
     * Tipo de usuario (ej: "INCLUB_MEMBER", "REGULAR_USER", etc.)
     */
    private String userType;
    
    /**
     * Indica si el usuario es de Inclub
     */
    private Boolean isUserInclub;
    
    /**
     * Mensaje de respuesta
     */
    private String message;
} 