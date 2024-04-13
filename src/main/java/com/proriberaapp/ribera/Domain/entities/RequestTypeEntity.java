package com.proriberaapp.ribera.Domain.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("requesttype")
public class RequestTypeEntity {
    @Id
    private Integer requesttypeid;
    private String requesttypedesc;
}
