package com.proriberaapp.ribera.Domain.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Table("category_room_type")
@Data
@Builder
public class CategoryRoomType {
    @Id
    @Column("id")
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
