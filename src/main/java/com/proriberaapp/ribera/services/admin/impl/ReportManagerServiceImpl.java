package com.proriberaapp.ribera.services.admin.impl;

import com.proriberaapp.ribera.Domain.entities.ExcelEntity;
import com.proriberaapp.ribera.Infraestructure.repository.ExcelRepository;
import com.proriberaapp.ribera.services.admin.ReportManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ReportManagerServiceImpl implements ReportManagerService {
    private final ExcelRepository excelRepository;

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
}
