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
@Table("detailcomplaints")
public class DetailComplaintsEntity {

    @Id
    @Column("detailcomplaintsid")
    private Integer detailComplaintsId;

    @Column("detailcomplaintsdesc")
    private String detailComplaintsDesc;
}
