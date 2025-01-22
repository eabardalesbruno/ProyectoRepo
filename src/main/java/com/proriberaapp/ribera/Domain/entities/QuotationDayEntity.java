package com.proriberaapp.ribera.Domain.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Table("quotation_day")
@Setter
@Getter
@Builder
public class QuotationDayEntity {
    @Id
    @Column("id")
    private Integer id;
    @Column("idquotation")
    private Integer idQuotation;
    @Column("idday")
    private Integer idDay;
}
