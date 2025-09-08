
// Extrae el nombre de la membresía del JSON de la API externa
// Extrae el nombre de la membresía del JSON de la API externa
package com.proriberaapp.ribera.services.admin.impl;

import org.springframework.web.reactive.function.client.WebClient;

import com.proriberaapp.ribera.Domain.dto.BeneficiaryDto;
import com.proriberaapp.ribera.Domain.entities.BeneficiaryEntity;
import com.proriberaapp.ribera.services.admin.BeneficiaryService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.time.Period;

@Service
@Slf4j
public class BeneficiaryServiceImpl implements BeneficiaryService {
    // Extrae el nombre de la membresía del JSON de la API externa
    private String extractMembershipName(String json) {
        try {
            int packIdx = json.indexOf("\"pack\"");
            if (packIdx != -1) {
                int nameIdx = json.indexOf("\"name\"", packIdx);
                if (nameIdx != -1) {
                    int colonIdx = json.indexOf(":", nameIdx);
                    int commaIdx = json.indexOf(",", colonIdx);
                    return json.substring(colonIdx + 2, commaIdx - 1);
                }
            }
        } catch (Exception e) {
            log.warn("No se pudo extraer el nombre de la membresía: {}", e.getMessage());
        }
        return null;
    }

    private final WebClient webClient = WebClient.builder().build();

    @Override
    public Flux<com.proriberaapp.ribera.Domain.dto.InclubUserDto> consultarSociosDesdeInclub(String username) {
        String url = "https://adminpanelapi-dev.inclub.world/api/user/getListUsersOfAdmin/search";
        String bodyJson = "{" +
                "\"username\": \"" + username + "\"," +
                "\"state\": -1," +
                "\"familyPackage\": -1," +
                "\"packageDetail\": -1," +
                "\"typeUser\": 1" +
                "}";
        return webClient.post()
                .uri(url)
                .header("Content-Type", "application/json")
                .bodyValue(bodyJson)
                .retrieve()
                .bodyToFlux(com.proriberaapp.ribera.Domain.dto.InclubUserDto.class);
    }

    private final com.proriberaapp.ribera.Infraestructure.repository.BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryServiceImpl(
            com.proriberaapp.ribera.Infraestructure.repository.BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
    }

    @Override
    public Mono<BeneficiaryDto> createBeneficiary(BeneficiaryDto dto) {
        BeneficiaryEntity entity = toEntity(dto);
        log.info("Intentando guardar beneficiario: {}", entity);
        return beneficiaryRepository.save(entity)
                .doOnSuccess(saved -> log.info("Beneficiario guardado: {}", saved))
                .doOnError(error -> log.error("Error al guardar beneficiario", error))
                .map(this::toDto);
    }

    @Override
    public Mono<BeneficiaryDto> updateBeneficiary(Integer id, BeneficiaryDto dto) {
        return beneficiaryRepository.findById(id)
                .flatMap(existing -> {
                    BeneficiaryEntity updated = toEntity(dto);
                    updated.setId(id);
                    log.info("Actualizando beneficiario id {}: {}", id, updated);
                    return beneficiaryRepository.save(updated)
                            .doOnSuccess(saved -> log.info("Beneficiario actualizado: {}", saved))
                            .doOnError(error -> log.error("Error al actualizar beneficiario", error));
                })
                .map(this::toDto);
    }

    @Override
    public Mono<Void> deleteBeneficiary(Integer id) {
        log.info("Eliminando beneficiario id {}", id);
        return beneficiaryRepository.deleteById(id)
                .doOnSuccess(v -> log.info("Beneficiario eliminado id {}", id))
                .doOnError(error -> log.error("Error al eliminar beneficiario", error));
    }

    @Override
    public Mono<BeneficiaryDto> getBeneficiaryById(Integer id) {
        log.info("Buscando beneficiario id {}", id);
        return beneficiaryRepository.findById(id)
                .doOnError(error -> log.error("Error al buscar beneficiario", error))
                .map(this::toDto);
    }

    @Override
    public Flux<BeneficiaryDto> getAllBeneficiaries() {
        log.info("Listando todos los beneficiarios");
        return beneficiaryRepository.findAll()
                .doOnError(error -> log.error("Error al listar beneficiarios", error))
                .map(this::toDto);
    }

