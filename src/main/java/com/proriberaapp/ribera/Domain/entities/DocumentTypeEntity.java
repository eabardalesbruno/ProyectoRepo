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
@Table("documenttype")
public class DocumentTypeEntity {

    @Id
    @Column("documenttypeid")
    private Integer documentTypeId;

    @Column("documenttypedesc")
    private String documentTypeDesc;

    // Constructor, Getters, Setters y otros campos según sea necesario

}