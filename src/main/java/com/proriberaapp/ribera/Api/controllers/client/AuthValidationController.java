package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.request.ValidateUserTypeRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.AuthDataResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.ValidateUserTypeResponse;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.services.client.UserClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthValidationController {

    private final JwtProvider jwtProvider;
    private final UserClientService userClientService;

    /**
     * Endpoint para validar tokens desde microservicios
     * @param token Token a validar (viene en el header Authorization)
     * @return Información del usuario si el token es válido
     */
    @PostMapping("/validate-token")
    public Mono<ResponseEntity<Object>> validateToken(
        @RequestHeader("Authorization") String token, 
        @RequestBody (required = false) String ignoredBody) {
        log.debug("Validando token desde microservicio: {}", token);
        
        try {
            // Extraer el token del header "Bearer <token>"
            String actualToken = token.replace("Bearer ", "");
            log.debug("Token extraído: {}", actualToken);
            
            // Validar token usando el JwtProvider existente
            boolean isValid = jwtProvider.validateToken(actualToken);
            log.debug("Resultado de validación: {}", isValid);
            
            if (isValid) {
                try {
                    // Primero, vamos a ver qué contiene el token
                    var claims = jwtProvider.getClaimsFromToken(actualToken);
                    log.debug("Claims del token: {}", claims);
                    log.debug("ID del token: {}", claims.get("id"));
                    log.debug("Roles del token: {}", claims.get("roles"));
                    
                    // Obtener información del usuario desde el token
                    Integer userId = jwtProvider.getIdFromToken(token);
                    log.debug("ID de usuario obtenido del token: {}", userId);
                    
                    // Verificar que el userId no sea null
                    if (userId == null) {
                        log.error("El ID de usuario es null en el token");
                        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Map.of("valid", false, "message", "Token no contiene ID de usuario válido")));
                    }
                    
                    // Obtener username si es posible, sino usar un valor por defecto
                    String username;
                    try {
                        username = jwtProvider.getUsernameFromToken(token);
                        log.debug("Username obtenido del token: {}", username);
                    } catch (Exception e) {
                        log.debug("No se pudo obtener username del token, usando ID: {}", userId);
                        username = "user" + userId;
                    }
                    
                    // Crear respuesta con información del usuario usando HashMap para evitar valores null
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("id", userId);
                    userInfo.put("username", username != null ? username : "user" + userId);
                    
                    // Extraer roles reales del claim
                    Object rolesClaim = claims.get("roles");
                    userInfo.put("roles", rolesClaim); // Devuelve el claim real

                    // Si quieres devolver solo el primer rol como string:
                    String mainRole = "USER";
                    if (rolesClaim instanceof java.util.List<?> rolesList && !rolesList.isEmpty()) {
                        Object firstRole = rolesList.get(0);
                        if (firstRole instanceof java.util.Map<?,?> roleMap && roleMap.get("authority") != null) {
                            mainRole = roleMap.get("authority").toString();
                        }
                    }
                    userInfo.put("role", mainRole); // Devuelve el rol principal real
                    
                    userInfo.put("valid", true);
                    
                    log.debug("Token válido para usuario: {}", username);
                    return Mono.just(ResponseEntity.ok(userInfo));
                } catch (Exception e) {
                    log.error("Error al extraer información del token: {}", e.getMessage(), e);
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("valid", false);
                    errorResponse.put("message", "Error al procesar token: " + e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse));
                }
            } else {
                log.warn("Token inválido: {}", token);
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("valid", false);
                errorResponse.put("message", "Token inválido");
                return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse));
            }
        } catch (Exception e) {
            log.error("Error general al validar token: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("valid", false);
            errorResponse.put("message", "Error interno del servidor: " + e.getMessage());
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
        }
    }

    /**
     * Endpoint para validar el tipo de usuario
     * @param request Request con idUser y password (opcional)
     * @return Información del tipo de usuario
     */
    @PostMapping("/validate-user-type")
    public Mono<ResponseEntity<ValidateUserTypeResponse>> validateUserType(
            @RequestBody ValidateUserTypeRequest request) {
        
        log.debug("Validando tipo de usuario para idUser: {}", request.getIdUser());
        
        return userClientService.findById(request.getIdUser().intValue())
                .map(user -> {
                    // Determinar el tipo de usuario basado en los datos
                    String userType = determineUserType(user);
                    Boolean isUserInclub = user.isUserInclub();
                    
                    ValidateUserTypeResponse response = ValidateUserTypeResponse.builder()
                            .id(request.getIdUser())
                            .userType(userType)
                            .isUserInclub(isUserInclub)
                            .username(user.getUsername())
                            .message("Usuario validado exitosamente")
                            .build();
                    
                    log.debug("Usuario {} validado como tipo: {}", request.getIdUser(), userType);
                    return ResponseEntity.ok(response);
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ValidateUserTypeResponse.builder()
                                .id(request.getIdUser())
                                .userType("NOT_FOUND")
                                .isUserInclub(false)
                                .username(null)
                                .message("Usuario no encontrado")
                                .build()))
                .onErrorResume(error -> {
                    log.error("Error validando usuario {}: {}", request.getIdUser(), error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(ValidateUserTypeResponse.builder()
                                    .id(request.getIdUser())
                                    .userType("ERROR")
                                    .isUserInclub(false)
                                    .username(null)
                                    .message("Error interno del servidor: " + error.getMessage())
                                    .build()));
                });
    }

    /**
     * Determina el tipo de usuario basado en sus características
     */
    private String determineUserType(UserClientEntity user) {
        if (Boolean.TRUE.equals(user.isUserInclub())) {
            return "INCLUB_MEMBER";
        } else {
            return "REGULAR_USER";
        }
    }

    /**
     * Endpoint para obtener información de usuario por ID
     * @param userId ID del usuario
     * @param token Token de autenticación
     * @return Información del usuario
     */
    @GetMapping("/user/{userId}")
    public Mono<ResponseEntity<Object>> getUserInfo(
            @PathVariable Integer userId,
            @RequestHeader("Authorization") String token) {
        
        log.debug("Obteniendo información de usuario: {}", userId);
        
        try {
            // Validar token primero
            String actualToken = token.replace("Bearer ", "");
            
            if (!jwtProvider.validateToken(actualToken)) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("valid", false);
                errorResponse.put("message", "Token inválido");
                return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse));
            }
            
            // Aquí deberías obtener la información del usuario desde la BD
            // Por ahora retornamos un objeto básico
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", userId);
            userInfo.put("username", "user" + userId);
            userInfo.put("email", "user" + userId + "@example.com");
            userInfo.put("role", "USER");
            userInfo.put("active", true);
            
            log.debug("Información de usuario obtenida: {}", userId);
            return Mono.just(ResponseEntity.ok(userInfo));
            
        } catch (Exception e) {
            log.error("Error al obtener información de usuario {}: {}", userId, e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error interno del servidor");
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
        }
    }
} 