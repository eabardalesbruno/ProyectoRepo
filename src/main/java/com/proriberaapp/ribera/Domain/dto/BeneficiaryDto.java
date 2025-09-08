package com.proriberaapp.ribera.Domain.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Builder
public class BeneficiaryDto {
    private Integer id;
    private String name;
    private String lastName;
    private String documentNumber;

    // Formato requerido dd/MM/yyyy
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    private Integer age;
    private String email;
    private Integer visits;
    private Integer idMembership;
    private String username;
    private Integer status;

    // Formato requerido dd/MM/yyyy HH:mm:ss
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime lastCheckin;

    // Formato requerido dd/MM/yyyy HH:mm:ss
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime creationDate;
}
