package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.dto.CompanionsDto;
import com.proriberaapp.ribera.Domain.entities.CompanionsEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BookingRepository;
import com.proriberaapp.ribera.Infraestructure.repository.CompanionsRepository;
import com.proriberaapp.ribera.Infraestructure.repository.FullDayRepository;
import com.proriberaapp.ribera.services.client.CompanionsService;
import com.proriberaapp.ribera.services.client.EmailService;
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
    private final EmailService emailService;
    private final FullDayRepository fullDayRepository;


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
        if (companions.getBirthdate() != null) {
            Timestamp birthTime = companions.getBirthdate();
            LocalDate birthDate = birthTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int years = Period.between(birthDate, LocalDate.now()).getYears();
            companions.setYears(years);
        }
        return addCompanionBooking(companions)
                .flatMap(savedCompanion -> {
                    if (Boolean.TRUE.equals(savedCompanion.isTitular())) {
                        return getCompanionsListForBooking(savedCompanion.getBookingId())
                                .filter(companionsList -> companionsList.size() > 1)
                                .flatMap(companionsList -> {
                                    int yearsValue = savedCompanion.getYears() != null ? savedCompanion.getYears() : 0;

                                    String emailBody = generatebody(
                                            savedCompanion.getFirstname(),
                                            savedCompanion.getLastname(),
                                            String.valueOf(savedCompanion.getTypeDocumentId()),
                                            yearsValue,
                                            savedCompanion.getDocumentNumber(),
                                            savedCompanion.getCellphone(),
                                            savedCompanion.getEmail(),
                                            companionsList
                                    );

                                    return sendSuccessEmail(savedCompanion.getEmail(), emailBody)
                                            .thenReturn(savedCompanion);
                                })
                                .switchIfEmpty(Mono.just(savedCompanion));
                    }
                    return Mono.just(savedCompanion);
                });
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
                                    } else{
                                        existingCompanion.setYears(0);
                                    }

                                    return companionsRepository.save(existingCompanion)
                                            .flatMap(savedCompanion -> {
                                                if (Boolean.TRUE.equals(savedCompanion.isTitular())) {
                                                    return getCompanionsListForBooking(savedCompanion.getBookingId())
                                                            .filter(companionsList -> companionsList.size() > 1)
                                                            .flatMap(companionsList -> {
                                                                String emailBody = generatebody(
                                                                        savedCompanion.getFirstname(),
                                                                        savedCompanion.getLastname(),
                                                                        String.valueOf(savedCompanion.getTypeDocumentId()),
                                                                        savedCompanion.getYears() != null ? savedCompanion.getYears() : 0,
                                                                        savedCompanion.getDocumentNumber(),
                                                                        savedCompanion.getCellphone(),
                                                                        savedCompanion.getEmail(),
                                                                        companionsList
                                                                );

                                                                return sendSuccessEmail(savedCompanion.getEmail(), emailBody)
                                                                        .thenReturn(savedCompanion);
                                                            })
                                                            .switchIfEmpty(Mono.just(savedCompanion));
                                                }
                                                return Mono.just(savedCompanion);
                                            });
                                });
                    } else {
                        CompanionsEntity newCompanion = new CompanionsEntity();
                        newCompanion.setFirstname(companion.getFirstname());
                        newCompanion.setLastname(companion.getLastname());
                        newCompanion.setTypeDocumentId(companion.getTypeDocumentId());
                        newCompanion.setDocumentNumber(companion.getDocumentNumber());
                        newCompanion.setCellphone(companion.getCellphone());
                        newCompanion.setEmail(companion.getEmail());
                        newCompanion.setCategory(companion.getCategory());
                        newCompanion.setBirthdate(companion.getBirthdate());
                        newCompanion.setGenderId(companion.getGenderId());
                        newCompanion.setCountryId(companion.getCountryId());
                        newCompanion.setBookingId(bookingId);
                        newCompanion.setCompanionId(null);

                        if (newCompanion.getBirthdate() != null) {
                            Timestamp birthTime = newCompanion.getBirthdate();
                            LocalDate birthDate = birthTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            int years = Period.between(birthDate, LocalDate.now()).getYears();
                            newCompanion.setYears(years);
                        } else {
                            newCompanion.setYears(0);
                        }

                        return companionsRepository.save(newCompanion)
                                .flatMap(savedCompanion -> {
                                    if (Boolean.TRUE.equals(savedCompanion.isTitular())) {
                                        return getCompanionsListForBooking(savedCompanion.getBookingId())
                                                .filter(companionsList -> companionsList.size() > 1)
                                                .flatMap(companionsList -> {
                                                    String emailBody = generatebody(
                                                            savedCompanion.getFirstname(),
                                                            savedCompanion.getLastname(),
                                                            String.valueOf(savedCompanion.getTypeDocumentId()),
                                                            savedCompanion.getYears() != null ? savedCompanion.getYears() : 0,
                                                            savedCompanion.getDocumentNumber(),
                                                            savedCompanion.getCellphone(),
                                                            savedCompanion.getEmail(),
                                                            companionsList
                                                    );
                                                    return sendSuccessEmail(savedCompanion.getEmail(), emailBody)
                                                            .thenReturn(savedCompanion);
                                                })
                                                .switchIfEmpty(Mono.just(savedCompanion));
                                    }
                                    return Mono.just(savedCompanion);
                                });
                    }
                });
    }

    @Override
    public Flux<CompanionsEntity> getCompanionsByFulldayId(Integer fulldayId) {
        return companionsRepository.findByFullDayId(fulldayId);
    }

    @Override
    public Mono<CompanionsEntity> addCompanionFullday(CompanionsEntity companionsEntity) {
        return companionsRepository.save(companionsEntity);
    }

    @Override
    public Mono<Void> validateTotalCompanionsFullday(Integer fulldayId, Flux<CompanionsEntity> companionsEntity) {
        return fullDayRepository.findById(fulldayId)
                .flatMap(fullDay -> {
                    return companionsEntity.count()
                            .flatMap(count -> {
                                if (count > 1) {
                                    return Mono.error(new IllegalArgumentException(
                                            "El total de acompañantes no coincide con el total de personas en el FullDay"));
                                }
                                return Mono.empty();
                            });
                });
    }

    @Override
    public Flux<CompanionsEntity> updateMultipleCompanionsFullday(Integer fulldayId, List<CompanionsEntity> companions) {
        return null;
    }

    public Mono<List<CompanionsEntity>> getCompanionsListForFullDay(Integer fulldayid) {
        return companionsRepository.findByFullDayId(fulldayid)
                .collectList();
    }

    @Override
    public Flux<CompanionsEntity> calculateAgeAndSaveFullDay(List<CompanionsEntity> companionsEntities) {
        if (companionsEntities == null || companionsEntities.isEmpty()) {
            return Flux.error(new IllegalArgumentException("La lista de acompañantes no puede estar vacía."));
        }
        long titularCount = companionsEntities.stream()
                .filter(companion -> companion.isTitular())
                .count();
        if (titularCount != 1) {
            return Flux.error(new IllegalArgumentException("Debe haber exactamente un titular en la lista."));
        }
        int fulldayId = companionsEntities.get(0).getFulldayid();
        boolean allSameFullDayId = companionsEntities.stream()
                .allMatch(companion -> companion.getFulldayid().equals(fulldayId));

        if (!allSameFullDayId) {
            return Flux.error(new IllegalArgumentException("Todos los acompañantes deben tener el mismo fulldayid."));
        }
        List<CompanionsEntity> updatedCompanions = companionsEntities.stream()
                .map(companion -> {
                    if (companion.getBirthdate() != null) {
                        LocalDate birthDate = companion.getBirthdate().toInstant()
                                .atZone(ZoneId.systemDefault()).toLocalDate();
                        int years = Period.between(birthDate, LocalDate.now()).getYears();
                        companion.setYears(years);
                    }
                    return companion;
                })
                .collect(Collectors.toList());
        CompanionsEntity titular = updatedCompanions.stream()
                .filter(CompanionsEntity::isTitular)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("El titular debe tener todos los campos completos."));

        if (titular.getFirstname() == null || titular.getLastname() == null || titular.getBirthdate() == null) {
            return Flux.error(new IllegalArgumentException("El titular debe tener nombre, apellido y fecha de nacimiento."));
        }
        return Flux.fromIterable(updatedCompanions)
                .flatMap(this::addCompanionFullday)
                .collectList()
                .flatMapMany(savedCompanions -> {
                    if (titular != null) {
                        return getCompanionsListForFullDay(titular.getFulldayid())
                                .filter(companionsList -> companionsList.size() > 1)
                                .flatMapMany(companionsList -> {
                                    int yearsValue = titular.getYears() != null ? titular.getYears() : 0;

                                    String emailBody = generatebody(
                                            titular.getFirstname(),
                                            titular.getLastname(),
                                            String.valueOf(titular.getTypeDocumentId()),
                                            yearsValue,
                                            titular.getDocumentNumber(),
                                            titular.getCellphone(),
                                            titular.getEmail(),
                                            companionsList
                                    );

                                    return sendSuccessEmail(titular.getEmail(), emailBody)
                                            .thenMany(Flux.fromIterable(savedCompanions));
                                })
                                .switchIfEmpty(Flux.fromIterable(savedCompanions));
                    }
                    return Flux.fromIterable(savedCompanions);
                });
    }


    @Override
    public Flux<CompanionsEntity> updateMultipleCompanions(Integer bookingId, List<CompanionsEntity> companions) {
        return Flux.fromIterable(companions)
                .flatMap(companion -> updateCompanion(bookingId, companions))
                .collectList()
                .flatMapMany(updatedCompanions -> {
                    return validateTotalCompanions(bookingId, Flux.fromIterable(updatedCompanions))
                            .thenMany(Flux.fromIterable(updatedCompanions));
                });
    }


    private Mono<List<CompanionsEntity>> getCompanionsListForBooking(Integer bookingId) {
        return companionsRepository.findByBookingId(bookingId)
                .collectList();
    }

    private String generatebody(String titularName, String titularLastName, String titulartypeDocument, int titularAge, String titularDocument,
                                String titularCellphone, String titularEmail, List<CompanionsEntity> companions) {
        StringBuilder companionsHtml = new StringBuilder();
        boolean hasCompanion = false;
        for (CompanionsEntity companion : companions) {
            if (Boolean.TRUE.equals(companion.isTitular())) {
                continue;
            }
            if (companion.getFirstname() != null && !companion.getFirstname().isEmpty()) {
                hasCompanion = true;
                String gender = (companion.getGenderId() != null) ? (companion.getGenderId() == 1 ? "Masculino" : (companion.getGenderId() == 2 ? "Femenino" : " ")) : " ";
                String documentType = (companion.getTypeDocumentId() != null) ? (companion.getTypeDocumentId() == 1 ? "DNI" : (companion.getTypeDocumentId() == 2 ? "RUC" : " ")) : " ";
                String age = (companion.getYears() != null) ? String.valueOf(companion.getYears()) : "----";

                companionsHtml.append("<div class=\"companion\">")
                        .append("<p><strong>Nombre:</strong> ").append(companion.getFirstname()).append(" ").append(companion.getLastname()).append("</p>")
                        .append("<p><strong>Tipo de Documento:</strong> ").append(documentType).append("</p>")
                        .append("<p><strong>Número de Documento:</strong> ").append(companion.getDocumentNumber()).append("</p>")
                        .append("<p><strong>Género:</strong> ").append(gender).append("</p>")
                        .append("<p><strong>Edad:</strong> ").append(age).append("</p>")
                        .append("</div>");
            }
        }
        String titularGender = titularDocument != null && titularDocument.equals("1") ? "Masculino" : "Femenino";
        String titularDocumentType = titulartypeDocument.equals("1") ? "DNI" : "RUC";
        String titularAgeValue = titularAge > 0 ? String.valueOf(titularAge) : "----";

        String html = "<html lang=\"es\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Detalles de Reserva</title>" +
                "    <style>" +
                "        body {width: 100%;background: rgb(246, 247, 251);padding-bottom: 40px;padding-top: 40px;font-family: Arial, sans-serif;margin: 0;}" +
                "        .button {width: 90%;display: inline-block;padding: 10px;background-color: #025928;color: white !important;text-align: center;text-decoration: none;border-radius: 0px;}" +
                "        .card {background-color: rgb(246, 247, 251);padding: 24px;}" +
                "        .container, .footer-message {width: 100%;max-width: 900px;margin: 0 auto;box-sizing: border-box;}" +
                "        .header {position: relative;}" +
                "        .header img.banner {width: 100%;height: auto;border-top-left-radius: 8px;border-top-right-radius: 8px;}" +
                "        .header img.logo {width: 105px;top: 25.5px;right: 22px;position: absolute;}" +
                "        .body {padding: 40px;box-sizing: border-box;font-family: 'Product Sans', sans-serif;}" +
                "        .footer-message {background-color: white;padding: 24px 40px;border-radius: 8px;margin: 20px auto;text-align: center;}" +
                "        .font-italic {font-style: italic;}" +
                "        .font-size {font-size: 16px;}" +
                "        .extra-style {color: #333;font-weight: bold;}" +
                "        .img { max-width: 100%;height: auto;}" +
                "        .check-in {margin: 0;font-size: 12px;color: #216D42;font-weight: 400;}" +
                "        .room-name {margin: 0;font-size: 20px;}" +
                "        p.no-margin {margin: 0;}" +
                "        .container-data {vertical-align: baseline;font-size: 14px;}" +
                "        .container-img {width: 433px;padding-right: 16px;}" +
                "        .container-img .img {width: 433px;}" +
                "        .table-layout {font-family: 'Product Sans', sans-serif;width: 100%;}" +
                "        .hr {border: 1px solid #E1E1E1;margin: 0;}" +
                "        .font {font-size: 16px;font-family: 'Product Sans', sans-serif;}" +
                "        .strong-text {color: #384860;font-style: italic;}" +
                "        .container {width: 100%;max-width: 600px;margin: 20px auto;padding: 20px;background: white;border-radius: 10px;box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);}" +
                "        .section {margin-bottom: 20px;}" +
                "        h2 {color: #0d5f05;}" +
                "        p {margin: 5px 0;}" +
                "        .companion {margin-bottom: 10px;padding: 10px;border: 1px solid #ddd;border-radius: 5px;}" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <table class=\"container\" cellpadding=\"0\" cellspacing=\"0\">" +
                "        <tr>" +
                "            <td class=\"header\">" +
                "                <img class=\"banner\" src=\"https://s3.us-east-2.amazonaws.com/backoffice.documents/email/panoramica_resort.png\" alt=\"Bienvenido\"/>" +
                "            </td>" +
                "        </tr>" +
                "        <tr>" +
                "            <td class=\"body\">" +
                "                <p class=\"font\">Estimado(a), <strong>" + titularName  + titularLastName + "</strong></p>" +
                "                <p class=\"font\">El presente es para informar que se completó exitosamente el registro de sus acompañantes" +
                "                <div class=\"card\">" +
                "                    <table class=\"table-layout\">" +
                "                        <tbody>" +
                "                            <tr>" +
                "                                <td width=\"16\"></td>" +
                "                                <td class=\"container-data\">" +
                "                                    <table width=\"100%\" style=\"box-sizing: border-box;\">" +
                "                                        <div class=\"container\">" +
                "                                            <h2>Información del Titular</h2>" +
                "                                            <div class=\"section\">" +
                "                                                <p><strong>Nombre Completo:</strong> " + titularName + " " + titularLastName + "</p>" +
                "                                                <p><strong>Edad:</strong> " + titularAgeValue + "</p>" +
                "                                                <p><strong>Tipo de Documento:</strong> " + titularDocumentType + "</p>" +
                "                                                <p><strong>Número de Documento:</strong> " + titularDocument + "</p>" +
                "                                                <p><strong>Celular:</strong> " + titularCellphone + "</p>" +
                "                                                <p><strong>Email:</strong> " + titularEmail + "</p>" +
                "                                            </div>" +
                "                                        <h2>Acompañantes</h2>" +
                "                                        <div class=\"section\">" +
                                                            (hasCompanion ? companionsHtml.toString() : "<p>No se registraron acompañantes.</p>") +
                "                                         </div>" +
                "                                        </div>" +
                "                                    </table>" +
                "                                </td>" +
                "                            </tr>" +
                "                        </tbody>" +
                "                    </table>" +
                "                </div>" +
                "            </td>" +
                "        </tr>" +
                "    </table>" +
                "</body>" +
                "</html>";
        return html;
    }

    private Mono<Void> sendSuccessEmail(String email, String emailBody) {
        if (email == null || email.isEmpty()) {
            return Mono.error(new IllegalArgumentException("El email proporcionado no es válido."));
        }

        return emailService.sendEmail(email, "Confirmación de Registro de Acompañantes", emailBody)
                .doOnSuccess(v -> System.out.println("Correo enviado correctamente a: " + email))
                .doOnError(e -> System.err.println("Error al enviar el correo: " + e.getMessage()));
    }

}