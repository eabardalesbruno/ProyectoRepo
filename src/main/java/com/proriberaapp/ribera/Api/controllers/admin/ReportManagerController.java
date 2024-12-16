package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.*;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Domain.entities.ExcelEntity;
import com.proriberaapp.ribera.services.admin.ReportManagerService;
import com.proriberaapp.ribera.services.client.BookingService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.proriberaapp.ribera.services.client.UserClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/reports")
public class ReportManagerController {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private ReportManagerService reportManagerService;
    @Autowired
    private UserClientService userClientService;

    @GetMapping("/bookings/by-state")
    public Flux<BookingEntity> getBookingsByState(@RequestParam Integer stateId) {
        return bookingService.findBookingsByStateId(stateId);
    }

    @GetMapping("/excel")
    public Flux<ExcelEntity> getAllExcelData() {
        return reportManagerService.findAll();
    }

    @GetMapping("/bookings/by-stateId")
    public Flux<BookingWithPaymentDTO> getBookingsByStateId(@RequestParam Integer stateId, Integer month) {
        return reportManagerService.generateBookingReport(stateId, month);
    }

    @GetMapping("/bookings/resumeByStateId")
    public Flux<BookingResumenPaymentDTO> getResumePaymentByStateId(@RequestParam Integer stateId, Integer month) {
        return reportManagerService.findBookingsWithResumeByStateId(stateId, month);
    }

    @GetMapping("/bookings/stateIdAndDate")
    public Flux<BookingWithPaymentDTO> getBookingsByStateIdAndDate(@RequestParam Integer stateId, @RequestParam(required = false) LocalDateTime dateini, @RequestParam(required = false) LocalDateTime datefin) {
        return reportManagerService.findBookingsWithPaymentByStateIdAndDate(stateId, dateini, datefin);
    }

    @GetMapping("/count-users")
    public Mono<TotalUsersDTO> countUsers(@RequestParam Integer month) {
        return userClientService.countUsers(month);
    }

    @GetMapping("/total-payments")
    public Mono<BigDecimal> totalPaymentSum(@RequestParam Integer stateId, Integer month) {
        return reportManagerService.totalPaymentSum(stateId, month);
    }

    @GetMapping("/total-payments-months")
    public Mono<TotalSalesDTO> totalPaymentMonthSum(@RequestParam Integer stateId, Integer month) {
        return reportManagerService.totalPaymentMonthSum(stateId, month);
    }

    @GetMapping("/total-sales-cancell")
    public Mono<TotalCalculationMonthsDTO> TotalCancellSales(@RequestParam Integer month) {
        return reportManagerService.TotalCancellSales(month);
    }

    @GetMapping("/total-before-year")
    public Mono<BigDecimal> getTotalBeforeYear() {
        return reportManagerService.getTotalBeforeYear();
    }

    @GetMapping("/total-active-clients")
    public Mono<Long> totalActiveClients(
            @RequestParam(required = false) Integer stateId, @RequestParam Integer month) {
        return reportManagerService.getTotalActiveClients(stateId, month);
    }

    @GetMapping("/total-active-clients-months")
    public Mono<TotalCalculationMonthsDTO> totalActiveClientsMonths(
            @RequestParam(required = false) Integer stateId, @RequestParam Integer month) {
        return reportManagerService.getTotalActiveClientsMonths(stateId, month);
    }

}
