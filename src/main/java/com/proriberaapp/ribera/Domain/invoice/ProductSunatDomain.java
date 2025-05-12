package com.proriberaapp.ribera.Domain.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Table("product_sunat")
public class ProductSunatDomain {

    @Id
    @Column("product_sunat_id")
    private Integer productSunatId;

    @Column("cod_sunat")
    private String codSunat;

    @Column("description")
    private String description;

}
