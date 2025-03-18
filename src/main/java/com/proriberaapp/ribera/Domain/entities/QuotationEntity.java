package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.proriberaapp.ribera.Api.controllers.client.dto.quotationDayDto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@Builder
@Table("quotation")
public class QuotationEntity {
    @Id
    @Column("quotation_id")
    private Integer quotationId;

    @Column("quotation_description")
    private String quotationDescription;

    @Column("infant_cost")
    private BigDecimal infantCost;

    @Column("kid_cost")
    private BigDecimal kidCost;

    @Column("adult_cost")
    private BigDecimal adultCost;

    @Column("adult_mayor_cost")
    private BigDecimal adultMayorCost;

    @Column("adult_extra_cost")
    private BigDecimal adultExtraCost;

    @Column("kid_reward")
    private BigInteger kidReward;

    @Column("adult_reward")
    private BigInteger adultReward;

    @Column("adult_mayor_reward")
    private BigInteger adultMayorReward;

    @Column("adult_extra_reward")
    private BigInteger adultExtraReward;

}
