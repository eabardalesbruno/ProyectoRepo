package com.proriberaapp.ribera.Domain.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("exchangetype")
public class ExchangeTypeEntity {
    @Id
    private Integer exchangetypeid;
    private String exchangetypedesc;
}
