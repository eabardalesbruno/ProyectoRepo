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
@Table("quotation_roomoffer")
public class QuotationRoomOfferEntity {
    @Id
    @Column("id")
    private Integer id;

    @Column("quotation_id")
    private Integer quotationId;

    @Column("room_offer_id")
    private Integer roomOfferId;
}
