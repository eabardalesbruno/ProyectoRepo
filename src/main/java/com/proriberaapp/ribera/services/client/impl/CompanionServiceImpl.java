package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.dto.CompanionsDto;
import com.proriberaapp.ribera.Domain.entities.CompanionsEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BookingRepository;
import com.proriberaapp.ribera.Infraestructure.repository.CompanionsRepository;
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
                                .flatMap(companionsList -> {
                                    int yearsValue = savedCompanion.getYears() != null ? Integer.parseInt(String.valueOf(savedCompanion.getYears())) : 0;

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
                                });
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
                                                            });
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
                        }else{
                            newCompanion.setYears(0);
                        }

                        return companionsRepository.save(newCompanion)
                                .flatMap(savedCompanion -> {
                                    if (Boolean.TRUE.equals(savedCompanion.isTitular())) {
                                        return getCompanionsListForBooking(savedCompanion.getBookingId())
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
                                                });
                                    }
                                    return Mono.just(savedCompanion);
                                });
                    }
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
        for (CompanionsEntity companion : companions) {
            if (Boolean.TRUE.equals(companion.isTitular())) {
                continue;
            }

            String gender = companion.getGenderId() == 1 ? "Masculino" : (companion.getGenderId() == 2 ? "Femenino" : "Indeterminado");
            String documentType = companion.getTypeDocumentId() == 1 ? "DNI" : (companion.getTypeDocumentId() == 2 ? "RUC" : "Indeterminado");
            String age = (companion.getYears() != null) ? String.valueOf(companion.getYears()) : "----";

            companionsHtml.append("<div class=\"companion\">")
                    .append("<p><strong>Nombre:</strong> ").append(companion.getFirstname()).append(" ").append(companion.getLastname()).append("</p>")
                    .append("<p><strong>Tipo de Documento:</strong> ").append(documentType).append("</p>")
                    .append("<p><strong>Número de Documento:</strong> ").append(companion.getDocumentNumber()).append("</p>")
                    .append("<p><strong>Género:</strong> ").append(gender).append("</p>")
                    .append("<p><strong>Edad:</strong> ").append(age).append("</p>")
                    .append("</div>");
        }

        String titularGender = titularDocument != null && titularDocument.equals("1") ? "Masculino" : "Femenino";
        String titularDocumentType = titulartypeDocument.equals("1") ? "DNI" : "RUC";
        String titularAgeValue = titularAge > 0 ? String.valueOf(titularAge) : "----";

        String html = "<html lang=\"es\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Detalles de Reserva</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            width: 100%;\n" +
                "            background: rgb(246, 247, 251);\n" +
                "            padding-bottom: 40px;\n" +
                "            padding-top: 40px;\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            margin: 0;\n" +
                "        }\n" +
                "        .button {\n" +
                "            width: 90%;\n" +
                "            display: inline-block;\n" +
                "            padding: 10px;\n" +
                "            background-color: #025928;\n" +
                "            color: white !important;\n" +
                "            text-align: center;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 0px;\n" +
                "        }\n" +
                "        .card {\n" +
                "            background-color: rgb(246, 247, 251);\n" +
                "            padding: 24px;\n" +
                "        }\n" +
                "        .container, .footer-message {\n" +
                "            width: 100%;\n" +
                "            max-width: 900px;\n" +
                "            margin: 0 auto;\n" +
                "            box-sizing: border-box;\n" +
                "        }\n" +
                "        .header {\n" +
                "            position: relative;\n" +
                "        }\n" +
                "        .header img.banner {\n" +
                "            width: 100%;\n" +
                "            height: auto;\n" +
                "            border-top-left-radius: 8px;\n" +
                "            border-top-right-radius: 8px;\n" +
                "        }\n" +
                "        .header img.logo {\n" +
                "            width: 105px;\n" +
                "            top: 25.5px;\n" +
                "            right: 22px;\n" +
                "            position: absolute;\n" +
                "        }\n" +
                "        .body {\n" +
                "            padding: 40px;\n" +
                "            box-sizing: border-box;\n" +
                "            font-family: 'Product Sans', sans-serif;\n" +
                "        }\n" +
                "        .footer-message {\n" +
                "            background-color: white;\n" +
                "            padding: 24px 40px;\n" +
                "            border-radius: 8px;\n" +
                "            margin: 20px auto;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .font-italic {\n" +
                "            font-style: italic;\n" +
                "        }\n" +
                "        .font-size {\n" +
                "            font-size: 16px;\n" +
                "        }\n" +
                "        .extra-style {\n" +
                "            color: #333;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "        .img {\n" +
                "            width: 100% !important;\n" +
                "            height: 100% !important;\n" +
                "            object-fit: cover;\n" +
                "        }\n" +
                "        .check-in {\n" +
                "            margin: 0;\n" +
                "            font-size: 12px;\n" +
                "            color: #216D42;\n" +
                "            font-weight: 400;\n" +
                "        }\n" +
                "        .room-name {\n" +
                "            margin: 0;\n" +
                "            font-size: 20px;\n" +
                "        }\n" +
                "        p.no-margin {\n" +
                "            margin: 0;\n" +
                "        }\n" +
                "        .container-data {\n" +
                "            vertical-align: baseline;\n" +
                "            font-size: 14px;\n" +
                "        }\n" +
                "        .container-img {\n" +
                "            width: 433px;\n" +
                "            padding-right: 16px;\n" +
                "        }\n" +
                "        .container-img .img {\n" +
                "            width: 433px;\n" +
                "        }\n" +
                "        .table-layout {\n" +
                "            font-family: 'Product Sans', sans-serif;\n" +
                "            width: 100%;\n" +
                "        }\n" +
                "        .hr {\n" +
                "            border: 1px solid #E1E1E1;\n" +
                "            margin: 0;\n" +
                "        }\n" +
                "        .font {\n" +
                "            font-size: 16px;\n" +
                "            font-family: 'Product Sans', sans-serif;\n" +
                "        }\n" +
                "        .strong-text {\n" +
                "            color: #384860;\n" +
                "            font-style: italic;\n" +
                "        }\n" +
                "        .container {\n" +
                "            width: 600px;\n" +
                "            margin: 20px auto;\n" +
                "            padding: 20px;\n" +
                "            background: white;\n" +
                "            border-radius: 10px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        .section {\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        h2 {\n" +
                "            color: #0d5f05;\n" +
                "        }\n" +
                "        p {\n" +
                "            margin: 5px 0;\n" +
                "        }\n" +
                "        .companion {\n" +
                "            margin-bottom: 10px;\n" +
                "            padding: 10px;\n" +
                "            border: 1px solid #ddd;\n" +
                "            border-radius: 5px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <table class=\"container\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "        <tr>\n" +
                "            <td class=\"header\">\n" +
                "                <img class=\"banner\" src=\"https://s3.us-east-2.amazonaws.com/backoffice.documents/email/panoramica_resort.png\" alt=\"Bienvenido\"/>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td class=\"body\">\n" +
                "                <p class=\"font\">Estimado(a), <strong>" + titularName  + titularLastName + "</strong></p>\n" +
                "                <p class=\"font\">El presente es para informar que se completó exitosamente el registro de sus acompañantes\n" +
                "                <div class=\"card\">\n" +
                "                    <table class=\"table-layout\">\n" +
                "                        <tbody>\n" +
                "                            <tr>\n" +
                "                                <td width=\"16\"></td>\n" +
                "                                <td class=\"container-data\">\n" +
                "                                    <table width=\"100%\" style=\"box-sizing: border-box;\">\n" +
                "                                        <div class=\"container\">\n" +
                "                                            <h2>Información del Titular</h2>\n" +
                "                                            <div class=\"section\">\n" +
                "                                                <p><strong>Nombre Completo:</strong> " + titularName + " " + titularLastName + "</p>\n" +
                "                                                <p><strong>Edad:</strong> " + titularAgeValue + "</p>\n" +
                "                                                <p><strong>Tipo de Documento:</strong> " + titularDocumentType + "</p>\n" +
                "                                                <p><strong>Número de Documento:</strong> " + titularDocument + "</p>\n" +
                "                                                <p><strong>Celular:</strong> " + titularCellphone + "</p>\n" +
                "                                                <p><strong>Email:</strong> " + titularEmail + "</p>\n" +
                "                                            </div>\n" +
                "                                        <h2>Acompañantes</h2>\n" +
                "                                        <div class=\"section\">\n" + companionsHtml + "</div>\n" +
                "                                        </div>\n" +
                "                                    </table>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                        </tbody>\n" +
                "                    </table>\n" +
                "                </div>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
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

