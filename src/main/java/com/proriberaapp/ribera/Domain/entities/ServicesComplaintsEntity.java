package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("servicescomplaints")
public class ServicesComplaintsEntity {

    @Id
    @Column("servicescomplaintsid")
    private Integer servicesComplaintsId;

    @Column("servicescomplaintsdesc")
    private String servicesComplaintsDesc;
}
