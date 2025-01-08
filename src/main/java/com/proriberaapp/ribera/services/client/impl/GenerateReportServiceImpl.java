package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.ResponseFileDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.CompanionsDto;
import com.proriberaapp.ribera.Domain.dto.ReservationReportDto;
import com.proriberaapp.ribera.Infraestructure.repository.BookingRepository;
import com.proriberaapp.ribera.Infraestructure.repository.CompanionsRepository;
import com.proriberaapp.ribera.Infraestructure.repository.DocumentTypeRepository;
import com.proriberaapp.ribera.Infraestructure.repository.RoomRepository;
import com.proriberaapp.ribera.services.PDFGeneratorService;
import com.proriberaapp.ribera.services.client.GenerateReportService;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class GenerateReportServiceImpl implements GenerateReportService {

    private final BookingRepository bookingRepository;

    private final CompanionsRepository companionsRepository;

    private final RoomRepository roomRepository;

    private final PDFGeneratorService pdfGeneratorService;

    @Override
    public Mono<ResponseEntity<ResponseFileDto>> generateReportReservation(int idReservation) {
        return bookingRepository.findByBookingId(idReservation).publishOn(Schedulers.boundedElastic()).flatMap(bookingEntity -> {
            ReservationReportDto reservationDto = new ReservationReportDto();
            reservationDto.setPdfFileName("reserva.pdf");
            reservationDto.setDateReservation(bookingEntity.getCreatedAt().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            List<ReservationReportDto> listCompanions = new ArrayList<>();

            List<CompanionsDto> items = companionsRepository.getCompanionsByBookingId(bookingEntity.getBookingId()).collectList().block();

            return roomRepository.getRoomNameByBookingId(idReservation).flatMap(roomEntity -> {
                reservationDto.setRoomName(roomEntity.getRoomName());
                for (CompanionsDto item : items) {
                    if (item.isTitular()) {
                        reservationDto.setDocumentType(item.getDocumenttypedesc());
                        reservationDto.setDocumentNumber(item.getDocumentNumber());
                        reservationDto.setFullname(item.getFirstname() + " " + item.getLastname());
                        reservationDto.setYears(item.getYears() != null ? String.valueOf(item.getYears()) : "");
                    } else {
                        ReservationReportDto reportDto = new ReservationReportDto();
                        reportDto.setDocumentType(item.getDocumenttypedesc() != null ? item.getDocumenttypedesc() : "" );
                        reportDto.setDocumentNumber(item.getDocumentNumber() != null ? item.getDocumentNumber() : "" );
                        reportDto.setFullname(item.getFirstname() + " " + item.getLastname());
                        reportDto.setYears(item.getYears() != null ? ""+item.getYears() : "" );
                        reportDto.setGender(item.getGenderdesc() != null ? item.getGenderdesc() : "" );
                        listCompanions.add(reportDto);
                        reservationDto.setLstCompanions(listCompanions);
                    }
                }
                byte[] encoded = null;
                ResponseFileDto result = new ResponseFileDto();
                try {
                    File pdfFile = pdfGeneratorService.generateReservationPdfFromHtml(reservationDto);
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
    }
}