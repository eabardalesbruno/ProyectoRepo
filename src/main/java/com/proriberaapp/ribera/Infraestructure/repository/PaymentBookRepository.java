package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.PaymentBookDetailsDTO;
import com.proriberaapp.ribera.Domain.dto.PaymentBookUserDTO;
import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentBookRepository extends R2dbcRepository<PaymentBookEntity, Integer> {
        Flux<PaymentBookEntity> findAll();

        Flux<PaymentBookEntity> findByUserClientId(Integer userClientId);

        Mono<PaymentBookEntity> findById(Integer id);

        @Query("SELECT userclientid FROM paymentbook WHERE paymentbookid = :id")
        Mono<Integer> findUserClientIdByPaymentBookId(Integer id);

        @Query("SELECT * FROM paymentbook WHERE refusereasonid = :refuseReasonId AND pendingpay = :pendingPay ORDER BY paymentbookid DESC LIMIT :size OFFSET :offset")
        Flux<PaymentBookEntity> findAllByRefuseReasonIdAndPendingPay(int refuseReasonId, int pendingPay, int size,
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
                                inv.linkpdf as "invoiceLinkPdf"
                                from     paymentbook pb
                                join userclient uc on uc.userclientid=pb.userclientid
                                join documenttype dt on dt.documenttypeid=uc.documenttypeid
                                join paymentstate ps on ps.paymentstateid=pb.paymentstateid
                                join booking b on b.bookingid=pb.bookingid
                                join paymentmethod pm on pm.paymentmethodid=pb.paymentmethodid
                                join paymenttype pt on pt.paymenttypeid=pb.paymenttypeid
                                join currencytype ct on ct.currencytypeid=pb.currencytypeid
                                join paymentsubtype psub on psub.paymentsubtypeid=pb.paymentsubtypeid
                                left join invoice inv on inv.idpaymentbook=pb.paymentbookid

                                 WHERE pb.pendingpay= 1 and pb.refusereasonid = :refuseReasonId  ORDER BY pb.paymentbookid DESC LIMIT :size OFFSET :offset
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
                               	 ct.currencytypeid

                               FROM paymentbook pb
                               JOIN userclient u ON u.userclientid = pb.userclientid
                               join booking b on b.bookingid=pb.bookingid
                               join roomoffer ro on ro.roomofferid=b.roomofferid
                               join room r on r.roomid=ro.roomid
                               join currencytype ct on ct.currencytypeid=pb.currencytypeid
                        WHERE pb.paymentbookid = :paymentBookId
                        """)
        Mono<PaymentBookUserDTO> loadUserDataAndBookingData(int paymentBookId);

}