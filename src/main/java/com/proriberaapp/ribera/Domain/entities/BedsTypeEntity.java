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
@Table("bedstype")
public class BedsTypeEntity {
    @Id
    @Column("bedtypeid")
    private Integer bedTypeId;
    @Column("bedtypename")
    private String bedTypeName;
    @Column("bedtypedescription")
    private String bedTypeDescription;
    @Column("quantity")
    private Integer quantity;
    @Column("bedsizeid")
    private Integer bedsizeid;
    @Column("bedstateid")
    private Integer bedstateid;

    public Object complitToString() {
        return "BedsTypeEntity{" +
                "bedTypeId=" + bedTypeId +
                ", bedTypeName='" + bedTypeName + '\'' +
                ", bedTypeDescription='" + bedTypeDescription + '\'' +
                '}';
    }
}
