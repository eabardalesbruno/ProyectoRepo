package com.proriberaapp.ribera.Domain.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("service")
public class ServicesEntity {
    @Id
    private Integer serviceid;
    private String servicedesc;
}
