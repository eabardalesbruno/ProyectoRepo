package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.PaymentBookDetailsDTO;
import com.proriberaapp.ribera.Domain.dto.*;
import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentBookRepository extends R2dbcRepository<PaymentBookEntity, Integer> {
        Flux<PaymentBookEntity> findAll();

        Flux<PaymentBookEntity> findByUserClientId(Integer userClientId);

        Mono<PaymentBookEntity> findById(Integer id);

        Mono<PaymentBookEntity> findByPaymentBookId(Integer paymentBookId);

        @Query("SELECT userclientid FROM paymentbook WHERE paymentbookid = :id")
        Mono<Integer> findUserClientIdByPaymentBookId(Integer id);

        @Query("""
                                SELECT  pb.*,  bo.createdat, 
                          (CASE
                                        WHEN up.userpromoterid is not null  THEN concat('PROMOTOR ',' - ',up.firstname,' ',up.lastname)
                                        WHEN ua.useradminid is not null THEN concat('RECEPCION',' - ',ua.firstname,' ',ua.lastname)
                                        when uc.userclientid is not null and uc.isuserinclub THEN 'WEB - Socio'
                                        when uc.userclientid is not null and not uc.isuserinclub THEN 'WEB'
                                      ELSE
                                                    'Sin clasificar' END) as channel,
                                         pb.invoicedocumentnumber ,
                                         pb.invoicetype ,
                                         calculate_nights(bo.daybookinginit,bo.daybookingend ) as nights,
                                          to_char(bo.daybookinginit, 'DD/MM/YYYY') as daybookinginit,
                                        to_char(bo.daybookingend, 'DD/MM/YYYY') as daybookingend

                         FROM paymentbook pb
                        join booking bo on bo.bookingid=pb.bookingid
                        LEFT JOIN useradmin ua on ua.useradminid=bo.receptionistid
                        LEFT JOIN userpromoter up on up.userpromoterid=bo.userpromotorid
                        left join userclient uc on uc.userclientid=pb.userclientid
                                WHERE refusereasonid = :refuseReasonId AND pendingpay = :pendingPay ORDER BY pb.paymentdate DESC LIMIT :size OFFSET :offset
                                """)
        Flux<PaymentBookWithChannelDto> findAllByRefuseReasonIdAndPendingPay(int refuseReasonId, int pendingPay,
                        int size,
                        int offset);

        @Query("""
                                  select
                                          pb.paymentbookid as "paymentBookId",
                                          pb.bookingid as "bookingId",
                                          uc.userclientid as "userClientId",
                                          pb.refusereasonid as "refuseReasonId",
                                          pb.paymentbookid as "paymentMethodId",
                                          pb.paymentstateid as "paymentStateId",
                                          pb.paymenttypeid as "paymentTypeId",
                                          pb.paymentsubtypeid as "paymentSubTypeId",
                                          pb.currencytypeid as "currencyTypeId",
                                          pb.amount,
                                          pb.description,
                                          pb.paymentdate as "paymentDate",
                                          pb.operationcode as "operationCode",
                                          pb.note,
                                          pb.totalcost as "totalCost",
                                          pb.imagevoucher as "imageVoucher",
                                          pb.totalpoints as "totalPoints",
                                          pb.paymentcomplete as "paymentComplete",
                                          pb.pendingpay as "pendingPay",
                                          uc.firstname as "userClientName",
                                          uc.lastname as "userClientLastName",
                                          uc.email as "userClientEmail",
                                          uc.documentnumber as "userDocumentNumber",
                                          dt.documenttypeid as "userDocumentType",
                                          uc.cellnumber as "userCellphoneNumber",
                                          b.detail as "bookingName",
                                          pm.description as "paymentMethod",
                                          ps.paymentstatename as "paymentState",
                                          pt.paymenttypedesc as "paymentType",
                                          psub.paymentsubtypedesc as "paymentSubtype",
                                          ct.currencytypedescription as "currencyType",
                                          inv.serie as "invoiceSerie",
                                          inv.linkpdf as "invoiceLinkPdf",
                                          pb.totaldiscount as "totalDiscount",
                                          pb.totalcostwithoutdiscount as "totalCostWithOutDiscount",
                                          pb.percentagediscount as "percentageDiscount",
                                          bs.bookingstatename,
                                          bs.bookingstateid,
                                          calculate_nights(b.daybookinginit,b.daybookingend ) as nights,
                                                to_char(b.daybookinginit, 'DD/MM/YYYY') as daybookinginit,
                                                to_char(b.daybookingend, 'DD/MM/YYYY') as daybookingend,
                                                               (CASE
                              WHEN up.userpromoterid is not null  THEN concat('PROMOTOR ',' - ',up.firstname,' ',up.lastname)
                              WHEN ua.useradminid is not null THEN concat('RECEPCION',' - ',ua.firstname,' ',ua.lastname)
                              when uc.userclientid is not null and uc.isuserinclub THEN 'WEB - Socio'
                              when uc.userclientid is not null and not uc.isuserinclub THEN 'WEB'
                            ELSE
                                          'Sin clasificar' END) as channel,
                                      pb.invoicedocumentnumber ,
                                      pb.invoicetype,
                                      b.createdat
                                          from     paymentbook pb
                                          join userclient uc on uc.userclientid=pb.userclientid
                                          join documenttype dt on dt.documenttypeid=uc.documenttypeid
                                          join paymentstate ps on ps.paymentstateid=pb.paymentstateid
                                          join booking b on b.bookingid=pb.bookingid
                                          join bookingstate bs on bs.bookingstateid=b.bookingstateid
                                          join paymentmethod pm on pm.paymentmethodid=pb.paymentmethodid
                                          join paymenttype pt on pt.paymenttypeid=pb.paymenttypeid
                                          join currencytype ct on ct.currencytypeid=pb.currencytypeid
                                          join paymentsubtype psub on psub.paymentsubtypeid=pb.paymentsubtypeid
                                          LEFT JOIN useradmin ua on ua.useradminid=b.receptionistid
                                          LEFT JOIN userpromoter up on up.userpromoterid=b.userpromotorid
                                          left join invoice inv on inv.idpaymentbook=pb.paymentbookid
                                           WHERE pb.pendingpay= 1 and pb.refusereasonid = :refuseReasonId  ORDER BY pb.paymentdate DESC
                                           """)
        Flux<PaymentBookDetailsDTO> findAllPaged(int refuseReasonId);

        @Query("""
                                  select
                                          pb.paymentbookid as "paymentBookId",
                                          pb.bookingid as "bookingId",
                                          uc.userclientid as "userClientId",
                                          pb.refusereasonid as "refuseReasonId",
                                          pb.paymentbookid as "paymentMethodId",
                                          pb.paymentstateid as "paymentStateId",
                                          pb.paymenttypeid as "paymentTypeId",
                                          pb.paymentsubtypeid as "paymentSubTypeId",
                                          pb.currencytypeid as "currencyTypeId",
                                          pb.amount,
                                          pb.description,
                                          pb.paymentdate as "paymentDate",
                                          pb.operationcode as "operationCode",
                                          pb.note,
                                          pb.totalcost as "totalCost",
                                          pb.imagevoucher as "imageVoucher",
                                          pb.totalpoints as "totalPoints",
                                          pb.paymentcomplete as "paymentComplete",
                                          pb.pendingpay as "pendingPay",
                                          uc.firstname as "userClientName",
                                          uc.lastname as "userClientLastName",
                                          uc.email as "userClientEmail",
                                          uc.documentnumber as "userDocumentNumber",
                                          dt.documenttypeid as "userDocumentType",
                                          uc.cellnumber as "userCellphoneNumber",
                                          b.detail as "bookingName",
                                          pm.description as "paymentMethod",
                                          ps.paymentstatename as "paymentState",
                                          pt.paymenttypedesc as "paymentType",
                                          psub.paymentsubtypedesc as "paymentSubtype",
                                          ct.currencytypedescription as "currencyType",
                                          inv.serie as "invoiceSerie",
                                          inv.linkpdf as "invoiceLinkPdf",
                                          pb.totaldiscount as "totalDiscount",
                                          pb.totalcostwithoutdiscount as "totalCostWithOutDiscount",
                                          pb.percentagediscount as "percentageDiscount",
                                          bs.bookingstatename,
                                          bs.bookingstateid,
                                          calculate_nights(b.daybookinginit,b.daybookingend ) as nights,
                                                to_char(b.daybookinginit, 'DD/MM/YYYY') as daybookinginit,
                                                to_char(b.daybookingend, 'DD/MM/YYYY') as daybookingend,
                                                               (CASE
                              WHEN up.userpromoterid is not null  THEN concat('PROMOTOR ',' - ',up.firstname,' ',up.lastname)
                              WHEN ua.useradminid is not null THEN concat('RECEPCION',' - ',ua.firstname,' ',ua.lastname)
                              when uc.userclientid is not null and uc.isuserinclub THEN 'WEB - Socio'
                              when uc.userclientid is not null and not uc.isuserinclub THEN 'WEB'
                            ELSE
                                          'Sin clasificar' END) as channel,
                                      pb.invoicedocumentnumber ,
                                      pb.invoicetype
                                          from     paymentbook pb
                                          join userclient uc on uc.userclientid=pb.userclientid
                                          join documenttype dt on dt.documenttypeid=uc.documenttypeid
                                          join paymentstate ps on ps.paymentstateid=pb.paymentstateid
                                          join booking b on b.bookingid=pb.bookingid
                                          join bookingstate bs on bs.bookingstateid=b.bookingstateid
                                          join paymentmethod pm on pm.paymentmethodid=pb.paymentmethodid
                                          join paymenttype pt on pt.paymenttypeid=pb.paymenttypeid
                                          join currencytype ct on ct.currencytypeid=pb.currencytypeid
                                         left join paymentsubtype psub on psub.paymentsubtypeid=pb.paymentsubtypeid
                                          LEFT JOIN useradmin ua on ua.useradminid=b.receptionistid
                                          LEFT JOIN userpromoter up on up.userpromoterid=b.userpromotorid
                                          left join invoice inv on inv.idpaymentbook=pb.paymentbookid
                                           WHERE pb.pendingpay= 1 and pb.refusereasonid = :refuseReasonId  ORDER BY pb.paymentdate DESC LIMIT :size OFFSET :offset
                                           """)
        Flux<PaymentBookDetailsDTO> findAllPaged(int refuseReasonId, int size,
                        int offset);

        @Query("SELECT COUNT(*) FROM paymentbook WHERE refusereasonid = :refuseReasonId AND pendingpay = :pendingPay")
        Mono<Long> countByRefuseReasonIdAndPendingPay(int refuseReasonId, int pendingPay);

        @Query("UPDATE paymentbook SET pendingpay = 1, paymentstateid=2 WHERE paymentbookid = :paymentBookId")
        Mono<Void> confirmPayment(int paymentBookId);

        @Query("SELECT * FROM paymentbook WHERE bookingId = :bookingId AND pendingpay = 1")
        Flux<PaymentBookEntity> findAllByBookingIdAndCancelReasonIdIsNull(Integer bookingId);

        @Query("""
                               SELECT pb.paymentbookid,
                                 pb.operationcode,
                                                              pb.note,
                                                              pb.totalcost ,
                                                              pb.imagevoucher,
                                                              pb.totalpoints ,
                                                              pb.paymentcomplete,
                                                              pb.pendingpay ,
                                                              u.userclientid as userClientId,
                                                              u.firstname as userName,
                                                              u.email as userEmail,
                                                               u.address as userAddress,
                                                               u.cellnumber as userPhone,
                                                               u.documentnumber as useridentifierclient,
                                                               r.roomname as roomName,
                                                               r.roomnumber as roomNumber,
                                                              	 ct.currencytypename,
                                                              	 ct.currencytypeid,
                                                                pb.percentagediscount,
                                                               pb.totalcostwithoutdiscount,
                                                               pb.invoicedocumentnumber,
                                                               pb.invoicetype,
                                                               b.bookingid,
                                                               pb.fulldayid,
                                                               f.type
                                                              FROM paymentbook pb
                                                              JOIN userclient u ON u.userclientid = pb.userclientid
                                                              left join booking b on b.bookingid=pb.bookingid
                                                              left join roomoffer ro on ro.roomofferid=b.roomofferid
                                                              left join room r on r.roomid=ro.roomid
                                                              left join currencytype ct on ct.currencytypeid=pb.currencytypeid
                                                              LEFT JOIN fullday f ON f.fulldayid = pb.fulldayid
                        WHERE pb.paymentbookid = :paymentBookId
                        """)
        Mono<PaymentBookUserDTO> loadUserDataAndBookingData(int paymentBookId);

        @Query("SELECT COUNT(*) FROM paymentbook WHERE bookingid = :bookingid")
        Mono<Long> countPaymentBookByBookingId(int bookingid);

        @Query("update paymentbook set cancelreasonid = :cancelreasonid where bookingid = :bookingId")
        Mono<Void> setCancelForBookingId( Integer cancelreasonid,Integer bookingId);

        @Query("""
            select pb.paymentbookid, pb.paymentdate, pb.operationcode, pb.imagevoucher, pm.description methodpayment, pt.paymenttypedesc, (i.totaligv+i.subtotal) total
            from paymentbook pb
            join paymentmethod pm on pb.paymentmethodid = pm.paymentmethodid
            join paymenttype pt on pt.paymenttypeid = pb.paymenttypeid
            join invoice i on i.idpaymentbook = pb.paymentbookid
            where pb.bookingid = :bookingId
        """)
        Mono<PaymentDetailDTO> getPaymentDetail(Integer bookingId);

        @Query("""
                SELECT fd.fulldaydetailid , fd.typeperson, fd.quantity ,fd.finalprice , f.type
                FROM fulldaydetail fd
                JOIN fullday f ON fd.fulldayid = f.fulldayid
                WHERE f.fulldayid = :fulldayId
                """)
        Flux<FullDayDetailDTO> findByFullDayId(@Param("fulldayId") Integer fulldayId);

        @Query("""
           
                WITH CompanionsCount AS (
               SELECT
                   bookingid,
                   COALESCE(SUM(CASE WHEN category IN ('Adulto', 'Titular') THEN 1 ELSE 0 END), 0) AS num_adultos,
                   COALESCE(SUM(CASE WHEN category = 'Adulto Mayor' THEN 1 ELSE 0 END), 0) AS num_adultos_mayores,
                   COALESCE(SUM(CASE WHEN category = 'Adulto Extra' THEN 1 ELSE 0 END), 0) AS num_adultos_extras,
                   COALESCE(SUM(CASE WHEN category = 'Niño' THEN 1 ELSE 0 END), 0) AS num_ninos,
                   COALESCE(SUM(CASE WHEN category = 'Infante' THEN 1 ELSE 0 END), 0) AS num_infantes
               FROM companions
               WHERE bookingid = :bookingId
               GROUP BY bookingid
           )
           
           SELECT
               bf.bookingid,
               ffffg.idfeedingtype,
               CASE
                   WHEN ffffg.idfamilygroup = 1 THEN 'Niño'
                   WHEN ffffg.idfamilygroup = 2 THEN 'Adulto'
                   WHEN ffffg.idfamilygroup = 3 THEN 'Adulto Mayor'
                   WHEN ffffg.idfamilygroup = 4 THEN 'Adulto Extra'
               END AS categoria,
               CASE\s
                   WHEN ffffg.idfeedingtype = 1 THEN 'Desayuno'
                   WHEN ffffg.idfeedingtype = 2 THEN 'Almuerzo'
                   WHEN ffffg.idfeedingtype = 3 THEN 'Cena'
               END AS tipo_comida,
               ffffg.value AS precio_unitario,
               COALESCE(
                   CASE
                       WHEN ffffg.idfamilygroup = 1 THEN cc.num_ninos
                       WHEN ffffg.idfamilygroup = 2 THEN cc.num_adultos
                       WHEN ffffg.idfamilygroup = 3 THEN cc.num_adultos_mayores
                       WHEN ffffg.idfamilygroup = 4 THEN cc.num_adultos_extras
                   END, 0
               ) AS cantidad_personas,
               COALESCE(
                   CASE
                       WHEN ffffg.idfamilygroup = 1 THEN cc.num_ninos
                       WHEN ffffg.idfamilygroup = 2 THEN cc.num_adultos
                       WHEN ffffg.idfamilygroup = 3 THEN cc.num_adultos_mayores
                       WHEN ffffg.idfamilygroup = 4 THEN cc.num_adultos_extras
                   END, 0
               ) * ffffg.value AS costo_total
           FROM booking_feeding bf
           INNER JOIN feeding f ON bf.feedingid = f.id
           INNER JOIN feeding_type_feeding_family_group ffffg ON f.id = ffffg.idfeeding
           LEFT JOIN CompanionsCount cc ON bf.bookingid = cc.bookingid
           WHERE bf.bookingid = :bookingId
             AND (COALESCE(
                   CASE
                       WHEN ffffg.idfamilygroup = 1 THEN cc.num_ninos
                       WHEN ffffg.idfamilygroup = 2 THEN cc.num_adultos
                       WHEN ffffg.idfamilygroup = 3 THEN cc.num_adultos_mayores
                       WHEN ffffg.idfamilygroup = 4 THEN cc.num_adultos_extras
                   END, 0) * ffffg.value) > 0
           ORDER BY categoria, tipo_comida;
           """)
        Flux<FoodDetailVisualCountDto> findPaymentDetailsByBookingId(Integer bookingId);

        @Query("""
        SELECT 
            bookingid,
            CONCAT('SOLO ALOJAMIENTO - ',numberadults, ' Adultos + ', numberchildren, ' Niños') AS cantidad_personas,
            CONCAT(
                'INGRESO ', TO_CHAR(daybookinginit , 'DD'), ' ', TO_CHAR(daybookinginit, 'TMMonth'),
                ' - SALIDA ', TO_CHAR(daybookingend, 'DD'), ' ', TO_CHAR(daybookingend, 'TMMonth')
            ) AS rango_fechas,
            'INCLUYE DESAYUNO' AS incluye_desayuno
        FROM booking
        WHERE bookingid = :bookingId
    """)
        Mono<VisualCountDetailsDTO> findBookingDetailsByBookingId(Integer bookingId);
}