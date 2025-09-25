package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("refusereason")
public class RefuseEntity {
    private Integer refusereasonid;
    private String refusereasonname;

}
