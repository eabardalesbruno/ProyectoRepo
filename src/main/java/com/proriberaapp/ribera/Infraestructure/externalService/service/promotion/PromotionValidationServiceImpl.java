package com.proriberaapp.ribera.Infraestructure.externalService.service.promotion;

import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Infraestructure.externalService.dtos.response.UserContextResponse;
import com.proriberaapp.ribera.Infraestructure.repository.UserAdminRepository;
import com.proriberaapp.ribera.Infraestructure.repository.UserPromoterRepository;
import com.proriberaapp.ribera.Domain.enums.StatesUser;
import com.proriberaapp.ribera.services.client.UserClientService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionValidationServiceImpl implements IPromotionValidationService {

    private final JwtProvider jwtProvider;
    private final UserClientService userClientService;
    private final UserPromoterRepository userPromoterRepository;
    private final UserAdminRepository userAdminRepository;

    @Override
    public Mono<UserContextResponse> validateTokenForPromotions(String authHeader) {
        try {
            // 1. Extraer token del header "Bearer {token}"
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Mono.error(new IllegalArgumentException("Header de autorización inválido"));
            }

            String token = authHeader.substring(7);

            // 2. Validar token
            if (!jwtProvider.validateToken(token)) {
                return Mono.error(new IllegalArgumentException("Token inválido"));
            }

            // 3. Obtener claims del token
            Claims claims = jwtProvider.getClaimsFromToken(token);
            Integer userId = claims.get("id", Integer.class);
            String username = claims.getSubject();

            // 4. Determinar tipo de usuario y obtener información completa
            return determineUserTypeAndGetInfo(claims, userId, username, authHeader);

        } catch (Exception e) {
            log.error("Error al validar token para promociones: {}", e.getMessage(), e);
            return Mono.error(new RuntimeException("Error al procesar token: " + e.getMessage()));
        }
    }

    private Mono<UserContextResponse> determineUserTypeAndGetInfo(Claims claims, Integer userId, String username, String authHeader) {
        // Obtener roles del token
        Object rolesClaim = claims.get("roles");
        String mainRole = extractMainRole(rolesClaim);
        
        // Determinar tipo de usuario basado en el rol
        if (isAdminRole(mainRole)) {
            return buildAdminResponse(userId, username, mainRole, claims);
        } else if (isPromoterRole(mainRole)) {
            return buildPromoterResponse(userId, username, claims);
        } else {
            // Es un cliente - necesitamos consultar BD para isUserInclub
            return buildClientResponse(userId, username, claims);
        }
    }

    private String extractMainRole(Object rolesClaim) {
        if (rolesClaim instanceof List<?> rolesList && !rolesList.isEmpty()) {
            Object firstRole = rolesList.get(0);
            if (firstRole instanceof Map<?, ?> roleMap && roleMap.get("authority") != null) {
                return roleMap.get("authority").toString();
            }
        }
        return "ROLE_USER"; // Default
    }

    private boolean isAdminRole(String role) {
        return "ROLE_SUPER_ADMIN".equals(role) || "ROLE_ADMIN".equals(role) || "ROLE_MANAGER".equals(role);
    }

    private boolean isPromoterRole(String role) {
        return "ROLE_PROMOTER".equals(role);
    }

    private Mono<UserContextResponse> buildAdminResponse(Integer userId, String username, String mainRole, Claims claims) {
        return userAdminRepository.findById(userId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario admin no encontrado")))
                .map(admin -> {
                    boolean isActive = admin.getStatus() == StatesUser.ACTIVE;
                    return UserContextResponse.builder()
                            .userId(String.valueOf(admin.getUserAdminId()))
                            .username(admin.getUsername() != null ? admin.getUsername() : username)
                            .email(admin.getEmail())
                            .role("ADMIN")
                            .isActive(isActive)
                            .isUserInclub(null)
                            .build();
                });
    }

    private Mono<UserContextResponse> buildPromoterResponse(Integer userId, String username, Claims claims) {
        return userPromoterRepository.findById(userId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario promotor no encontrado")))
                .map(promoter -> {
                    boolean isActive = promoter.getStatus() == StatesUser.ACTIVE;
                    return UserContextResponse.builder()
                            .userId(String.valueOf(promoter.getUserPromoterId()))
                            .username(promoter.getUsername() != null ? promoter.getUsername() : username)
                            .email(promoter.getEmail())
                            .role("PROMOTOR")
                            .isActive(isActive)
                            .documentNumber(promoter.getDocumentNumber())
                            .isUserInclub(null)
                            .build();
                });
    }

    private Mono<UserContextResponse> buildClientResponse(Integer userId, String username, Claims claims) {
        // cliente normal
        Mono<UserContextResponse> clientMono = userClientService.findById(userId)
                .map(user -> {
                    String role = user.isUserInclub() ? "SOCIO" : "CLIENTE";
                    return UserContextResponse.builder()
                            .userId(userId.toString())
                            .username(user.getUsername() != null ? user.getUsername() : username)
                            .email(user.getEmail())
                            .role(role)
                            .isActive(true)
                            .documentNumber(user.getDocumentNumber())
                            .isUserInclub(user.isUserInclub())
                            .build();
                });

        // Si no hay cliente, intentamos como promotor
        Mono<UserContextResponse> promoterFallback = userPromoterRepository.findById(userId)
                .map(promoter -> {
                    boolean isActive = promoter.getStatus() == StatesUser.ACTIVE;
                    return UserContextResponse.builder()
                            .userId(String.valueOf(promoter.getUserPromoterId()))
                            .username(promoter.getUsername() != null ? promoter.getUsername() : username)
                            .email(promoter.getEmail())
                            .role("PROMOTOR")
                            .isActive(isActive)
                            .documentNumber(promoter.getDocumentNumber())
                            .isUserInclub(null)
                            .build();
                });

        return clientMono.switchIfEmpty(
                promoterFallback.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")))
        ).doOnError(error -> log.error("Error al consultar usuario {}: {}", userId, error.getMessage()));
    }
}
