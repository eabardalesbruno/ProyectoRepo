package com.proriberaapp.ribera.Domain.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class RoomCapacityDTO {
    private Integer adults;
    private Integer children;
    private Integer babies;
    private Integer adultsExtra;
    private Integer adultsMayor;
    private Integer total;
}
