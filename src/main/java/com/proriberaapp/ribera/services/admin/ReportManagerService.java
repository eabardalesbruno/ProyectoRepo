package com.proriberaapp.ribera.services.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.BookingResumenPaymentDTO;
import com.proriberaapp.ribera.Api.controllers.admin.dto.BookingWithPaymentDTO;
import com.proriberaapp.ribera.Api.controllers.admin.dto.TotalCalculationMonthsDTO;
import com.proriberaapp.ribera.Api.controllers.admin.dto.TotalSalesDTO;
import com.proriberaapp.ribera.Domain.entities.ExcelEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface ReportManagerService {
    Flux<ExcelEntity> findAll();

    Flux<ExcelEntity> findAllByIdentifierClient(String identifierClient);

    Flux<ExcelEntity> findAllByKeySupplier(String keySupplier);

    Flux<ExcelEntity> findAllByCreatedAtBetween(Date startDate, Date endDate);

    Mono<Long> countByKeySupplier(String keySupplier);

    Flux<ExcelEntity> findAllWithTotalPaymentGreaterThan(Double minTotalPayment);

    Mono<Long> countByRefuseReasonIdAndPendingPay(int refuseReasonId, int pendingPay);

    Flux<BookingWithPaymentDTO> generateBookingReport(Integer stateId, Integer month, Integer year);

    Mono<BigDecimal> totalPaymentSum(Integer stateId, Integer month, Integer year);

    Mono<TotalSalesDTO> totalPaymentMonthSum(Integer stateId, Integer month, Integer year);

    Flux<BookingWithPaymentDTO> findBookingsWithPaymentByStateIdAndDate(Integer stateId, LocalDateTime dateini, LocalDateTime datefin);

    Mono<TotalCalculationMonthsDTO> TotalCancellSales(Integer month, Integer year);

    Flux<BookingResumenPaymentDTO> findBookingsWithResumeByStateId(Integer stateId, Integer month, Integer year);

    Mono<BigDecimal> getTotalBeforeYear();

    Mono<Long> getTotalActiveClients(Integer stateId, Integer month);

    Mono<TotalCalculationMonthsDTO> getTotalActiveClientsMonths(Integer stateId, Integer month);

    Mono<List<Long>> getAllYearsInvoice();

}
