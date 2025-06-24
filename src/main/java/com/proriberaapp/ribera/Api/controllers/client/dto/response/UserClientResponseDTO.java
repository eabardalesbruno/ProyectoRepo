package com.proriberaapp.ribera.Api.controllers.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserClientResponseDTO {
    private Integer id;
    private Integer country;
    private String firstname;
    private String lastname;
    private Integer gender;
    private Integer documenttype;
    private String documentnumber;
    private Timestamp birthdate;
    private String username;
    private boolean isuserinclub;
    private String email;
}
