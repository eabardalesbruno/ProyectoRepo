package com.proriberaapp.ribera.Api.controllers.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BookingConflictDto {
    private Integer bookingid;
    private String datebookinginit;
    private String datebookingend;
}