    @Override
    // Extrae el nombre de la membresía del JSON de la API externa
    private String extractMembershipName(String json) {
        try {
            int packIdx = json.indexOf("\"pack\"");
            if (packIdx != -1) {
                int nameIdx = json.indexOf("\"name\"", packIdx);
                if (nameIdx != -1) {
                    int colonIdx = json.indexOf(":", nameIdx);
                    int commaIdx = json.indexOf(",", colonIdx);
                    return json.substring(colonIdx + 2, commaIdx - 1);
                }
            }
        } catch (Exception e) {
            log.warn("No se pudo extraer el nombre de la membresía: {}", e.getMessage());
        }
        return null;
    }

    public Flux<BeneficiaryDto> filterBeneficiaries(String nombre, String membresia) {
        log.info("Filtrando beneficiarios por name: {} y idMembership: {}", nombre, membresia);
        if (nombre != null) {
            // 1. Consultar API de usuarios para obtener idUser
            return consultarSociosDesdeInclub(nombre)
                    .flatMap(userDto -> {
                        Integer idUser = userDto.getUserId();
                        // 2. Consultar API de membresía usando idUser
                        String url = "https://adminpanelapi-dev.inclub.world/api/suscription/view/user/" + idUser;
                        return webClient.get()
                                .uri(url)
                                .retrieve()
                                .bodyToMono(String.class)
                                .map(json -> {
                                    String membershipName = extractMembershipName(json);
                                    return beneficiaryRepository.findByNameContainingIgnoreCase(nombre)
                                            .map(entity -> {
                                                BeneficiaryDto dto = toDto(entity);
                                                dto.setMembershipName(membershipName);
                                                return dto;
                                            });
                                })
                                .flatMapMany(flux -> flux);
                    });
        } else if (membresia != null) {
            try {
                Integer idMem = Integer.valueOf(membresia);
                return beneficiaryRepository.findByIdMembership(idMem).map(this::toDto);
            } catch (NumberFormatException ex) {
                return Flux.empty();
            }
        } else {
            return getAllBeneficiaries();
        }
    }

    private BeneficiaryEntity toEntity(BeneficiaryDto dto) {
        BeneficiaryEntity e = new BeneficiaryEntity();
        e.setId(dto.getId());
        e.setName(dto.getName());
        e.setLastName(dto.getLastName());
        e.setDocumentNumber(dto.getDocumentNumber());
        e.setBirthDate(dto.getBirthDate());
        e.setEmail(dto.getEmail());
        e.setVisits(dto.getVisits());
        e.setIdMembership(dto.getIdMembership());
        e.setUsername(dto.getUsername());
        e.setStatus(dto.getStatus());
        e.setLastCheckin(dto.getLastCheckin());
        e.setCreationDate(dto.getCreationDate());
        return e;
    }

    private BeneficiaryDto toDto(BeneficiaryEntity entity) {
        Integer age = null;
        if (entity.getBirthDate() != null) {
            try {
                age = Period.between(entity.getBirthDate(), LocalDate.now()).getYears();
            } catch (Exception ex) {
                log.warn("No se pudo calcular la edad para birthDate {}: {}", entity.getBirthDate(), ex.getMessage());
            }
        }
        return BeneficiaryDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .lastName(entity.getLastName())
                .documentNumber(entity.getDocumentNumber())
                .birthDate(entity.getBirthDate())
                .age(age)
                .email(entity.getEmail())
                .visits(entity.getVisits())
                .idMembership(entity.getIdMembership())
                .username(entity.getUsername())
                .status(entity.getStatus())
                .lastCheckin(entity.getLastCheckin())
                .creationDate(entity.getCreationDate())
                .build();
    }

    @Override
    public Mono<BeneficiaryDto> registrarVisita(Integer id) {
        return beneficiaryRepository.findById(id)
                .flatMap(entity -> {
                    entity.setVisits(entity.getVisits() == null ? 1 : entity.getVisits() + 1);
                    return beneficiaryRepository.save(entity);
                })
                .map(this::toDto);
    }

    @Override
    public Mono<BeneficiaryDto> registrarCheckin(Integer id) {
        return beneficiaryRepository.findById(id)
                .flatMap(entity -> {
                    entity.setLastCheckin(java.time.LocalDateTime.now());
                    return beneficiaryRepository.save(entity);
                })
                .map(this::toDto);
    }
}
