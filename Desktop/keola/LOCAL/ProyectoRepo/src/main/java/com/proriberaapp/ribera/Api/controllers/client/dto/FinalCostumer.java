package com.proriberaapp.ribera.Api.controllers.client.dto;

import com.proriberaapp.ribera.Domain.entities.FinalCostumerEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FinalCostumer {
    private String firstName;
    private String lastName;
    private String documentType;
    private String documentNumber;
    private Integer yearOld;

    public static FinalCostumerEntity toFinalCostumerEntity(Integer bookingId, FinalCostumer finalCostumer) {
        return FinalCostumerEntity.builder()
                .bookingId(bookingId)
                .firstName(finalCostumer.getFirstName())
                .lastName(finalCostumer.getLastName())
                .documentType(finalCostumer.getDocumentType())
                .documentNumber(finalCostumer.getDocumentNumber())
                .yearOld(finalCostumer.getYearOld())
                .build();
    }
}
