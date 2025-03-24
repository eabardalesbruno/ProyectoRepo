package com.proriberaapp.ribera.Domain.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("quotation_year")
public class QuotationYearEntity {
    @Id
    @Column("id")
    private Integer id;

    @Column("description")
    private String description;
}
