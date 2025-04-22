package com.proriberaapp.ribera.services.admin.impl;

import com.proriberaapp.ribera.Domain.dto.DiscountDto;
import com.proriberaapp.ribera.Domain.dto.UserNameAndDiscountDto;
import com.proriberaapp.ribera.services.admin.DiscountAdminService;
import com.proriberaapp.ribera.services.admin.VerifiedDiscountAdminService;
import com.proriberaapp.ribera.services.client.RoomOfferService;
import com.proriberaapp.ribera.utils.DiscountUtil;
import com.proriberaapp.ribera.utils.constants.DiscountTypeCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountAdminServiceImpl implements DiscountAdminService {

    private final VerifiedDiscountAdminService verifiedDiscountService;

    @Override
    public Mono<UserNameAndDiscountDto> getPercentageDiscount(
            DiscountTypeCode discountType, Integer userId, BigDecimal costTotal, BigDecimal totalAmountFeeding) {
        log.info("Start the service getPercentageDiscount with parameters: discountType :{}, userId: {}," +
                " costTotal: {}, totalAmountFeeding: {}", discountType, userId, costTotal, totalAmountFeeding);
        return getDiscount(userId, costTotal, totalAmountFeeding, discountType)
                .map(discount -> {
                    // Calcular el monto de alojamiento (total - alimentación)
                    BigDecimal totalAccommodation = costTotal.subtract(totalAmountFeeding);
                    float accommodationFloat = totalAccommodation.floatValue();
                    float feedingFloat = totalAmountFeeding.floatValue();
                    // Calcular y retornar el descuento
                    return DiscountUtil.calculateDiscount(discount, accommodationFloat, feedingFloat, discountType);
                });
    }

    private Mono<UserNameAndDiscountDto> getDiscount(
            Integer userId, BigDecimal costTotal, BigDecimal totalAmountFeeding, DiscountTypeCode discountType) {
        return switch (discountType) {
            case DISCOUNT_MEMBER -> verifiedDiscountService.verifiedPercentajeDiscountAdmin(userId, costTotal);
            case POINTS_REWARD -> {
                float costFinal = costTotal.floatValue();
                float feedingAmount = totalAmountFeeding.floatValue();

                // Calcular primer descuento (70% sobre alojamiento)
                float accommodation = costFinal - feedingAmount;
                float discount1 = accommodation * 0.7f;

                List<DiscountDto> discounts = new ArrayList<>();
                discounts.add(
                        DiscountDto.builder()
                                .amount(discount1)
                                .applyToReservation(true)
                                .name("PUNTOS REWARDS")
                                .percentage(70f)
                                .build()
                );

                // Calcular segundo descuento (20% sobre alimentación) si aplica
                float discount2 = 0f;
                if (feedingAmount > 0) {
                    discount2 = feedingAmount * 0.2f;
                    discounts.add(
                            DiscountDto.builder()
                                    .amount(discount2)
                                    .applyToReservation(true)
                                    .name("DESCUENTO ALIMENTACION")
                                    .percentage(20f)
                                    .build()
                    );
                }

                // Calcular descuento total y porcentaje global
                float totalDiscount = discount1 + discount2;
                float overallPercentage = (totalDiscount / costFinal) * 100f;

                yield Mono.just(
                        UserNameAndDiscountDto.builder()
                                .percentage(overallPercentage)
                                .discounts(discounts)
                                .build()
                );
            }

            case DISCOUNT_COUPON -> DiscountUtil.calculateCouponDiscount(userId);

            default -> Mono.error(new IllegalArgumentException("Tipo de descuento no válido"));
        };
    }
}
