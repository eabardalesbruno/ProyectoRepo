package com.proriberaapp.ribera.Api.controllers.admin.dto.booking.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDetailDto {
    private String fullname;
    private String email;
    private String fullcellnumber;
    private Integer documenttypeid;
    private String documentnumber;
}
