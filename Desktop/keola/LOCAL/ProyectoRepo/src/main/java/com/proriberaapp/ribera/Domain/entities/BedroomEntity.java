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
@Table("bedroom")
public class BedroomEntity {
    @Id
    @Column("bedroomid")
    private Integer bedroomId;
    @Column("roomid")
    private Integer roomId;
    @Column("bedtypeid")
    private Integer bedTypeId;
    private Integer quantity;
}
