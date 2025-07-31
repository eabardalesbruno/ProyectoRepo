package com.proriberaapp.ribera.Api.controllers.client.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para validar el tipo de usuario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateUserTypeRequest {
    
    /**
     * ID del usuario
     */
    private Long idUser;
    
    /**
     * Contraseña (puede ser null)
     */
    private String password;
} 