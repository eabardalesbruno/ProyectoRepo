package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Table("roomoffer")
public class RoomOfferEntity {
    @Id
    @Column("roomofferid")
    private Integer roomOfferId;
    @Column("roomid")
    private Integer roomId;
    @Column("offertypeid")
    private Integer offerTypeId;
    private BigDecimal cost;
    @Column("offertimeinit")
    private LocalDateTime offerTimeInit;
    @Column("offertimeend")
    private LocalDateTime offerTimeEnd;
    @Column("offername")
    private String offerName;
    @Column("riberapoints")
    private Integer riberaPoints;
    @Column("inresortpoints")
    private Integer inResortPoints;
    private Integer points;
    @Column("numberdays")
    private Integer numberDays;
    @Column("numbernights")
    private Integer numberNights;
}
