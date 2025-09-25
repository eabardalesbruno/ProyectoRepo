package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("comfortbookingdetail")
public class ComfortBookingDetailEntity {
    @Column("bookingid")
    Integer bookingId;
    @Column("comforttypeid")
    Integer comfortTypeId;
}
