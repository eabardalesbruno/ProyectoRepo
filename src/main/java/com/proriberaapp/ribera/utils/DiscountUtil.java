package com.proriberaapp.ribera.utils;

import com.proriberaapp.ribera.Domain.dto.DiscountDto;

import com.proriberaapp.ribera.Domain.dto.UserNameAndDiscountDto;
import com.proriberaapp.ribera.utils.constants.DiscountTypeCode;
import reactor.core.publisher.Mono;

import java.util.List;

public class DiscountUtil {

    public static UserNameAndDiscountDto calculateDiscount(UserNameAndDiscountDto discount,
                                                           float totalAccommodation,
                                                           float totalAmountFeeding,
                                                           DiscountTypeCode discountType) {
        switch (discountType) {
            case DISCOUNT_MEMBER:
                applyMemberDiscount(discount, totalAccommodation, totalAmountFeeding);
                break;
            case USD_REWARD:
                applyUsdRewardDiscount(discount, totalAccommodation, totalAmountFeeding);
                break;
            case DISCOUNT_COUPON:
                applyCouponDiscount(discount, totalAccommodation);
                break;
        }
        return discount;
    }

    private static void applyMemberDiscount(UserNameAndDiscountDto discount, float totalAccommodation, float totalAmountFeeding) {
        float totalPercentageDiscountAccommodation = discount.getDiscounts().stream()
                .filter(DiscountDto::isApplyToReservation)
                .map(DiscountDto::getPercentage)
                .reduce(0F, Float::sum);

        float totalPercentageDiscountFood = discount.getDiscounts().stream()
                .filter(DiscountDto::isApplyToFood)
                .map(DiscountDto::getPercentage)
                .reduce(0F, Float::sum);

        float totalAccommodationWithDiscount = totalAccommodation * totalPercentageDiscountAccommodation / 100;
        float totalFoodWithDiscount = totalAmountFeeding * totalPercentageDiscountFood / 100;

        discount.setTotalDiscountAccommodation(totalAccommodationWithDiscount);
        discount.setTotalPercentageDiscountAccommodation(totalPercentageDiscountAccommodation);
        discount.setTotalDiscountFood(totalFoodWithDiscount);
        discount.setTotalPercentageDiscountFood(totalPercentageDiscountFood);
        discount.setTotalAmount(totalAccommodation - totalAccommodationWithDiscount +
                totalAmountFeeding - totalFoodWithDiscount);
    }

    private static void applyUsdRewardDiscount(UserNameAndDiscountDto discount, float totalAccommodation, float totalAmountFeeding) {
        float totalDiscountAmount = discount.getDiscounts().stream()
                .map(DiscountDto::getAmount)
                .reduce(0F, Float::sum);

        discount.setTotalDiscountAccommodation(totalDiscountAmount);
        discount.setTotalPercentageDiscountAccommodation(discount.getPercentage());
        discount.setTotalAmount(totalAccommodation + totalAmountFeeding - totalDiscountAmount);
    }

    private static void applyCouponDiscount(UserNameAndDiscountDto discount, float totalAccommodation) {
        float maxDiscount = discount.getDiscounts().stream()
                .map(DiscountDto::getPercentage)
                .max(Float::compare)
                .orElse(0F);

        float discountAmount = totalAccommodation * maxDiscount / 100;

        discount.setTotalDiscountAccommodation(discountAmount);
        discount.setTotalPercentageDiscountAccommodation(maxDiscount);
        discount.setTotalAmount(totalAccommodation - discountAmount);
    }

    public static Mono<UserNameAndDiscountDto> calculateRewardsDiscount(Integer userId) {
        // find by bookingId for rewards value
        // validate rewards value by userId
        // Applied discount value
        return Mono.just(UserNameAndDiscountDto.builder()
                        .discounts(List.of())
                        .totalDiscountAccommodation(10f)
                        .totalDiscountFood(30f)
                .build());//new UserNameAndDiscountDto());
    }
    public static Mono<UserNameAndDiscountDto> calculateCouponDiscount(Integer userId) {
        // Implementar lógica para DISCOUNT_COUPON
        return Mono.just(null);//new UserNameAndDiscountDto());
    }

}




