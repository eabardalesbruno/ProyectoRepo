package com.proriberaapp.ribera.Domain.mapper;

import com.proriberaapp.ribera.Api.controllers.client.dto.FullDayRateDto;
import com.proriberaapp.ribera.Domain.dto.NotificationDto;
import com.proriberaapp.ribera.Domain.entities.FullDayRateEntity;
import com.proriberaapp.ribera.Domain.entities.NotificationBookingEntity;

public class FullDayRateMapper {
    public static FullDayRateDto toDto(FullDayRateEntity fullDayRateEntity){
        return FullDayRateDto.builder()
                .rateId(fullDayRateEntity.getRateId())
                .title(fullDayRateEntity.getTitle())
                .price(fullDayRateEntity.getPrice())
                .description(fullDayRateEntity.getDescription())
                .userCategory(fullDayRateEntity.getUserCategory())
                .rateType(fullDayRateEntity.getRateType())
                .rateStatus(fullDayRateEntity.getRateStatus())
                .build();
    }

    public static FullDayRateEntity toEntity(FullDayRateDto fullDayRateDto){
        return FullDayRateEntity.builder()
                .title(fullDayRateDto.getTitle())
                .price(fullDayRateDto.getPrice())
                .description(fullDayRateDto.getDescription())
                .userCategory(fullDayRateDto.getUserCategory())
                .rateType(fullDayRateDto.getRateType())
                .rateStatus(fullDayRateDto.getRateStatus() != null ? fullDayRateDto.getRateStatus() : true)
                .build();
    }
}
