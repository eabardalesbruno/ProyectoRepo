package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("userlevel")
public class UserLevelEntity {
    private Integer userLevelId;
    private String levelName;
    private String levelDescription;
}
