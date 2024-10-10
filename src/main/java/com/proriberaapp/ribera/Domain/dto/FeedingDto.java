package com.proriberaapp.ribera.Domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FeedingDto {
    private Integer id;
    private String name;
    private String roomName;
    private BigDecimal cost;
    private Integer state;
}
