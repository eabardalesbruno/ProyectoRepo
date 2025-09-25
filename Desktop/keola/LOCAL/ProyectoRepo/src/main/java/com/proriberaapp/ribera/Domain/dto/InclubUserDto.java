package com.proriberaapp.ribera.Domain.dto;

import lombok.Data;
import lombok.Builder;
import java.util.List;

@Data
@Builder
public class InclubUserDto {
    private Integer idUser;
    private String username;
    private List<Integer> creationDate; // [2021,2,8,11,52,12]
    private String documentNumber;
    private String name;
    private String gender;
    private String lastName;
    private String email;
    private String cellPhone;
    private String documentName;
    private Integer state;
    private String sponsorName;
    private String sponsorLastName;
    private String sponsorEmail;

    // Getters y setters generados por Lombok (@Data)
}
