package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Data
@Builder
@Table("comfortservicedetail")
public class ComfortServiceDetailEntity implements Serializable {
    @Column("roomofferid")
    private Integer roomOfferId;
    @Column("comforttypeid")
    private Integer comfortTypeId;
}
