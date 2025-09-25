package com.proriberaapp.ribera.Crosscutting.config;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.angus.mail.imap.protocol.ID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.proriberaapp.ribera.services.discount.DiscountForPoint;
import com.proriberaapp.ribera.services.discount.IDiscount;

@Configuration
public class DiscountsConfig {

    @Bean
    List<IDiscount> discounts() {
        List<IDiscount> discounts = new ArrayList();
        discounts.add(new DiscountForPoint());
        return discounts;
    }
}
