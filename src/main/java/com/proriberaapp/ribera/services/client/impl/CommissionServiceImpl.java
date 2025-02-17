package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.dto.CommissionAdminDto;
import com.proriberaapp.ribera.Domain.dto.CommissionDTO;
import com.proriberaapp.ribera.Domain.dto.CommissionGroupResponse;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
                            commission.setReceptionistId(booking.getReceptionistId());

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
    public Mono<BigDecimal> getTotalCommissionByPromoterId(Integer promoterId) {
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
    public Mono<CommissionGroupResponse> updateAllGroupedCommissions(List<Integer> commissionIds, Integer promoterId, Integer currencyTypeId, BigDecimal userAmount, String rucNumber, Mono<FilePart> file, Integer folderNumber) {

        return commissionRepository.findAllById(commissionIds)
                .collectList()
                .flatMap(pendingCommissions -> {
                    if (pendingCommissions.isEmpty()) {
                        return Mono.empty();
                    }
                    Timestamp firstCreatedAt = pendingCommissions.get(0).getCreatedAt();
                    int firstMonth = firstCreatedAt.toLocalDateTime().getMonthValue();
                    boolean sameMonth = pendingCommissions.stream()
                            .allMatch(commission -> commission.getCreatedAt().toLocalDateTime().getMonthValue() == firstMonth);

                    if (!sameMonth) {
                        return Mono.error(new IllegalArgumentException("Las comisiones deben ser del mismo mes."));
                    }

                    List<CommissionEntity> firstHalf = pendingCommissions.stream()
                            .filter(commission -> {
                                int dayOfMonth = commission.getCreatedAt().toLocalDateTime().getDayOfMonth();
                                return dayOfMonth <= 15;
                            })
                            .collect(Collectors.toList());

                    List<CommissionEntity> secondHalf = pendingCommissions.stream()
                            .filter(commission -> {
                                int dayOfMonth = commission.getCreatedAt().toLocalDateTime().getDayOfMonth();
                                return dayOfMonth > 15;
                            })
                            .collect(Collectors.toList());

                    BigDecimal totalFirstHalf = firstHalf.stream()
                            .map(CommissionEntity::getCommissionAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal totalSecondHalf = secondHalf.stream()
                            .map(CommissionEntity::getCommissionAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    firstHalf.sort(Comparator.comparing(CommissionEntity::getCreatedAt));
                    secondHalf.sort(Comparator.comparing(CommissionEntity::getCreatedAt));

                    Timestamp firstDisbursementDate = firstHalf.isEmpty() ? null : firstHalf.get(0).getCreatedAt();
                    Timestamp lastDisbursementDate = secondHalf.isEmpty() ? null : secondHalf.get(secondHalf.size() - 1).getCreatedAt();
                    return s3ClientService.uploadFile(file, folderNumber)
                            .flatMap(url -> {
                                List<Mono<CommissionEntity>> updatedCommissions = pendingCommissions.stream()
                                        .map(commission -> {
                                            commission.setCurrencyTypeId(currencyTypeId);
                                            commission.setRucNumber(rucNumber);
                                            commission.setStatus("Activo");
                                            commission.setInvoiceDocument(url);
                                            commission.setDateofapplication(new Timestamp(System.currentTimeMillis()));
                                            return commissionRepository.save(commission);
                                        })
                                        .collect(Collectors.toList());
                                CommissionGroupResponse response = new CommissionGroupResponse();
                                response.setTotalCommissionAmount(totalFirstHalf.add(totalSecondHalf));
                                response.setNumberOfCommissions(firstHalf.size() + secondHalf.size());
                                response.setFirstDisbursementDate(firstDisbursementDate);
                                response.setLastDisbursementDate(lastDisbursementDate);
                                return Flux.concat(updatedCommissions).collectList().then(Mono.just(response));
                            });
                });
    }

    @Override
    public Mono<CommissionGroupResponse> getGroupedCommissions(Integer promoterId, Integer partnerId, Integer receptionistId, Integer month) {
        return commissionRepository.findPendingCommissionsByIdsAndMonth(promoterId, partnerId, receptionistId, month)
                .collectList()
                .flatMap(pendingCommissions -> {
                    if (pendingCommissions.isEmpty()) {
                        return Mono.empty();
                    }
                    List<CommissionEntity> firstHalf = pendingCommissions.stream()
                            .filter(commission -> commission.getCreatedAt().toLocalDateTime().getDayOfMonth() <= 15)
                            .collect(Collectors.toList());

                    List<CommissionEntity> secondHalf = pendingCommissions.stream()
                            .filter(commission -> commission.getCreatedAt().toLocalDateTime().getDayOfMonth() > 15)
                            .collect(Collectors.toList());
                    BigDecimal totalFirstHalf = firstHalf.stream()
                            .map(CommissionEntity::getCommissionAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal totalSecondHalf = secondHalf.stream()
                            .map(CommissionEntity::getCommissionAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    firstHalf.sort(Comparator.comparing(CommissionEntity::getCreatedAt));
                    secondHalf.sort(Comparator.comparing(CommissionEntity::getCreatedAt));
                    Timestamp firstDisbursementDate = firstHalf.isEmpty() ? null : firstHalf.get(0).getCreatedAt();
                    Timestamp lastDisbursementDate = secondHalf.isEmpty() ? null : secondHalf.get(secondHalf.size() - 1).getCreatedAt();

                    CommissionGroupResponse response = new CommissionGroupResponse();
                    response.setTotalCommissionAmount(totalFirstHalf.add(totalSecondHalf));
                    response.setNumberOfCommissions(firstHalf.size() + secondHalf.size());
                    response.setFirstDisbursementDate(firstDisbursementDate);
                    response.setLastDisbursementDate(lastDisbursementDate);
                    response.setFirstHalfCommissions(firstHalf);
                    response.setSecondHalfCommissions(secondHalf);

                    return Mono.just(response);
                });
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

    @Override
    public Mono<String> generateSerialNumber() {
        return commissionRepository.findLastSerialNumber()
                .filter(lastSerial -> lastSerial.matches("\\d+"))
                .map(this::incrementSerialNumber)
                .switchIfEmpty(Mono.just("001"));
    }

    private String incrementSerialNumber(String lastSerial) {
        int number = Integer.parseInt(lastSerial) + 1;
        return String.format("%03d", number);
    }
}