package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/internal")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InternalController {

    @Autowired
    private UserClientRepository userClientRepository;

    /**
     * Endpoint interno para obtener el email de un usuario
     * No requiere autenticación ya que es para uso interno entre microservicios
     */
    @GetMapping("/user/{id}/email")
    public Mono<String> getUserEmail(@PathVariable Integer id) {
        log.info("Solicitud interna para obtener email del usuario: {}", id);
        
        return userClientRepository.findById(id)
                .map(user -> {
                    log.info("Email encontrado para usuario {}: {}", id, user.getEmail());
                    return user.getEmail();
                })
                .switchIfEmpty(Mono.fromSupplier(() -> {
                    log.warn("Usuario {} no encontrado", id);
                    throw new RuntimeException("Usuario no encontrado");
                }))
                .doOnError(error -> log.error("Error obteniendo email para usuario {}: {}", id, error.getMessage()));
    }
} 