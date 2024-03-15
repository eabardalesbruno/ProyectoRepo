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
@Table("comforttype")
public class ComfortTypeEntity {
    @Id
    @Column("comforttypeid")
    private Integer comfortTypeId;
    @Column("comforttypename")
    private String comfortTypeName;
    @Column("comforttypedescription")
    private String comfortTypeDescription;
    private Boolean active;
}
