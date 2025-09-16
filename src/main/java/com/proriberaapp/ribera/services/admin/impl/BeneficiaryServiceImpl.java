// ...existing code...
// Extrae el nombre de la membresía del JSON de la API externa
// Extrae el nombre de la membresía del JSON de la API externa
package com.proriberaapp.ribera.services.admin.impl;

import org.springframework.web.reactive.function.client.WebClient;
import com.proriberaapp.ribera.Domain.dto.BeneficiaryDto;
import com.proriberaapp.ribera.Domain.dto.InclubUserDto;
import com.proriberaapp.ribera.services.admin.BeneficiaryService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.netty.http.client.HttpClient;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDate;

@Service
@Slf4j
public class BeneficiaryServiceImpl implements BeneficiaryService {
        @Override
        public Flux<com.proriberaapp.ribera.Domain.dto.MembershipResponse> getMembershipsByUser(String url) {
                return webClient.get()
                                .uri(url)
                                .retrieve()
                                .bodyToFlux(com.proriberaapp.ribera.Domain.dto.MembershipResponse.class)
                                .onErrorResume(e -> {
                                        log.error("Error consultando membresías para url {}: {}", url, e.getMessage());
                                        return Flux.empty();
                                });
        }

        // Nuevo método para paginación y filtro
        public Flux<BeneficiaryDto> getBeneficiariesPage(String nombre, int page, int size) {
                Flux<InclubUserDto> usuarios = consultarSociosDesdeInclub(nombre);
                int maxConcurrency = 10;
                return usuarios
                                .skip(page * size)
                                .take(size)
                                .flatMap(userDto -> {
                                        Integer idUser = userDto.getIdUser();
                                        String url = "https://adminpanelapi-dev.inclub.world/api/suscription/view/user/"
                                                        + idUser;
                                        return webClient.get()
                                                        .uri(url)
                                                        .retrieve()
                                                        .bodyToFlux(com.proriberaapp.ribera.Domain.dto.MembershipResponse.class)
                                                        .next()
                                                        .timeout(java.time.Duration.ofSeconds(3))
                                                        .onErrorResume(e -> {
                                                                log.error("Error consultando membresía para idUser {}: {}",
                                                                                idUser, e.getMessage());
                                                                return Mono.justOrEmpty(
                                                                                (com.proriberaapp.ribera.Domain.dto.MembershipResponse) null);
                                                        })
                                                        .map(membership -> {
                                                                String membershipName = (membership != null
                                                                                && membership.getPack() != null)
                                                                                                ? membership.getPack()
                                                                                                                .getName()
                                                                                                : null;
                                                                String creationDate = null;
                                                                if (userDto.getCreationDate() != null && userDto
                                                                                .getCreationDate().size() >= 6) {
                                                                        java.time.LocalDateTime ldt = java.time.LocalDateTime
                                                                                        .of(
                                                                                                        userDto.getCreationDate()
                                                                                                                        .get(0),
                                                                                                        userDto.getCreationDate()
                                                                                                                        .get(1),
                                                                                                        userDto.getCreationDate()
                                                                                                                        .get(2),
                                                                                                        userDto.getCreationDate()
                                                                                                                        .get(3),
                                                                                                        userDto.getCreationDate()
                                                                                                                        .get(4),
                                                                                                        userDto.getCreationDate()
                                                                                                                        .get(5));
                                                                        creationDate = ldt.format(
                                                                                        java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                                                                }
                                                                return BeneficiaryDto.builder()
                                                                                .id(userDto.getIdUser())
                                                                                .name(userDto.getName())
                                                                                .lastName(userDto.getLastName())
                                                                                .documentNumber(userDto
                                                                                                .getDocumentNumber())
                                                                                .creationDate(creationDate)
                                                                                .email(userDto.getEmail())
                                                                                .membershipName(membershipName)
                                                                                .build();
                                                        });
                                }, maxConcurrency)
                                .doOnComplete(() -> log.info("Fin de beneficiarios paginados"));
        }

        // WebClient que ignora certificados SSL (solo para pruebas)
        private final WebClient webClient = WebClient.builder()
                        .clientConnector(new ReactorClientHttpConnector(
                                        HttpClient.create()
                                                        .secure(spec -> spec.sslContext(
                                                                        SslContextBuilder.forClient().trustManager(
                                                                                        InsecureTrustManagerFactory.INSTANCE)))))
                        .build();

        @Override
        public Flux<InclubUserDto> consultarSociosDesdeInclub(String username) {
                String url = "https://adminpanelapi-dev.inclub.world/api/user/getListUsersOfAdmin/search";
                String bodyJson;
                if (username == null || username.trim().isEmpty()) {
                        bodyJson = "{" +
                                        "\"state\": -1," +
                                        "\"familyPackage\": -1," +
                                        "\"packageDetail\": -1," +
                                        "\"typeUser\": 1" +
                                        "}";
                } else {
                        bodyJson = "{" +
                                        "\"username\": \"" + username + "\"," +
                                        "\"state\": -1," +
                                        "\"familyPackage\": -1," +
                                        "\"packageDetail\": -1," +
                                        "\"typeUser\": 1" +
                                        "}";
                }
                log.info("[consultarSociosDesdeInclub] URL: {}", url);
                log.info("[consultarSociosDesdeInclub] Body: {}", bodyJson);
                return webClient.post()
                                .uri(url)
                                .header("Content-Type", "application/json")
                                .bodyValue(bodyJson)
                                .retrieve()
                                .bodyToFlux(InclubUserDto.class)
                                .onErrorResume(e -> {
                                        log.error("Error consultando usuarios: {}", e.getMessage());
                                        return Flux.empty();
                                })
                                .doOnNext(user -> log.info("[consultarSociosDesdeInclub] Usuario recibido: {}", user))
                                .doOnComplete(() -> log.info("[consultarSociosDesdeInclub] Fin de usuarios recibidos"));
        }

        @Override
        public Flux<BeneficiaryDto> getAllBeneficiaries() {
                log.info("Listando todos los beneficiarios desde API externa");
                String usernameFiltro = "";
                int maxConcurrency = 10;
                return consultarSociosDesdeInclub(usernameFiltro)
                                .flatMap(userDto -> {
                                        Integer idUser = userDto.getIdUser();
                                        String url = "https://adminpanelapi-dev.inclub.world/api/suscription/view/user/"
                                                        + idUser;
                                        return webClient.get()
                                                        .uri(url)
                                                        .retrieve()
                                                        .bodyToFlux(com.proriberaapp.ribera.Domain.dto.MembershipResponse.class)
                                                        .next()
                                                        .timeout(java.time.Duration.ofSeconds(3))
                                                        .onErrorResume(e -> {
                                                                log.error("Error consultando membresía para idUser {}: {}",
                                                                                idUser, e.getMessage());
                                                                return Mono.justOrEmpty(
                                                                                (com.proriberaapp.ribera.Domain.dto.MembershipResponse) null);
                                                        })
                                                        .map(membership -> {
                                                                String membershipName = (membership != null
                                                                                && membership.getPack() != null)
                                                                                                ? membership.getPack()
                                                                                                                .getName()
                                                                                                : null;
                                                                String creationDate = null;
                                                                if (userDto.getCreationDate() != null && userDto
                                                                                .getCreationDate().size() >= 6) {
                                                                        java.time.LocalDateTime ldt = java.time.LocalDateTime
                                                                                        .of(
                                                                                                        userDto.getCreationDate()
                                                                                                                        .get(0),
                                                                                                        userDto.getCreationDate()
                                                                                                                        .get(1),
                                                                                                        userDto.getCreationDate()
                                                                                                                        .get(2),
                                                                                                        userDto.getCreationDate()
                                                                                                                        .get(3),
                                                                                                        userDto.getCreationDate()
                                                                                                                        .get(4),
                                                                                                        userDto.getCreationDate()
                                                                                                                        .get(5));
                                                                        creationDate = ldt.format(
                                                                                        java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                                                                }
                                                                return BeneficiaryDto.builder()
                                                                                .id(userDto.getIdUser())
                                                                                .name(userDto.getName())
                                                                                .lastName(userDto.getLastName())
                                                                                .documentNumber(userDto
                                                                                                .getDocumentNumber())
                                                                                .creationDate(creationDate)
                                                                                .email(userDto.getEmail())
                                                                                .membershipName(membershipName)
                                                                                .build();
                                                        });
                                }, maxConcurrency);
        }

        @Override
        public Mono<BeneficiaryDto> createBeneficiary(BeneficiaryDto dto) {
                throw new UnsupportedOperationException("No soportado en fuente externa");
        }

        @Override
        public Mono<BeneficiaryDto> updateBeneficiary(Integer id, BeneficiaryDto dto) {
                throw new UnsupportedOperationException("No soportado en fuente externa");
        }

        @Override
        public Mono<Void> deleteBeneficiary(Integer id) {
                throw new UnsupportedOperationException("No soportado en fuente externa");
        }

        // @Override
        // public Mono<BeneficiaryDto> getBeneficiaryById(Integer id) {
        // return Mono.empty();
        // }

        @Override
        public Flux<BeneficiaryDto> filterBeneficiaries(String nombre, String membresia) {
                log.info("[filterBeneficiaries] Filtrando beneficiarios por name: {} y idMembership: {}", nombre,
                                membresia);
                if (nombre != null) {
                        int maxConcurrency = 10;
                        return consultarSociosDesdeInclub(nombre)
                                        .flatMap(userDto -> {
                                                log.info("[filterBeneficiaries] Usuario recibido: {}", userDto);
                                                Integer idUser = userDto.getIdUser();
                                                String url = "https://adminpanelapi-dev.inclub.world/api/suscription/view/user/"
                                                                + idUser;
                                                log.info("[filterBeneficiaries] Consultando membresía en URL: {} para idUser: {}",
                                                                url, idUser);
                                                return webClient.get()
                                                                .uri(url)
                                                                .retrieve()
                                                                .bodyToFlux(com.proriberaapp.ribera.Domain.dto.MembershipResponse.class)
                                                                .next()
                                                                .timeout(java.time.Duration.ofSeconds(3))
                                                                .onErrorResume(e -> {
                                                                        log.error("Error consultando membresía para idUser {}: {}",
                                                                                        idUser, e.getMessage());
                                                                        return Mono
                                                                                        .justOrEmpty((com.proriberaapp.ribera.Domain.dto.MembershipResponse) null);
                                                                })
                                                                .map(membership -> {
                                                                        String membershipName = (membership != null
                                                                                        && membership.getPack() != null)
                                                                                                        ? membership.getPack()
                                                                                                                        .getName()
                                                                                                        : null;
                                                                        String creationDate = null;
                                                                        if (userDto.getCreationDate() != null
                                                                                        && userDto.getCreationDate()
                                                                                                        .size() >= 6) {
                                                                                creationDate = java.time.LocalDateTime
                                                                                                .of(
                                                                                                                userDto.getCreationDate()
                                                                                                                                .get(0),
                                                                                                                userDto.getCreationDate()
                                                                                                                                .get(1),
                                                                                                                userDto.getCreationDate()
                                                                                                                                .get(2),
                                                                                                                userDto.getCreationDate()
                                                                                                                                .get(3),
                                                                                                                userDto.getCreationDate()
                                                                                                                                .get(4),
                                                                                                                userDto.getCreationDate()
                                                                                                                                .get(5))
                                                                                                .toString();
                                                                        }
                                                                        BeneficiaryDto dto = BeneficiaryDto.builder()
                                                                                        .id(userDto.getIdUser())
                                                                                        .name(userDto.getName())
                                                                                        .lastName(userDto.getLastName())
                                                                                        .documentNumber(userDto
                                                                                                        .getDocumentNumber())
                                                                                        .creationDate(creationDate)
                                                                                        .email(userDto.getEmail())
                                                                                        .membershipName(membershipName)
                                                                                        .build();
                                                                        log.info("[filterBeneficiaries] BeneficiaryDto construido: {}",
                                                                                        dto);
                                                                        return dto;
                                                                });
                                        }, maxConcurrency)
                                        .doOnComplete(() -> log
                                                        .info("[filterBeneficiaries] Fin de beneficiarios filtrados"));
                }
                return Flux.empty();
        }

        @Override
        public Mono<BeneficiaryDto> registrarVisita(Integer id) {
                throw new UnsupportedOperationException("No soportado en fuente externa");
        }

        @Override
        public Mono<BeneficiaryDto> registrarCheckin(Integer id) {
                throw new UnsupportedOperationException("No soportado en fuente externa");
        }

        @Override
        public Flux<BeneficiaryDto> getBeneficiariesByMembership(Integer id) {
                String url = "https://membershipapi-dev.inclub.world/api/v1/beneficiaries/subscription/" + id;
                return webClient.get()
                                .uri(url)
                                .retrieve()
                                .bodyToMono(com.fasterxml.jackson.databind.JsonNode.class)
                                .flatMapMany(json -> {
                                        com.fasterxml.jackson.databind.JsonNode dataNode = json.get("data");
                                        if (dataNode != null && dataNode.isArray()) {
                                                return Flux.fromIterable(dataNode)
                                                                .map(node -> BeneficiaryDto.builder()
                                                                                .id(node.has("idBeneficiary") && !node
                                                                                                .get("idBeneficiary")
                                                                                                .isNull()
                                                                                                                ? node.get("idBeneficiary")
                                                                                                                                .asInt()
                                                                                                                : null)
                                                                                .idSubscription(
                                                                                                node.has("idSubscription")
                                                                                                                && !node.get("idSubscription")
                                                                                                                                .isNull()
                                                                                                                                                ? node.get("idSubscription")
                                                                                                                                                                .asInt()
                                                                                                                                                : null)
                                                                                .userId(node.has("userId") && !node
                                                                                                .get("userId").isNull()
                                                                                                                ? node.get("userId")
                                                                                                                                .asInt()
                                                                                                                : null)
                                                                                .documentTypeId(
                                                                                                node.has("documentTypeId")
                                                                                                                && !node.get("documentTypeId")
                                                                                                                                .isNull()
                                                                                                                                                ? node.get("documentTypeId")
                                                                                                                                                                .asInt()
                                                                                                                                                : null)
                                                                                .residenceCountryId(node.has(
                                                                                                "residenceCountryId")
                                                                                                && !node.get("residenceCountryId")
                                                                                                                .isNull()
                                                                                                                                ? node.get("residenceCountryId")
                                                                                                                                                .asInt()
                                                                                                                                : null)
                                                                                .name(node.has("name") && !node.get(
                                                                                                "name")
                                                                                                .isNull() ? node.get(
                                                                                                                "name")
                                                                                                                .asText()
                                                                                                                : null)
                                                                                .lastName(node.has("lastName") && !node
                                                                                                .get("lastName")
                                                                                                .isNull()
                                                                                                                ? node.get("lastName")
                                                                                                                                .asText()
                                                                                                                : null)
                                                                                .gender(node.has("gender") && !node
                                                                                                .get("gender").isNull()
                                                                                                                ? node.get("gender")
                                                                                                                                .asText()
                                                                                                                : null)
                                                                                .email(node.has("email") && !node
                                                                                                .get("email").isNull()
                                                                                                                ? node.get("email")
                                                                                                                                .asText()
                                                                                                                : null)
                                                                                // Mapeo correcto: 'nroDocument' del
                                                                                // JSON se asigna a 'documentNumber' en
                                                                                // el DTO
                                                                                .documentNumber(node.has("nroDocument")
                                                                                                && !node.get("nroDocument")
                                                                                                                .isNull()
                                                                                                                                ? node.get("nroDocument")
                                                                                                                                                .asText()
                                                                                                                                : null)
                                                                                .birthDate(node.has("ageDate") && !node
                                                                                                .get("ageDate").isNull()
                                                                                                                ? node.get("ageDate")
                                                                                                                                .asText()
                                                                                                                : null)
                                                                                .status(node.has("status") && !node
                                                                                                .get("status").isNull()
                                                                                                                ? node.get("status")
                                                                                                                                .asInt()
                                                                                                                : null)
                                                                                .isAdult(node.has("isAdult") && !node
                                                                                                .get("isAdult").isNull()
                                                                                                                ? node.get("isAdult")
                                                                                                                                .asText()
                                                                                                                : null)
                                                                                .creationDate(node.has("creationDate")
                                                                                                && !node.get("creationDate")
                                                                                                                .isNull()
                                                                                                                                ? node.get("creationDate")
                                                                                                                                                .asText()
                                                                                                                                : null)
                                                                                .expirationDate(
                                                                                                node.has("expirationDate")
                                                                                                                && !node.get("expirationDate")
                                                                                                                                .isNull()
                                                                                                                                                ? node.get("expirationDate")
                                                                                                                                                                .asText()
                                                                                                                                                : null)
                                                                                .build());
                                        } else {
                                                return Flux.empty();
                                        }
                                })
                                .onErrorResume(e -> {
                                        log.error("Error consultando beneficiarios por suscripción {}: {}", id,
                                                        e.getMessage());
                                        return Flux.empty();
                                });
        }
}
