package com.proriberaapp.ribera.Domain.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveFamilyPackageResponseDto {

    private boolean result;
    private List<FamilyPackageResponseDto> data;
}
