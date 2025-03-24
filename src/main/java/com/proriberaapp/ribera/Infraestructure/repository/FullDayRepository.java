package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.dto.PaymentDetailFulldayDTO;
import com.proriberaapp.ribera.Domain.entities.FullDayEntity;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FullDayRepository extends R2dbcRepository<FullDayEntity,Integer> {

    @Query("SELECT * FROM fullday WHERE receptionistid = :id AND bookingstateid = :bookingStateId")
    Flux<FullDayEntity> findByReceptionistIdAndBookingStateId(@Param("id") Integer associatedId, @Param("bookingStateId") Integer bookingStateId);

    @Query("SELECT * FROM fullday WHERE userpromoterid = :id AND bookingstateid = :bookingStateId")
    Flux<FullDayEntity> findByUserPromoterIdAndBookingStateId(@Param("id") Integer associatedId, @Param("bookingStateId") Integer bookingStateId);

    @Query("SELECT * FROM fullday WHERE userclientid = :id AND bookingstateid = :bookingStateId")
    Flux<FullDayEntity> findByUserClientIdAndBookingStateId(@Param("id") Integer associatedId, @Param("bookingStateId") Integer bookingStateId);

    Mono<FullDayEntity> findByFulldayid(Integer fulldayid);

    @Query("""

          SELECT pb.paymentdate,
                 pb.imagevoucher,
               pb.paymentmethodid,
                pm.description AS paymentmethod_description,
                fd.totalprice,
                uc.firstname AS client_firstname,
               uc.lastname AS client_lastname,
                uc.documentnumber,
                uc.documenttypeid ,
                pb.paymentstateid,
                 pb.paymentbookid,
                CASE 
                    WHEN fd.receptionistid IS NOT NULL THEN CONCAT('Recepcionista-', ua.firstname, ' ', ua.lastname)
                    WHEN fd.userpromoterid IS NOT NULL THEN CONCAT('Promotor-', up.firstname, ' ', up.lastname)
                    ELSE CONCAT('Web-', uc.firstname, ' ', uc.lastname)
                END AS channel
            FROM paymentbook pb
            JOIN fullday fd ON pb.fulldayid = fd.fulldayid
            JOIN userclient uc ON fd.userclientid = uc.userclientid
            JOIN paymentmethod pm ON pb.paymentmethodid = pm.paymentmethodid
            LEFT JOIN useradmin  ua ON fd.receptionistid = ua.useradminid
            LEFT JOIN userpromoter  up ON fd.userpromoterid = up.userpromoterid
      """)
    Flux<PaymentDetailFulldayDTO> findByAllPayment();

    @Query("""
    select u.cellnumber, u.countryid, u.firstname, u.lastname, 
           u.documenttypeid, u.documentnumber, u.email, u.userclientid, u.role 
    from userclient u 
    where u.userclientid = :userId
""")
    Mono<UserClientEntity> findByUserclientid(@Param("userId") Integer userId);

}
