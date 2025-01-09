package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.CommissionEntity;
import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BookingRepository;
import com.proriberaapp.ribera.Infraestructure.repository.CommissionRepository;
import com.proriberaapp.ribera.services.client.CommissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@Service
public class CommissionServiceImpl implements CommissionService {

    @Autowired
    private CommissionRepository commissionRepository;

    @Autowired
    private BookingRepository bookingRepository;


    @Override
    public Mono<CommissionEntity> calculateAndSaveCommission(PaymentBookEntity paymentBook, Integer caseType) {
        return bookingRepository.findById(paymentBook.getBookingId())
                .flatMap(booking -> {
                    CommissionEntity commission = new CommissionEntity();
                    commission.setPaymentBookId(paymentBook.getPaymentBookId());
                    commission.setPromoterId(booking.getUserPromotorId());
                    commission.setCaseType(caseType);

                    BigDecimal totalAmount = paymentBook.getAmount();
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    commission.setCreatedAt(now);

                    LocalDate currentDate = now.toLocalDateTime().toLocalDate();
                    int dayOfMonth = currentDate.getDayOfMonth();
                    LocalDate disbursementDate;
                    if (dayOfMonth <= 15) {
                        disbursementDate = currentDate.withDayOfMonth(20);
                    } else {
                        disbursementDate = currentDate.plusMonths(1).withDayOfMonth(5);
                    }
                    commission.setDisbursementDate(Timestamp.valueOf(disbursementDate.atStartOfDay()));

                    switch (caseType) {
                        case 1:
                            commission.setRiberaAmount(totalAmount);
                            commission.setCommissionAmount(totalAmount.multiply(new BigDecimal("0.15")));
                            commission.setPartnerPayment(BigDecimal.ZERO);
                            commission.setAdminFee(BigDecimal.ZERO);
                            commission.setServiceFee(BigDecimal.ZERO);
                            break;

                        case 2:
                            commission.setRiberaAmount(totalAmount);
                            commission.setCommissionAmount(totalAmount.multiply(new BigDecimal("0.15")));
                            commission.setPartnerPayment(totalAmount.multiply(new BigDecimal("0.50")));
                            commission.setAdminFee(totalAmount.multiply(new BigDecimal("0.05")));
                            commission.setServiceFee(totalAmount.multiply(new BigDecimal("0.30")));
                            break;

                        case 3:
                            commission.setRiberaAmount(totalAmount.multiply(new BigDecimal("0.30")));
                            commission.setCommissionAmount(BigDecimal.ZERO);
                            commission.setPartnerPayment(BigDecimal.ZERO);
                            commission.setAdminFee(BigDecimal.ZERO);
                            commission.setServiceFee(totalAmount.multiply(new BigDecimal("0.30")));
                            break;

                        default:
                            return Mono.error(new IllegalArgumentException("Tipo de caso no válido"));
                    }

                    return commissionRepository.save(commission);
                });
    }
}