package com.proriberaapp.ribera.Domain.entities;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.proriberaapp.ribera.Domain.entities.RoomTypeEntity.RoomType;

import io.r2dbc.postgresql.codec.Json;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomTypeViewEntity {
    @Id
    @Column("roomtypeid")
    private Integer roomTypeId;
    @Column("roomtype")
    private RoomType roomType;
    @Column("roomtypename")
    private String roomTypeName;
    @Column("roomtypedescription")
    private String roomTypeDescription;
    @Column("roomstateid")
    private Integer roomstateid;
    @Column("roomstateid")
    private Integer roomStateId;
    @Column("roomstatename")
    private String roomStateName;
    @Column("roomstatedescription")
    private String roomStateDescription;
    private Integer id;
    @Column("name")
    private String name;
    @Column("mincapacity")
    private Integer minCapacity;
    @Column("maxcapacity")
    private Integer maxCapacity;
    @Column("groupmincalculate")
    private Json groupMinCalculate;
    @Column("groupmaxcalculate")
    private Json groupMaxCalculate;

}
