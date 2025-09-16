package com.proriberaapp.ribera.Infraestructure.externalService.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserContextResponse {
    // Necesarios
    private String userId;
    private String email;
    private String role;        // "ADMIN", "SOCIO", "CLIENTE", "PROMOTOR"
    private boolean isActive;
    
    // Campos adicionales
    private String username;
    private String documentNumber;
    
    // Campo para identificar Socio
    private Boolean isUserInclub;
}
