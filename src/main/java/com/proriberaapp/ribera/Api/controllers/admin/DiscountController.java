package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.dto.UserNameAndDiscountDto;
import com.proriberaapp.ribera.services.admin.DiscountAdminService;
import com.proriberaapp.ribera.utils.constants.DiscountTypeCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequestMapping("${url.manager}/booking/discount")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountAdminService discountAdminService;

    @GetMapping("/{discountType}")
    public Mono<UserNameAndDiscountDto> getDiscount(
            @PathVariable DiscountTypeCode discountType, @RequestParam Integer userId,
            @RequestParam BigDecimal costTotal, @RequestParam BigDecimal totalAmountFeeding) {
        return this.discountAdminService.getPercentageDiscount(discountType, userId, costTotal,totalAmountFeeding);
    }
}
