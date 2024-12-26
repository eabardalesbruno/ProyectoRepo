package com.proriberaapp.ribera.services.client.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.proriberaapp.ribera.Domain.dto.CompanionsDto;
import com.proriberaapp.ribera.Domain.entities.CompanionsEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BookingRepository;
import com.proriberaapp.ribera.Infraestructure.repository.CompanionsRepository;
import com.proriberaapp.ribera.services.client.CompanionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class CompanionServiceImpl implements CompanionsService {

    private final CompanionsRepository companionsRepository;
    private final BookingRepository bookingRepository;
    @Value("${url.api.dni}")
    private String urlApiDni;
    @Value("${url.api.ruc.token}")
    private String tokenApiDni;


    @Override
    public Flux<CompanionsEntity> getCompanionsByBookingId(Integer bookingId) {
        return companionsRepository.findByBookingId(bookingId);
    }

    @Override
    public Mono<CompanionsEntity> addCompanionBooking(CompanionsEntity companionsEntity) {
        return companionsRepository.save(companionsEntity);
    }

    @Override
    public Mono<Void> validateTotalCompanions(Integer bookingId, Flux<CompanionsEntity> companionsEntity) {
        return bookingRepository.findByBookingId(bookingId)
                .flatMap(booking -> {
                    int totalPersons = booking.getNumberAdults() +
                            booking.getNumberChildren() +
                            booking.getNumberBabies() +
                            booking.getNumberAdultsExtra() +
                            booking.getNumberAdultsMayor();

                    return companionsEntity.count()
                            .flatMap(count ->{
                                if(count != totalPersons){
                                    return Mono.error(new IllegalArgumentException(
                                            "El total de acompañantes no coincide con el total de personas en la reserva"));
                                }
                                return Mono.empty();
                            });
                });
    }

    @Override
    public Mono<CompanionsDto> fetchCompanionByDni(String dni) {
        String url = urlApiDni + "?numero=" + dni;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Authorization", "Bearer " + tokenApiDni);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        Map<String, Object> body = response.getBody();
        if (body != null && body.containsKey("nombres") && body.containsKey("apellidoPaterno") && body.containsKey("apellidoMaterno")) {
            String firstName = (String) body.get("nombres");
            String lastName = (String) body.get("apellidoPaterno") + " " + body.get("apellidoMaterno");
            return Mono.just(new CompanionsDto(firstName, lastName, dni, 1, 176));
        } else {
            throw new IllegalArgumentException("No se encontró información del DNI");
        }
    }

    @Override
    public Mono<CompanionsEntity> calculateAgeandSave(CompanionsEntity companions) {
        if(companions.getBirthdate() != null){
            Timestamp birthTime = companions.getBirthdate();
            LocalDate birthDate = birthTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int years = Period.between(birthDate, LocalDate.now()).getYears();
            companions.setYears(years);
        }
        return addCompanionBooking(companions);
    }

    @Override
    public Mono<CompanionsEntity> updateCompanion(Integer bookingId, CompanionsEntity updatedCompanion) {
        return companionsRepository.findByBookingIdAndDocumentNumber(bookingId, updatedCompanion.getDocumentNumber())
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        "No se encontró el acompañante con el documento " + updatedCompanion.getDocumentNumber())))
                .flatMap(existingCompanion -> {

                    updatedCompanion.setCompanionId(existingCompanion.getCompanionId());
                    updatedCompanion.setBookingId(existingCompanion.getBookingId());
                    updatedCompanion.setTitular(existingCompanion.isTitular());

                    existingCompanion.setCategory(updatedCompanion.getCategory());
                    existingCompanion.setFirstname(updatedCompanion.getFirstname());
                    existingCompanion.setLastname(updatedCompanion.getLastname());
                    existingCompanion.setTypeDocumentId(updatedCompanion.getTypeDocumentId());
                    existingCompanion.setDocumentNumber(updatedCompanion.getDocumentNumber());
                    existingCompanion.setGenderId(updatedCompanion.getGenderId());
                    existingCompanion.setCountryId(updatedCompanion.getCountryId());
                    existingCompanion.setCellphone(updatedCompanion.getCellphone());
                    existingCompanion.setEmail(updatedCompanion.getEmail());

                    if(updatedCompanion.getBirthdate() != null) {
                        existingCompanion.setBirthdate(updatedCompanion.getBirthdate());
                        LocalDate birthDate = updatedCompanion.getBirthdate()
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        int years = Period.between(birthDate, LocalDate.now()).getYears();
                        existingCompanion.setYears(years);
                    }

                    return companionsRepository.save(existingCompanion);
                });
    }

    @Override
    public Flux<CompanionsEntity> updateMultipleCompanions(Integer bookingId, List<CompanionsEntity> companions) {
        return Flux.fromIterable(companions)
                .flatMap(companion -> updateCompanion(bookingId, companion))
                .collectList()
                .flatMapMany(updatedCompanions -> {

                    return validateTotalCompanions(bookingId, Flux.fromIterable(updatedCompanions))
                            .thenMany(Flux.fromIterable(updatedCompanions));
                });
    }
    }

