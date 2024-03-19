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
@Table("offertype")
public class OfferTypeEntity {
    @Id
    @Column("offertypeid")
    private Integer offerTypeId;
    @Column("offertypename")
    private String offerTypeName;
    @Column("offertypedescription")
    private String offerTypeDescription;
}
