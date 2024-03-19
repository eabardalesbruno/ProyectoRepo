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
@Table("roomtype")
public class RoomTypeEntity {
    @Id
    @Column("roomtypeid")
    private Integer roomTypeId;
    @Column("roomtypename")
    private String roomTypeName;
    @Column("roomtypedescription")
    private String roomTypeDescription;
}
