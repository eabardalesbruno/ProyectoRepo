package com.proriberaapp.ribera.services.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.BookingWithPaymentDTO;
import com.proriberaapp.ribera.Api.controllers.admin.dto.TotalCancellDTO;
import com.proriberaapp.ribera.Api.controllers.admin.dto.TotalSalesDTO;
import com.proriberaapp.ribera.Domain.entities.ExcelEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public interface ReportManagerService {
    Flux<ExcelEntity> findAll();

    Flux<ExcelEntity> findAllByIdentifierClient(String identifierClient);

    Flux<ExcelEntity> findAllByKeySupplier(String keySupplier);

    Flux<ExcelEntity> findAllByCreatedAtBetween(Date startDate, Date endDate);

    Mono<Long> countByKeySupplier(String keySupplier);

    Flux<ExcelEntity> findAllWithTotalPaymentGreaterThan(Double minTotalPayment);

    Mono<Long> countByRefuseReasonIdAndPendingPay(int refuseReasonId, int pendingPay);

    Flux<BookingWithPaymentDTO> generateBookingReport(Integer stateId, Integer month);

    Mono<BigDecimal> totalPaymentSum(Integer stateId, Integer month);

    Mono<TotalSalesDTO> totalPaymentMonthSum(Integer stateId, Integer month);

    Flux<BookingWithPaymentDTO> findBookingsWithPaymentByStateIdAndDate(Integer stateId, LocalDateTime date);

    Mono<TotalCancellDTO> TotalCancellSales(Integer month);

}
