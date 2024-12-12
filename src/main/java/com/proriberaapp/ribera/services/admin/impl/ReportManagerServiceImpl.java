package com.proriberaapp.ribera.services.admin.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.BookingWithPaymentDTO;
import com.proriberaapp.ribera.Api.controllers.admin.dto.TotalCancellDTO;
import com.proriberaapp.ribera.Api.controllers.admin.dto.TotalSalesDTO;
import com.proriberaapp.ribera.Domain.entities.ExcelEntity;
import com.proriberaapp.ribera.Infraestructure.repository.ExcelRepository;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentBookRepository;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.services.admin.ReportManagerService;
import com.proriberaapp.ribera.services.client.BookingService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ReportManagerServiceImpl implements ReportManagerService {
    private final ExcelRepository excelRepository;
    private final PaymentBookRepository paymentBookRepository;
    private final UserClientRepository userClientRepository;
    private final BookingService bookingService;

    @Override
    public Flux<ExcelEntity> findAll() {
        return excelRepository.findAll();
    }

    @Override
    public Flux<ExcelEntity> findAllByIdentifierClient(String identifierClient) {
        return excelRepository.findAllByIdentifierClient(identifierClient);
    }

    @Override
    public Flux<ExcelEntity> findAllByKeySupplier(String keySupplier) {
        return excelRepository.findAllByKeySupplier(keySupplier);
    }

    @Override
    public Flux<ExcelEntity> findAllByCreatedAtBetween(Date startDate, Date endDate) {
        return excelRepository.findAllByCreatedAtBetween(startDate, endDate);
    }

    @Override
    public Mono<Long> countByKeySupplier(String keySupplier) {
        return excelRepository.countByKeySupplier(keySupplier);
    }

    @Override
    public Flux<ExcelEntity> findAllWithTotalPaymentGreaterThan(Double minTotalPayment) {
        return excelRepository.findAllWithTotalPaymentGreaterThan(minTotalPayment);
    }

    @Override
    public Mono<Long> countByRefuseReasonIdAndPendingPay(int refuseReasonId, int pendingPay) {
        return paymentBookRepository.countByRefuseReasonIdAndPendingPay(refuseReasonId, pendingPay);
    }

    @Override
    public Flux<BookingWithPaymentDTO> generateBookingReport(Integer stateId, Integer month) {
        return bookingService.findBookingsWithPaymentByStateId(stateId, month);
    }

    @Override
    public Mono<BigDecimal> totalPaymentSum(Integer stateId, Integer month) {
        return bookingService.totalPaymentSum(stateId, month);
    }

    @Override
    public Mono<TotalSalesDTO> totalPaymentMonthSum(Integer stateId, Integer month) {
        return bookingService.totalPaymentMonthSum(stateId, month);
    }

    @Override
    public Flux<BookingWithPaymentDTO> findBookingsWithPaymentByStateIdAndDate(Integer stateId, LocalDateTime date) {
        return bookingService.findBookingsWithPaymentByStateIdAndDate(stateId, date);
    }

    @Override
    public Mono<TotalCancellDTO> TotalCancellSales(Integer month) {
        return bookingService.totalPaymentMonthSum(month);
    }

}
