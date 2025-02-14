package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.CommissionEntity;
import com.proriberaapp.ribera.Domain.entities.FullDayDetailEntity;
import com.proriberaapp.ribera.Domain.entities.FullDayEntity;
import com.proriberaapp.ribera.Domain.entities.FullDayFoodEntity;
import com.proriberaapp.ribera.Infraestructure.repository.*;
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

@Service
@RequiredArgsConstructor
public class FullDayServiceImpl implements FullDayService {

    private final FullDayRepository fullDayRepository;

    private final FullDayDetailRepository fullDayDetailRepository;

    private final FullDayFoodRepository fullDayFoodRepository;

    private final CommissionRepository commissionRepository;

    @Override
    public Mono<FullDayEntity> registerFullDay(Integer receptionistId, Integer userPromoterId, Integer userClientId, String type,
                                               List<FullDayDetailEntity> details, List<FullDayFoodEntity> foods) {
        FullDayEntity fullDay = FullDayEntity.builder()
                .receptionistId(receptionistId)
                .userPromoterId(userPromoterId)
                .userClientId(userClientId)
                .type(type)
                .purchaseDate(Timestamp.from(Instant.now()))
                .totalPrice(BigDecimal.ZERO)
                .build();

        return fullDayRepository.save(fullDay)
                .flatMap(savedEntity -> Flux.fromIterable(details)
                        .map(detail -> {
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
                .index()
                .flatMap(tuple -> {
                    long index = tuple.getT1();
                    FullDayFoodEntity food = tuple.getT2();
                    FullDayDetailEntity targetDetail = savedDetails.get((int) (index % savedDetails.size()));
                    food.setFulldaydetailid(targetDetail.getFulldaydetailid());

                    return fullDayFoodRepository.save(food);
                })
                .then();
    }


    private FullDayDetailEntity calcularPrecios(FullDayDetailEntity detail, String type) {
        BigDecimal basePrice = getBasePrice(detail.getTypePerson());
        BigDecimal foodPrice = type.equalsIgnoreCase("Full Day Todo Completo") ? getFoodPrice(detail.getTypePerson()) : BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;
        if (type.equalsIgnoreCase("Full Day Todo Completo")) {
            discount = basePrice.multiply(BigDecimal.valueOf(0.50));
        }
        BigDecimal finalPrice = basePrice.subtract(discount).add(foodPrice).multiply(BigDecimal.valueOf(detail.getQuantity()));

        detail.setBasePrice(basePrice);
        detail.setFoodPrice(foodPrice);
        detail.setDiscountApplied(discount);
        detail.setFinalPrice(finalPrice);

        return detail;
    }

    private BigDecimal getBasePrice(String typePerson) {
        switch (typePerson.toUpperCase()) {
            case "ADULTO":
                return BigDecimal.valueOf(40);
            case "ADULTO_MAYOR":
                return BigDecimal.valueOf(30);
            case "NINO":
                return BigDecimal.valueOf(30);
            default:
                throw new IllegalArgumentException("Tipo de persona no válido");
        }
    }

    private BigDecimal getFoodPrice(String typePerson) {
        switch (typePerson.toUpperCase()) {
            case "ADULTO":
                return BigDecimal.valueOf(50);
            case "ADULTO_MAYOR":
                return BigDecimal.valueOf(30);
            case "NINO":
                return BigDecimal.valueOf(30);
            default:
                return BigDecimal.ZERO;
        }
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
        commission.setCreatedAt(createdAt);
        commission.setDisbursementDate(disbursementDate);
        return commissionRepository.save(commission)
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