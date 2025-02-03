package com.proriberaapp.ribera.Api.controllers.admin.dto;

import org.springframework.data.relational.core.mapping.Column;

import lombok.Data;

@Data
public class FeedingItemsGrouped {
    private Integer idfeeding;
    @Column("name")
    private String name;
    private float value;
}
