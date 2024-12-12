package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.BookingWithPaymentDTO;
import com.proriberaapp.ribera.Api.controllers.admin.dto.TotalCancellDTO;
import com.proriberaapp.ribera.Api.controllers.admin.dto.TotalSalesDTO;
import com.proriberaapp.ribera.Api.controllers.admin.dto.TotalUsersDTO;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Domain.entities.ExcelEntity;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
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

    @GetMapping("/bookings/stateIdAndDate")
    public Flux<BookingWithPaymentDTO> getBookingsByStateIdAndDate(@RequestParam Integer stateId, LocalDateTime date) {
        return reportManagerService.findBookingsWithPaymentByStateIdAndDate(stateId, date);
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
    public Mono<TotalCancellDTO> TotalCancellSales(@RequestParam Integer month) {
        return reportManagerService.TotalCancellSales(month);
    }

}
