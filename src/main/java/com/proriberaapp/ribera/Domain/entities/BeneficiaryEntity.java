package com.proriberaapp.ribera.Domain.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import java.time.LocalDate;

@Data
@Table("beneficiary")
public class BeneficiaryEntity {
    @Id
    private Integer id;
    @Column("nombres")
    private String nombres;
    @Column("apellidos")
    private String apellidos;
    @Column("documento")
    private String documento;
    @Column("fecha_nacimiento")
    private LocalDate fechaNacimiento;
    @Column("edad")
    private Integer edad;
    @Column("correo")
    private String correo;
    @Column("visitas")
    private Integer visitas;
    @Column("membresia")
    private String membresia;
    @Column("usuario")
    private String usuario;
    @Column("estado")
    private Integer estado;
}
