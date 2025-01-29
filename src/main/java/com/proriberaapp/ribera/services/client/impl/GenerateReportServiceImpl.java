package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.ResponseFileDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.CompanionsDto;
import com.proriberaapp.ribera.Domain.dto.ReservationReportDto;
import com.proriberaapp.ribera.Domain.entities.CountryEntity;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.services.PDFGeneratorService;
import com.proriberaapp.ribera.services.client.GenerateReportService;
import com.proriberaapp.ribera.utils.pdfTemplate.ReportKitchenPdf;

import static com.proriberaapp.ribera.services.PDFGeneratorService.generateReservationPdfFromHtml;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.jose4j.base64url.internal.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenerateReportServiceImpl implements GenerateReportService {

    private final BookingRepository bookingRepository;

    private final CompanionsRepository companionsRepository;

    private final RoomRepository roomRepository;

    private final UserClientRepository userRepository;

    private final CountryRepository countryRepository;

    @Override
    public Mono<ResponseEntity<ResponseFileDto>> generateReportReservation(int idReservation) {
        return bookingRepository.findByBookingId(idReservation).flatMap(bookingEntity -> {
            ReservationReportDto reservationDto = new ReservationReportDto();
            reservationDto.setPdfFileName("reserva.pdf");
            reservationDto.setDateReservation(bookingEntity.getCreatedAt().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            reservationDto.setDayBookingInit(bookingEntity.getDayBookingInit().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            reservationDto.setDayBookingEnd(bookingEntity.getDayBookingEnd().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            reservationDto.setNumberAdults((bookingEntity.getNumberAdults()+bookingEntity.getNumberAdultsMayor()+bookingEntity.getNumberAdultsExtra())+" Adultos");
            reservationDto.setNumberChildren(bookingEntity.getNumberChildren()+" Niños");
            reservationDto.setNumberBabies(bookingEntity.getNumberBabies()+" Bebés");
            List<ReservationReportDto> listCompanions = new ArrayList<>();


            return userRepository.findById(bookingEntity.getUserClientId()).flatMap(userEntity -> {
                String gender = (userEntity.getGenderId() != null) ? (userEntity.getGenderId() == 1 ? "Masculino" : (userEntity.getGenderId() == 2 ? "Femenino" : "")) : "";
                reservationDto.setAddress(userEntity.getAddress() != null ? userEntity.getAddress() : "");
                reservationDto.setGender(gender);
                reservationDto.setBirthdate(userEntity.getBirthDate() != null ? userEntity.getBirthDate().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) : "");

                return bookingRepository.fingMethodPäymentByBookingId(idReservation).publishOn(Schedulers.boundedElastic()).flatMap(methodPayment -> {
                    reservationDto.setMethodPayment(methodPayment);

                    List<CompanionsDto> items = companionsRepository.getCompanionsByBookingId(bookingEntity.getBookingId()).collectList().block();

                    return roomRepository.getRoomNameByBookingId(idReservation).flatMap(roomEntity -> {
                        reservationDto.setRoomName(roomEntity.getRoomName());
                        reservationDto.setRoomNumber(roomEntity.getRoomNumber());
                        for (CompanionsDto item : items) {

                            if (item.isTitular()) {
                                reservationDto.setDocumentType(item.getDocumenttypedesc());
                                reservationDto.setDocumentNumber(item.getDocumentNumber());
                                reservationDto.setFullname(item.getFirstname() + " " + item.getLastname());
                                reservationDto.setYears(item.getYears() != null ? String.valueOf(item.getYears()) : "");
                                reservationDto.setEmail(item.getEmail() != null ? item.getEmail() : "");
                                reservationDto.setCountrydesc(item.getCountrydesc() != null ? item.getCountrydesc() : "");
                                reservationDto.setCellphone(item.getCellphone() != null ? item.getCellphone() : "");
                                reservationDto.setCourtesy(item.getCourtesy() != null ? item.getCourtesy() : "");

                            } else {
                                ReservationReportDto reportDto = new ReservationReportDto();
                                reportDto.setDocumentType(item.getDocumenttypedesc() != null ? item.getDocumenttypedesc() : "");
                                reportDto.setDocumentNumber(item.getDocumentNumber() != null ? item.getDocumentNumber() : "");
                                reportDto.setFullname(item.getFirstname() + " " + item.getLastname());
                                reportDto.setYears(item.getYears() != null ? "" + item.getYears() : "");
                                reportDto.setGender(item.getGenderdesc() != null ? item.getGenderdesc() : "");
                                reportDto.setCourtesy(item.getCourtesy() != null ? item.getCourtesy() : "");
                                reportDto.setCountrydesc(item.getCountrydesc() != null ? item.getCountrydesc() : "");
                                reportDto.setCellphone(item.getCellphone() != null ? item.getCellphone() : "");
                                reportDto.setEmail(item.getEmail() != null ? item.getEmail() : "");
                                reportDto.setBirthdate(item.getBirthdate() != null ? item.getBirthdate().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) : "");
                                listCompanions.add(reportDto);
                                reservationDto.setLstCompanions(listCompanions);
                            }
                        }

                        byte[] encoded = null;
                        ResponseFileDto result = new ResponseFileDto();
                        try {
                            File pdfFile = generateReservationPdfFromHtml(reservationDto);
                            encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(pdfFile));
                            result.setFile(new String(encoded, StandardCharsets.US_ASCII));
                            result.setFilename(pdfFile.getName());
                        } catch (IOException e) {
                            result.setErrormessage(String.valueOf(new RuntimeException("Error al generar el PDF", e)));
                            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
                        }

                        return Mono.just(ResponseEntity.ok(result));
                    });
                });
            });
        });
    }

    @Override
    public Mono<ResponseEntity<ResponseFileDto>> getReportOfKitchen() {
        return bookingRepository.getReportOfKitchenBdDto()
                .collectList().flatMap(
                        data -> {
                           /*  Integer addBreakfast = data.stream()
                                    .map(d -> d.getTotalperson())
                                    .reduce(0, Integer::sum);
                            Integer numberTotalAdults = data.stream()
                                    .map(d -> d.getNumberadults()
                                            + d.getNumberadultsextra())
                                    .reduce(0, Integer::sum);
                            Integer numberTotalChildren = data.stream()
                                    .map(d -> d.getNumberchildren())
                                    .reduce(0, Integer::sum);
                            Integer numberTotalAdultMayor = data.stream()
                                    .map(d -> d.getNumberadultsmayor())
                                    .reduce(0, Integer::sum);
                           List<ReportOfKitchenBdDto> listDataAlimentation = data
                                    .stream()
                                    .filter(d -> d.isIsalimentation()).toList();

                            Integer totalLunch = listDataAlimentation.stream()
                                    .map(d -> d.getTotalperson())
                                    .reduce(0, Integer::sum);

                            Integer totalDinner = listDataAlimentation.stream()
                                    .map(d -> d.getTotalperson())
                                    .reduce(0, Integer::sum);
                            ReportOfKitchenDto reportOfKitchenDto = new ReportOfKitchenDto();
                            reportOfKitchenDto.setNumberBreakfast(addBreakfast);
                            reportOfKitchenDto.setNumberAdults(numberTotalAdults);
                            reportOfKitchenDto.setNumberChildren(numberTotalChildren);
                            reportOfKitchenDto.setNumberAdultMayor(numberTotalAdultMayor);
                            reportOfKitchenDto.setNumberLunch(totalLunch);
                            reportOfKitchenDto.setNumberDinner(totalDinner); */
                            ReportKitchenPdf reportKitchenPdf = new ReportKitchenPdf(data);
                            String htmlContent = reportKitchenPdf.toTemplate();
                            ResponseFileDto response = PDFGeneratorService.generatePdfFromContent(htmlContent,
                                    "reporte cocina.pdf");
                            return Mono.just(ResponseEntity.ok(response));
                        });

    }
}