package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.ExcelEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

public interface ExcelRepository extends R2dbcRepository<ExcelEntity, UUID> {

    Flux<ExcelEntity> findAllByIdentifierClient(String identifierClient);

    Flux<ExcelEntity> findAllByKeySupplier(String keySupplier);

    @Query("SELECT * FROM excel_view WHERE createdat BETWEEN :startDate AND :endDate")
    Flux<ExcelEntity> findAllByCreatedAtBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    Flux<ExcelEntity> findAllByIdCurrency(Integer idCurrency);

    @Query("SELECT * FROM excel_view WHERE totalpayment > :minTotalPayment")
    Flux<ExcelEntity> findAllWithTotalPaymentGreaterThan(@Param("minTotalPayment") Double minTotalPayment);

    @Query("SELECT COUNT(*) FROM excel_view WHERE keysupplier = :keySupplier")
    Mono<Long> countByKeySupplier(@Param("keySupplier") String keySupplier);
}
