package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.dto.PaymentVoucherDTO;
import com.proriberaapp.ribera.Domain.entities.PaymentVoucherEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

public interface PaymentVoucherRepository extends R2dbcRepository<PaymentVoucherEntity, Integer> {
    @Query("""
            SELECT
                pv.amount AS voucher_amount,
                pv.operationcode AS voucher_operationcode,
                pv.imagevoucher AS voucher_imagevoucher,
                pv.note AS voucher_note,
                ct.currencytypename AS voucher_currencyname,
                pst.paymentsubtypedesc AS voucher_paymentsubtypename
            FROM paymentvoucher pv
            LEFT JOIN currencytype ct ON ct.currencytypeid = pv.currencytypeid
            LEFT JOIN paymentsubtype pst ON pst.paymentsubtypeid = pv.paymentsubtypeid
            WHERE pv.paymentbookid = :paymentBookId
        """)
    Flux<PaymentVoucherDTO> findAllByPaymentBookId(@Param("paymentBookId") Integer paymentBookId);
}