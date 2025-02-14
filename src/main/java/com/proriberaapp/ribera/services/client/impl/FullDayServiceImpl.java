package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.FullDayDetailEntity;
import com.proriberaapp.ribera.Domain.entities.FullDayEntity;
import com.proriberaapp.ribera.Domain.entities.FullDayFoodEntity;
import com.proriberaapp.ribera.Infraestructure.repository.FullDayDetailRepository;
import com.proriberaapp.ribera.Infraestructure.repository.FullDayFoodRepository;
import com.proriberaapp.ribera.Infraestructure.repository.FullDayRepository;
import com.proriberaapp.ribera.Infraestructure.repository.FullDayTypeFoodRepository;
import com.proriberaapp.ribera.services.client.FullDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FullDayServiceImpl implements FullDayService {

    private final FullDayRepository fullDayRepository;

    private final FullDayDetailRepository fullDayDetailRepository;

    private final FullDayFoodRepository fullDayFoodRepository;

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
                .flatMap(savedFullDay -> Flux.fromIterable(details)
                        .map(detail -> calcularPrecios(detail, type))
                        .flatMap(fullDayDetailRepository::save)
                        .collectList()
                        .flatMap(savedDetails -> {
                            if (type.equalsIgnoreCase("Full Day Todo Completo")) {
                                return saveFood(savedDetails, foods)
                                        .thenReturn(savedDetails);
                            } else {
                                return Mono.just(savedDetails);
                            }
                        })
                        .flatMap(savedDetails -> {
                            BigDecimal totalPrice = savedDetails.stream()
                                    .map(FullDayDetailEntity::getFinalPrice)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                            savedFullDay.setTotalPrice(totalPrice);
                            return fullDayRepository.save(savedFullDay);
                        })
                        .thenReturn(savedFullDay)
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
}

