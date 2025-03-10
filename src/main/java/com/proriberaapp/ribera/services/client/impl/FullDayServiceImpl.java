package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.*;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.services.client.CommissionService;
import com.proriberaapp.ribera.services.client.FullDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FullDayServiceImpl implements FullDayService {

    private final FullDayRepository fullDayRepository;

    private final FullDayDetailRepository fullDayDetailRepository;

    private final FullDayFoodRepository fullDayFoodRepository;

    private final CommissionRepository commissionRepository;

    private final CommissionService commissionService;

    private final TicketEntryFullDayRepository ticketEntryFullDayRepository;

    private final FullDayTypeFoodRepository fullDayTypeFoodRepository;

    @Override
    public Mono<FullDayEntity> registerFullDay(Integer receptionistId, Integer userPromoterId, Integer userClientId, String type, Timestamp bookingdate,
                                               List<FullDayDetailEntity> details, List<FullDayFoodEntity> foods) {
        FullDayEntity fullDay = FullDayEntity.builder()
                .receptionistId(receptionistId)
                .userPromoterId(userPromoterId)
                .userClientId(userClientId)
                .type(type)
                .purchaseDate(Timestamp.from(Instant.now()))
                .totalPrice(BigDecimal.ZERO)
                .bookingstateid(3)
                .bookingDate(bookingdate)
                .build();

        return fullDayRepository.save(fullDay)
                .flatMap(savedEntity -> Flux.fromIterable(details)
                        .flatMap(detail -> {
                            detail.setFulldayid(savedEntity.getFulldayid());
                            return calcularPrecios(detail, type);
                        })
                        .flatMap(fullDayDetailRepository::save)
                        .collectList()
                        .flatMap(savedDetails -> {
                            if (type.equalsIgnoreCase("Full Day Todo Completo")) {
                                return saveFood(savedDetails, foods).thenReturn(savedDetails);
                            }
                            return Mono.just(savedDetails);
                        })
                        .flatMap(savedDetails -> {
                            BigDecimal totalPrice = savedDetails.stream()
                                    .map(FullDayDetailEntity::getFinalPrice)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                            savedEntity.setTotalPrice(totalPrice);

                            return fullDayRepository.save(savedEntity);
                        })
                        .flatMap(updatedEntity -> {
                            if (type.equalsIgnoreCase("Full Day Todo Completo")) {
                                return fullDayRepository.findById(updatedEntity.getFulldayid())
                                        .flatMap(existingFullDay -> calculateAndSaveCommission(existingFullDay, details));
                            }
                            return Mono.just(updatedEntity);
                        })
                );
    }

    @Override
    public Mono<Void> saveFood(List<FullDayDetailEntity> savedDetails, List<FullDayFoodEntity> foods) {
        if (savedDetails.isEmpty() || foods.isEmpty()) {
            return Mono.empty();
        }

        return Flux.fromIterable(foods)
                .flatMap(food -> {
                    FullDayDetailEntity targetDetail = savedDetails.stream()
                            .filter(detail -> detail.getQuantity() > 0)
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("No se encuentra detalle correspondiente"));

                    targetDetail.getFulldayTypefoodid().add(food.getFulldayTypefoodid());
                    targetDetail.setQuantity(targetDetail.getQuantity() - 1);
                    food.setFulldaydetailid(targetDetail.getFulldaydetailid());
                    return fullDayFoodRepository.save(food);
                })
                .then();
    }


    private Mono<FullDayDetailEntity> calcularPrecios(FullDayDetailEntity detail, String type) {
        return getBasePrice(detail.getTypePerson())
                .flatMap(basePrice -> {
                    BigDecimal discount;
                    if (type.equalsIgnoreCase("Full Day Todo Completo")) {
                        discount = basePrice.multiply(BigDecimal.valueOf(0.50));
                    } else {
                        discount = BigDecimal.ZERO;
                    }
                    if (detail.getFulldayTypefoodid() == null || detail.getFulldayTypefoodid().isEmpty()) {
                        BigDecimal foodPrice = BigDecimal.ZERO;
                        if (type.equalsIgnoreCase("Full Day Todo Completo")) {
                            switch (detail.getTypePerson().toUpperCase()) {
                                case "ADULTO":
                                    foodPrice = BigDecimal.valueOf(50);
                                    break;
                                case "ADULTO_MAYOR":
                                    foodPrice = BigDecimal.valueOf(35);
                                    break;
                                case "NINO":
                                    foodPrice = BigDecimal.valueOf(35);
                                    break;
                                case "INFANTE":
                                    foodPrice = BigDecimal.ZERO;
                                    break;
                                default:
                                    foodPrice = BigDecimal.ZERO;
                            }
                        }
                        BigDecimal finalPrice = basePrice.subtract(discount).add(foodPrice)
                                .multiply(BigDecimal.valueOf(detail.getQuantity()));
                        detail.setBasePrice(basePrice);
                        detail.setFoodPrice(foodPrice);
                        detail.setDiscountApplied(discount);
                        detail.setFinalPrice(finalPrice);

                        return Mono.just(detail);
                    }
                    Mono<BigDecimal> foodPriceMono = Flux.fromIterable(detail.getFulldayTypefoodid())
                            .flatMap(this::getFoodPriceById)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return foodPriceMono.flatMap(foodPrice -> {
                        BigDecimal finalPrice = basePrice.subtract(discount).add(foodPrice)
                                .multiply(BigDecimal.valueOf(detail.getQuantity()));
                        detail.setBasePrice(basePrice);
                        detail.setFoodPrice(foodPrice);
                        detail.setDiscountApplied(discount);
                        detail.setFinalPrice(finalPrice);
                        return Mono.just(detail);
                    });
                });
    }

    private Mono<BigDecimal> getFoodPriceById(Integer fulldayTypefoodid) {
        return fullDayTypeFoodRepository.findById(fulldayTypefoodid)
                .map(FullDayTypeFoodEntity::getPrice)
                .defaultIfEmpty(BigDecimal.ZERO);
    }

    private Mono<BigDecimal> getBasePrice(String typePerson) {
        return ticketEntryFullDayRepository.findByTicketEntryFullDayId(1)
                .flatMap(ticket -> {
                    switch (typePerson.toUpperCase()) {
                        case "ADULTO":
                            return Mono.just(ticket.getAdultPrice());
                        case "ADULTO_MAYOR":
                            return Mono.just(ticket.getSeniorPrice());
                        case "NINO":
                            return Mono.just(ticket.getChildPrice());
                        case "INFANTE":
                            return Mono.just(ticket.getInfantPrice());
                        default:
                            return Mono.error(new IllegalArgumentException("Tipo de persona no válido"));
                    }
                });
    }

    private Mono<FullDayEntity> calculateAndSaveCommission(FullDayEntity fullDay, List<FullDayDetailEntity> details) {
        BigDecimal commissionAmount = BigDecimal.ZERO;

        for (FullDayDetailEntity detail : details) {
            BigDecimal commissionRate = getCommissionRate(detail.getTypePerson());
            BigDecimal calculatedCommission = commissionRate.multiply(BigDecimal.valueOf(detail.getQuantity()));
            commissionAmount = commissionAmount.add(calculatedCommission);
        }

        if (commissionAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.just(fullDay);
        }

        Timestamp createdAt = Timestamp.from(Instant.now());
        Timestamp disbursementDate = calculateDisbursementDate(createdAt);

        CommissionEntity commission = new CommissionEntity();
        commission.setPromoterId(fullDay.getUserPromoterId());
        commission.setReceptionistId(fullDay.getReceptionistId());
        commission.setCommissionAmount(commissionAmount);
        commission.setRiberaAmount(fullDay.getTotalPrice());
        commission.setCaseType(1);
        commission.setPartnerPayment(BigDecimal.ZERO);
        commission.setAdminFee(BigDecimal.ZERO);
        commission.setServiceFee(BigDecimal.ZERO);
        commission.setStatus("Pendiente");

        commission.setDayBookingInit(fullDay.getPurchaseDate());
        commission.setCreatedAt(createdAt);
        commission.setDisbursementDate(disbursementDate);
        return commissionService.generateSerialNumber()
                .doOnNext(commission::setSerialNumber)
                .then(commissionRepository.save(commission))
                .thenReturn(fullDay);
    }

    private BigDecimal getCommissionRate(String typePerson) {
        if (typePerson == null || typePerson.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        String normalizedType = typePerson.trim().toUpperCase();
        BigDecimal rate;
        switch (normalizedType) {
            case "ADULTO":
                rate = BigDecimal.valueOf(10);
                break;
            case "NINO":
            case "ADULTO_MAYOR":
                rate = BigDecimal.valueOf(7.50);
                break;
            default:
                rate = BigDecimal.ZERO;
        }
        return rate;
    }

    private Timestamp calculateDisbursementDate(Timestamp createdAt) {
        LocalDate createdDate = createdAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate disbursementDate;
        if (createdDate.getDayOfMonth() <= 15) {
            disbursementDate = LocalDate.of(createdDate.getYear(), createdDate.getMonth(), 20);
        } else {
            disbursementDate = LocalDate.of(createdDate.plusMonths(1).getYear(), createdDate.plusMonths(1).getMonth(), 5);
        }
        return Timestamp.valueOf(disbursementDate.atStartOfDay());
    }
}