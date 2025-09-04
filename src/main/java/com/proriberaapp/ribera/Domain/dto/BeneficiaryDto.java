package com.proriberaapp.ribera.Domain.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class BeneficiaryDto {
    private Integer id;
    private String nombres;
    private String apellidos;
    private String documento;
    private LocalDate fechaNacimiento;
    private Integer edad;
    private String correo;
    private Integer visitas;
    private String membresia;
    private String usuario;
    private Integer estado;
}
