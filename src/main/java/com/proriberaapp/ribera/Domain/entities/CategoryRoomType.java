package com.proriberaapp.ribera.Domain.entities;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Data;

@Table("category_room_type")
@Data
@Builder
public class CategoryRoomType {
    @Id
    @Column("id")
    private Integer id;
    @Column("name")
    private String name;
    @Column("mincapacity")
    private Integer minCapacity;
    @Column("maxcapacity")
    private Integer maxCapacity;
    @Column("groupmincalculate")
    private Object groupMinCalculate;
    @Column("groupmaxcalculate")
    private Object groupMaxCalculate;
}
