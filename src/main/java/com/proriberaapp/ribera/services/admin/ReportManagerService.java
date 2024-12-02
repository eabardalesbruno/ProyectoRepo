package com.proriberaapp.ribera.services.admin;

import com.proriberaapp.ribera.Domain.entities.ExcelEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

public interface ReportManagerService {
    Flux<ExcelEntity> findAll();
    Flux<ExcelEntity> findAllByIdentifierClient(String identifierClient);
    Flux<ExcelEntity> findAllByKeySupplier(String keySupplier);
    Flux<ExcelEntity> findAllByCreatedAtBetween(Date startDate, Date endDate);
    Mono<Long> countByKeySupplier(String keySupplier);
    Flux<ExcelEntity> findAllWithTotalPaymentGreaterThan(Double minTotalPayment);
}
