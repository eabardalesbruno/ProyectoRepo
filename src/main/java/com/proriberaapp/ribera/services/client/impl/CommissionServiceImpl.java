package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.dto.CommissionAdminDto;
import com.proriberaapp.ribera.Domain.dto.CommissionDTO;
import com.proriberaapp.ribera.Domain.dto.CommissionPromoterDto;
import com.proriberaapp.ribera.Domain.entities.BookingFeedingEntity;
import com.proriberaapp.ribera.Domain.entities.CommissionEntity;
import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BookingFeedingRepository;
import com.proriberaapp.ribera.Infraestructure.repository.BookingRepository;
import com.proriberaapp.ribera.Infraestructure.repository.CommissionRepository;
import com.proriberaapp.ribera.services.admin.impl.S3ClientService;
import com.proriberaapp.ribera.services.client.CommissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
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

    @Autowired
    private S3ClientService s3ClientService;

    @Autowired
    private BookingFeedingRepository bookingFeedingRepository;


    @Override
    public Mono<CommissionEntity> calculateAndSaveCommission(PaymentBookEntity paymentBook, Integer caseType) {
        return bookingRepository.findById(paymentBook.getBookingId())
                .flatMap(booking -> bookingFeedingRepository.findByBookingId(booking.getBookingId())
                        .switchIfEmpty(Mono.just(new BookingFeedingEntity()))
                        .flatMap(bookingFeeding -> {
                            BigDecimal totalAmount = booking.getCostFinal();
                            if (totalAmount == null) {
                                return Mono.error(new IllegalStateException("El monto total (costFinal) es nulo"));
                            }
                            BigDecimal feedingAmount = bookingFeeding.getBookingfeedingamout() != null
                                    ? BigDecimal.valueOf(bookingFeeding.getBookingfeedingamout())
                                    : BigDecimal.ZERO;
                            totalAmount = totalAmount.subtract(feedingAmount);
                            if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
                                totalAmount = BigDecimal.ZERO;
                            }

                            CommissionEntity commission = new CommissionEntity();
                            commission.setPaymentBookId(paymentBook.getPaymentBookId());
                            commission.setPromoterId(booking.getUserPromotorId());
                            commission.setCaseType(caseType);
                            commission.setCurrencyTypeId(paymentBook.getCurrencyTypeId());
                            commission.setStatus("Pendiente");
                            commission.setDayBookingInit(booking.getDayBookingInit());

                            Timestamp now = new Timestamp(System.currentTimeMillis());
                            commission.setCreatedAt(now);

                            LocalDate currentDate = now.toLocalDateTime().toLocalDate();
                            int dayOfMonth = currentDate.getDayOfMonth();
                            LocalDate disbursementDate = (dayOfMonth <= 15)
                                    ? currentDate.withDayOfMonth(20)
                                    : currentDate.plusMonths(1).withDayOfMonth(5);

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

                            return generateSerialNumber()
                                    .flatMap(serialNumber -> {
                                        commission.setSerialNumber(serialNumber);
                                        return commissionRepository.save(commission);
                                    })
                                    .doOnSuccess(saved -> System.out.println("Comisión guardada exitosamente: " + saved))
                                    .doOnError(error -> System.err.println("Error al guardar la comisión: " + error.getMessage()));
                        }));
    }

    @Override
    public Mono<BigDecimal> getTotalCommissionByPromoterId(Integer promoterId){
        return commissionRepository.findTotalCommissionByPromoterId(promoterId);
    }

    @Override
    public Flux<CommissionEntity> getCommissionByPromoterId(Integer promoterId) {
        return commissionRepository.findByPromoterId(promoterId);
    }

    @Override
    public Mono<CommissionPromoterDto> getCommissionById(Integer commissionId) {
        return commissionRepository.findByCommissionId(commissionId);
    }

    @Override
    public Mono<CommissionEntity> updateCommission(Integer commissionId, Integer currencyTypeId, BigDecimal userAmount, String rucNumber, Mono<FilePart> file, Integer folderNumber) {
        return commissionRepository.findById(commissionId)
                .flatMap(existingCommission ->
                        s3ClientService.uploadFile(file, folderNumber)
                                .flatMap(url -> {
                                    existingCommission.setCurrencyTypeId(currencyTypeId);
                                    existingCommission.setUserAmount(userAmount);
                                    existingCommission.setRucNumber(rucNumber);
                                    existingCommission.setStatus("Activo");
                                    existingCommission.setInvoiceDocument(url);
                                    existingCommission.setDateofapplication(new Timestamp(System.currentTimeMillis()));
                                    return commissionRepository.save(existingCommission);
                                })
                );
    }

    @Override
    public Mono<CommissionEntity> updateStatusByCommissionId(Integer commissionId, String status) {
        return commissionRepository.findById(commissionId)
                .flatMap(commission -> {
                    commission.setStatus(status);
                    return commissionRepository.save(commission);
                });
    }

    @Override
    public Mono<CommissionAdminDto> getPaymentBookDetails(Integer paymentBookId) {
        return commissionRepository.findByPaymentBookId(paymentBookId);
    }


    @Override
    public Flux<CommissionEntity> getAllCommission() {
        return commissionRepository.findAll();
    }

    @Override
    public Flux<CommissionDTO> getCommissionsPaged(int page, int size) {
        int offset = page * size;
        return commissionRepository.findAllWithPromoter(size, offset);
    }

    public Mono<String> generateSerialNumber() {
        return commissionRepository.findLastSerialNumber()
                .map(this::incrementSerialNumber)
                .switchIfEmpty(Mono.just("RE001"));
    }

    private String incrementSerialNumber(String lastSerial) {
        String prefix = "RE";
        String numericPart = lastSerial.substring(2);
        int number = Integer.parseInt(numericPart) + 1;
        String newNumericPart = String.format("%0" + numericPart.length() + "d", number);
        return prefix + newNumericPart;
    }
}