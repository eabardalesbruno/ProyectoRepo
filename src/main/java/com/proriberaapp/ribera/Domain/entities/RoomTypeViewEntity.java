package com.proriberaapp.ribera.Domain.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;

import com.proriberaapp.ribera.Domain.entities.RoomTypeEntity.RoomType;

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
    @Column("minchildrencapacity")
    private Integer minChildrenCapacity;
    @Column("mintotalcapacity")
    private Integer minTotalCapacity;
    @Column("minadultcapacity")
    private Integer minAdultCapacity;
    @Column("mintotalnumberincludeschildren")
    private boolean minTotalNumberIncludesChildren;
    @Column("ministotalnumber")
    private boolean minIsTotalNumber;
    @Column("maxchildrencapacity")
    private Integer maxChildrenCapacity;
    @Column("maxtotalcapacity")
    private Integer maxTotalCapacity;
    @Column("maxadultcapacity")
    private Integer maxAdultCapacity;
    @Column("maxtotalnumberincludeschildren")
    private boolean maxTotalNumberIncludesChildren;
    @Column("maxistotalnumber")
    private boolean maxIsTotalNumber;
    @Column("mininfantcapacity")
    private Integer minInfantcapacity;
    @Column("maxinfantcapacity")
    private Integer maxInfantcapacity;
}
