package com.proriberaapp.ribera.Crosscutting.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.proriberaapp.ribera.services.point.PointTransactionTypeEnum;
import com.proriberaapp.ribera.services.point.PointTransferStrategy;
import com.proriberaapp.ribera.services.point.PointsTransactionStrategy;

@Configuration
public class PointsStrategyConfig {
    @Bean
    public Map<PointTransactionTypeEnum, PointsTransactionStrategy<?>> strategies(
            PointTransferStrategy transferStrategy) {
        Map<PointTransactionTypeEnum, PointsTransactionStrategy<?>> strategies = new HashMap<>();
        strategies.put(PointTransactionTypeEnum.TRANSFER, transferStrategy);
        return strategies;
    }

}