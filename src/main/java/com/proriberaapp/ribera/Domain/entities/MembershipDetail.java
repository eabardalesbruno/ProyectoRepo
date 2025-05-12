package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MembershipDetail {
    private Integer membershipId;
    private Integer adults;
    private Integer adultMayor;
    private Integer children;
    private Integer infants;
    private Integer membershipQuantity;
}