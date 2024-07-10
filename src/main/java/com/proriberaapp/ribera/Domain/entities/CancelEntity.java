package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("cancelreason")
public class CancelEntity {
    private Integer cancelreasonid;
    private String cancelreasonname;

}
