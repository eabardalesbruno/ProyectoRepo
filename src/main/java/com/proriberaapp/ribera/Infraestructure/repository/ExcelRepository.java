package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.dto.ReportCommissionDto;
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




    @Query("""
       SELECT
                   b.bookingid AS codigoReserva,
                   p.firstName || ' ' || p.lastName AS promotor,
                   c.rucnumber ,
                   c.commissionamount AS comision,
                   c.invoicedocument AS facturaLink,
                   c.status AS estado,
                   r.roomname AS nombreHabitacion,
                   b.bookingstateid AS estadoHabitacion,
                   u.firstName || ' ' || u.lastName AS nombreTitular,
                   b.costfinal AS costoFinal,
                    pb.currencytypeid,
                   bf.bookingfeedingamout AS costoAlimentos,
                   (b.costfinal - COALESCE(bf.bookingfeedingamout, 0)) AS costoSinAlimentos
               FROM booking b
               LEFT JOIN userpromoter  p ON b.userpromotorid = p.userpromoterid
               LEFT JOIN paymentbook pb ON pb.bookingid = b.bookingid
               LEFT JOIN commission  c ON c.paymentbookid = pb.paymentbookid
               LEFT JOIN roomoffer ro ON b.roomofferid = ro.roomofferid
               LEFT JOIN room r ON ro.roomid = r.roomid
               LEFT JOIN userclient u ON b.userclientid = u.userclientid
               LEFT JOIN booking_feeding  bf ON bf.bookingid = b.bookingid
               WHERE c.commissionamount IS NOT NULL
    """)
    Flux<ReportCommissionDto> findExcelReportCommission();
}
