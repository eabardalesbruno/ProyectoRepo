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
@Table("currencytype")
public class CurrencyTypeEntity {
    @Id
    @Column("currencytypeid")
    private Integer currencyTypeId;

    @Column("currencytypename")
    private String currencyTypeName;

    @Column("currencytypedescription")
    private String currencyTypeDescription;
}