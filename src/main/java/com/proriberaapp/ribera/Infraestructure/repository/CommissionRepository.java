package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.dto.CommissionAdminDto;
import com.proriberaapp.ribera.Domain.dto.CommissionDTO;
import com.proriberaapp.ribera.Domain.dto.CommissionPromoterDto;
import com.proriberaapp.ribera.Domain.entities.CommissionEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Repository
public interface CommissionRepository extends R2dbcRepository <CommissionEntity, Integer> {


    @Query("SELECT SUM(commissionamount) FROM commission WHERE promoterid = :promoterId")
    Mono<BigDecimal> findTotalCommissionByPromoterId(@Param("promoterId") Integer promoterId);

    @Query("SELECT * FROM commission c WHERE c.disbursementdate BETWEEN :startOfDay AND :endOfDay AND c.processed = false  and c.status = 'Activo'")
    Flux<CommissionEntity> findByDisbursementDateRange(@Param("startOfDay") Timestamp startOfDay, @Param("endOfDay") Timestamp endOfDay);

    @Query("SELECT serialNumber FROM commission ORDER BY commissionId DESC LIMIT 1")
    Mono<String> findLastSerialNumber();

    Flux<CommissionEntity> findByPromoterId(Integer promoterId);

    @Query("SELECT c.commissionid, c.serialnumber, c.createdat, c.currencytypeid, c.commissionamount, c.rucnumber, c.status, c.dateofapplication, c.invoicedocument, b.bookingid FROM commission c JOIN paymentbook p ON c.paymentbookid = p.paymentbookid JOIN booking b ON p.bookingid = b.bookingid WHERE c.commissionid = :commissionId")
    Mono<CommissionPromoterDto> findByCommissionId(@Param("commissionId") Integer commissionId);

    @Query("""
        SELECT 
            c.commissionid,
            c.paymentbookid ,
            c.disbursementdate,
            c.promoterid,
            p.firstname || ' ' || p.lastname AS promoter_fullname,
            c.rucnumber,
            c.commissionamount,
            c.currencytypeid,
            c.status,
            c.invoicedocument,
            c.processed,
            c.currencytypeid,
            (SELECT COUNT(*) FROM commission WHERE rucnumber IS NOT NULL) AS total_commissions
            FROM commission c
        LEFT JOIN userpromoter p ON c.promoterid = p.userpromoterid
        WHERE c.rucnumber IS NOT NULL
        ORDER BY c.disbursementdate DESC
        LIMIT :size OFFSET :offset
    """)
    Flux<CommissionDTO> findAllWithPromoter(int size, int offset);



    @Query("""
        SELECT
            pb.paymentbookid,
            b.bookingid,
            b.bookingstateid,
            uc.firstname,
            uc.lastname,
            b.costfinal,
            bf.bookingfeedingamout,
            (b.costfinal - COALESCE(bf.bookingfeedingamout, 0)) AS montosinalimentos,
            r.roomname,
            c.dateofapplication,
            pb.currencytypeid
        FROM paymentbook pb
        JOIN booking b ON pb.bookingid = b.bookingid
        JOIN userclient uc ON b.userclientid = uc.userclientid
        LEFT JOIN booking_feeding bf ON b.bookingid = bf.bookingid
        LEFT JOIN roomoffer ro ON b.roomofferid = ro.roomofferid
        LEFT JOIN room r ON ro.roomid = r.roomid
        LEFT JOIN commission c ON pb.paymentbookid = c.paymentbookid
        WHERE pb.paymentbookid = :paymentBookId
    """)
    Mono<CommissionAdminDto> findByPaymentBookId(Integer paymentBookId);

}
