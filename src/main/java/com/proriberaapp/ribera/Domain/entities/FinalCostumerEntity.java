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
@Table("finalcostumer")
public class FinalCostumerEntity {
    @Id
    @Column("finalcostumerid")
    private Integer finalCostumerId;
    @Column("bookingid")
    private Integer bookingId;
    @Column("documenttype")
    private String documentType;
    @Column("documentnumber")
    private String documentNumber;
    @Column("firstname")
    private String firstName;
    @Column("lastname")
    private String lastName;
    @Column("yearold")
    private Integer yearOld;
}
