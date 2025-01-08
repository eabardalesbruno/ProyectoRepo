package com.proriberaapp.ribera.services.client.impl;

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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                            .flatMap(count -> {
                                if (count > 1 && count != totalPersons) {
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
    public Flux<CompanionsEntity> updateCompanion(Integer bookingId, List<CompanionsEntity> companionsEntities) {
        return Flux.fromIterable(companionsEntities)
                .flatMap(companion -> {
                    companion.setBookingId(bookingId);

                    if (companion.getCompanionId() != null) {
                        return companionsRepository.findByCompanionIdAndBookingId(companion.getCompanionId(), bookingId)
                                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                                        "No se encontró un acompañante con bookingId " + bookingId +
                                                " y companionId " + companion.getCompanionId())))
                                .flatMap(existingCompanion -> {
                                    existingCompanion.setFirstname(companion.getFirstname());
                                    existingCompanion.setLastname(companion.getLastname());
                                    existingCompanion.setTypeDocumentId(companion.getTypeDocumentId());
                                    existingCompanion.setDocumentNumber(companion.getDocumentNumber());
                                    existingCompanion.setCellphone(companion.getCellphone());
                                    existingCompanion.setEmail(companion.getEmail());
                                    existingCompanion.setCategory(companion.getCategory());
                                    existingCompanion.setBirthdate(companion.getBirthdate());
                                    existingCompanion.setGenderId(companion.getGenderId());
                                    existingCompanion.setCountryId(companion.getCountryId());

                                    if (existingCompanion.getBirthdate() != null) {
                                        Timestamp birthTime = existingCompanion.getBirthdate();
                                        LocalDate birthDate = birthTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                        int years = Period.between(birthDate, LocalDate.now()).getYears();
                                        existingCompanion.setYears(years);
                                    }

                                    return companionsRepository.save(existingCompanion);
                                });
                    } else {
                        return companionsRepository.save(companion)
                                .flatMap(newCompanion -> {
                                    if (newCompanion.getBirthdate() != null) {
                                        Timestamp birthTime = newCompanion.getBirthdate();
                                        LocalDate birthDate = birthTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                        int years = Period.between(birthDate, LocalDate.now()).getYears();
                                        newCompanion.setYears(years);
                                    }
                                    return companionsRepository.save(newCompanion);
                                });
                    }
                });
    }

    @Override
    public Flux<CompanionsEntity> updateMultipleCompanions(Integer bookingId, List<CompanionsEntity> companions) {
        return Flux.fromIterable(companions)
                .flatMap(companion -> updateCompanion(bookingId,companions))
                .collectList()
                .flatMapMany(updatedCompanions -> {
                    return validateTotalCompanions(bookingId, Flux.fromIterable(updatedCompanions))
                            .thenMany(Flux.fromIterable(updatedCompanions));
                });
    }
    }

